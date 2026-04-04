import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdelantoResponse } from '../../../../../core/models/pagos/adelanto-response';
import { PagosService } from '../../../../../core/services/pagos_services';
import { AdelantoService } from '../../../../../core/services/adelanto_services';
import { PagoRequest } from '../../../../../core/models/pagos/pago-request';
import { PagoResponse } from '../../../../../core/models/pagos/pago-response';
import { PagoDetalleModal } from "../modal/pago-detalle-modal/pago-detalle-modal";
import { PlanillaAdministrativoService } from '../../../../../core/services/planilla-administrativos.services';
import { PlanillaAdministrativoDTO } from '../../../../../core/models/Administrativos/planillaAdministrativoDTO';
import { AdministrativoService } from '../../../../../core/services/administrativo_services';
import { AdministrativoResponse } from '../../../../../core/models/Administrativos/administrativo-response';

@Component({
  selector: 'app-pagos',
  standalone: true,
  imports: [CommonModule, FormsModule, PagoDetalleModal],
  templateUrl: './pagos.html',
  styleUrl: './pagos.css',
})
export class Pagos implements OnInit {
  // --- Listas Dinámicas ---
  listaAdministrativos: AdministrativoResponse[] = [];
  aniosDisponibles: number[] = [2025, 2026, 2027];
  mesesDelAnio = [
    { nombre: 'Enero', valor: 'ENERO', num: '01' },
    { nombre: 'Febrero', valor: 'FEBRERO', num: '02' },
    { nombre: 'Marzo', valor: 'MARZO', num: '03' },
    { nombre: 'Abril', valor: 'ABRIL', num: '04' },
    { nombre: 'Mayo', valor: 'MAYO', num: '05' },
    { nombre: 'Junio', valor: 'JUNIO', num: '06' },
    { nombre: 'Julio', valor: 'JULIO', num: '07' },
    { nombre: 'Agosto', valor: 'AGOSTO', num: '08' },
    { nombre: 'Septiembre', valor: 'SEPTIEMBRE', num: '09' },
    { nombre: 'Octubre', valor: 'OCTUBRE', num: '10' },
    { nombre: 'Noviembre', valor: 'NOVIEMBRE', num: '11' },
    { nombre: 'Diciembre', valor: 'DICIEMBRE', num: '12' }
  ];

  // --- Estado de la Vista ---
  usuarioSeleccionadoId: number | null = null;
  mesSeleccionado: string = 'ABRIL';
  anioSeleccionado: number = 2026;

  // --- Datos Económicos ---
  sueldoBase: number = 0;
  montoTardanzas: number = 0; 
  adelantosPendientes: AdelantoResponse[] = [];
  totalAdelantos: number = 0;
  netoProyectado: number = 0;

  modalVisible = false;
  pagoGenerado: PagoResponse | null = null;

  constructor(
    private pagosService: PagosService,
    private adelantoService: AdelantoService,
    private administrativoService: AdministrativoService,
    private planillaService: PlanillaAdministrativoService 
  ) {}

  ngOnInit(): void {
    this.cargarListaPersonal();
  }

  cargarListaPersonal(): void {
    this.administrativoService.listarConContrato().subscribe({
      next: (data) => {
        this.listaAdministrativos = data;
        if (data.length > 0) {
          // Sincronizamos con el ID que viene de la respuesta (AdministrativoResponse.id)
          this.usuarioSeleccionadoId = data[0].id;
          this.cargarInformacionTodo();
        }
      },
      error: (err) => console.error('Error cargando personal:', err)
    });
  }

  cargarInformacionTodo(): void {
    if (!this.usuarioSeleccionadoId) return;

    // 1. Cargamos adelantos primero
    this.adelantoService.listarPendientesPorUsuario(this.usuarioSeleccionadoId)
      .subscribe({
        next: (data) => {
          this.adelantosPendientes = data;
          this.totalAdelantos = data.reduce((sum, item) => sum + item.monto, 0);
          // 2. Cargamos los datos de planilla (esto actualizará el sueldoBase y el neto)
          this.obtenerDatosPlanilla();
        },
        error: (err) => console.error('Error cargando adelantos:', err)
      });
  }

  obtenerDatosPlanilla(): void {
    if (!this.usuarioSeleccionadoId) return;

    const mesNum = this.obtenerNumeroMes();
    const anio = this.anioSeleccionado;
    
    const inicio = `${anio}-${mesNum}-01`;
    const ultimoDia = new Date(anio, parseInt(mesNum), 0).getDate();
    const fin = `${anio}-${mesNum}-${ultimoDia}`;

    this.planillaService.calcularPlanilla(this.usuarioSeleccionadoId, inicio, fin).subscribe({
      next: (data: PlanillaAdministrativoDTO) => {
        this.sueldoBase = data.sueldoBase;
        this.montoTardanzas = data.descuentoFaltas + data.descuentoTardanza;
        
        // CORRECCIÓN: El neto proyectado debe basarse en el sueldo neto del backend menos adelantos
        // o recalcular usando el sueldoBase que llega (el 1800)
        this.netoProyectado = data.sueldoNeto - this.totalAdelantos;
      },
      error: (err) => console.error('Error en cálculo de planilla:', err)
    });
  }

  recalcularTotales(): void {
    // Este método queda como apoyo, pero la lógica fuerte ahora está en obtenerDatosPlanilla
    this.totalAdelantos = this.adelantosPendientes.reduce((sum, item) => sum + item.monto, 0);
    this.netoProyectado = this.sueldoBase - this.totalAdelantos - this.montoTardanzas;
  }

  onGenerarPago(): void {
    if (!this.usuarioSeleccionadoId) return;

    const mesNum = this.obtenerNumeroMes();
    const ultimoDia = new Date(this.anioSeleccionado, parseInt(mesNum), 0).getDate();

    // CORRECCIÓN: Buscamos por .id, que es lo que guardamos en usuarioSeleccionadoId
    const adminActual = this.listaAdministrativos.find(a => a.id === this.usuarioSeleccionadoId);

    const payload: PagoRequest = {
      fecha: `${this.anioSeleccionado}-${mesNum}-${ultimoDia}`, 
      contratoId: adminActual?.id || 0, 
      adelantoIds: this.adelantosPendientes.map(a => a.id),
      deducciones: [], 
      bonificaciones: []
    };

    this.pagosService.crear(payload).subscribe({
      next: (response: PagoResponse) => {
        this.pagoGenerado = response; 
        this.modalVisible = true;
      },
      error: (err) => console.error('Error al generar pago:', err)
    });
  }

  private obtenerNumeroMes(): string {
    const mesEncontrado = this.mesesDelAnio.find(m => m.valor === this.mesSeleccionado);
    return mesEncontrado ? mesEncontrado.num : '01';
  }
}