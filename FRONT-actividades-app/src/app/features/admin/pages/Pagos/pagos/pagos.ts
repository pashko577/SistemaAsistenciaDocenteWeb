import { Component, OnInit,ChangeDetectorRef } from '@angular/core';
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
    private planillaService: PlanillaAdministrativoService,
    private cdr: ChangeDetectorRef 
  ) {}

  ngOnInit(): void {
    this.cargarListaPersonal();
  }

  cargarListaPersonal(): void {
    this.administrativoService.listarConContrato().subscribe({
      next: (data) => {
        this.listaAdministrativos = data;
        if (data.length > 0 && !this.usuarioSeleccionadoId) {
          this.usuarioSeleccionadoId = data[0].id;
        }
        this.cargarInformacionTodo();
      }
    });
  }

  cargarInformacionTodo(): void {
    if (!this.usuarioSeleccionadoId) return;

    // 3. Limpiar datos viejos para evitar confusión visual
    this.sueldoBase = 0;
    this.netoProyectado = 0;
    this.totalAdelantos = 0;
    this.adelantosPendientes = [];

    // 4. Encadenamos las llamadas para que se ejecuten en orden
    this.adelantoService.listarPendientesPorUsuario(this.usuarioSeleccionadoId).subscribe({
      next: (adelantos) => {
        this.adelantosPendientes = adelantos;
        this.totalAdelantos = adelantos.reduce((sum, item) => sum + item.monto, 0);
        
        // Llamamos al cálculo de planilla después de tener los adelantos
        this.obtenerDatosPlanilla();
      },
      error: (err) => console.error(err)
    });
  }

  obtenerDatosPlanilla(): void {
    const mesNum = this.obtenerNumeroMes();
    const anio = this.anioSeleccionado;
    const inicio = `${anio}-${mesNum}-01`;
    const ultimoDia = new Date(anio, parseInt(mesNum), 0).getDate();
    const fin = `${anio}-${mesNum}-${ultimoDia}`;

    this.planillaService.calcularPlanilla(this.usuarioSeleccionadoId!, inicio, fin).subscribe({
      next: (data: PlanillaAdministrativoDTO) => {
        this.sueldoBase = data.sueldoBase;
        this.montoTardanzas = data.descuentoFaltas + data.descuentoTardanza;
        this.netoProyectado = data.sueldoNeto - this.totalAdelantos;

        // 5. ¡CLAVE!: Forzar a Angular a revisar los cambios inmediatamente
        this.cdr.detectChanges(); 
      },
      error: (err) => {
        console.error('Error en cálculo:', err);
        this.cdr.detectChanges();
      }
    });
  }

  recalcularTotales(): void {
    // Este método queda como apoyo, pero la lógica fuerte ahora está en obtenerDatosPlanilla
    this.totalAdelantos = this.adelantosPendientes.reduce((sum, item) => sum + item.monto, 0);
    this.netoProyectado = this.sueldoBase - this.totalAdelantos - this.montoTardanzas;
  }

onGenerarPago(): void {
  if (!this.usuarioSeleccionadoId) return;

  // Convertimos ambos a Number para asegurar que el .find() funcione
  const idABuscar = Number(this.usuarioSeleccionadoId);
  const adminActual = this.listaAdministrativos.find(a => Number(a.id) === idABuscar);


  if (!adminActual) {

    return;
  }

  if (!adminActual.contratoId) {
    alert("Error: El administrativo seleccionado no tiene un contrato ACTIVO en el sistema.");
    return;
  }

  const payload: PagoRequest = {
    // Usamos el número de mes correctamente formateado
    fecha: `${this.anioSeleccionado}-${this.obtenerNumeroMes()}-${new Date(this.anioSeleccionado, parseInt(this.obtenerNumeroMes()), 0).getDate()}`, 
    contratoId: adminActual.contratoId,
    adelantoIds: this.adelantosPendientes.map(a => a.id),
    deducciones: [], 
    bonificaciones: []
  };

  this.pagosService.crear(payload).subscribe({
    next: (response) => {
      this.pagoGenerado = response;
      this.modalVisible = true;
    },
    error: (err) => console.error('Error al crear pago:', err)
  });
}
  private obtenerNumeroMes(): string {
    const mesEncontrado = this.mesesDelAnio.find(m => m.valor === this.mesSeleccionado);
    return mesEncontrado ? mesEncontrado.num : '01';
  }
}