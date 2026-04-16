import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

// Services
import { PagosService } from '../../../../../core/services/pagos_services';
import { AdministrativoService } from '../../../../../core/services/administrativo_services';
import { PlanillaAdministrativoService } from '../../../../../core/services/planilla-administrativos.services';

// Models
import { PagoResponse } from '../../../../../core/models/pagos/pago-response';
import { AdelantoResponse } from '../../../../../core/models/pagos/adelanto-response';
import { AdministrativoResponse } from '../../../../../core/models/Administrativos/administrativo-response';
import { DescuentoRequest } from '../../../../../core/models/pagos/descuento-request';

// Components
import { PagoDetalleModal } from "../modal/pago-detalle-modal/pago-detalle-modal";
import { Adelanto } from '../modal/adelanto/adelanto';

import { AdelantoService } from '../../../../../core/services/adelanto_services';


@Component({
  selector: 'app-pagos',
  standalone: true,
  imports: [CommonModule, FormsModule, PagoDetalleModal, Adelanto],
  templateUrl: './pagos.html',
  styleUrl: './pagos.css',
})
export class Pagos implements OnInit {
  // --- Estado de la Vista ---
  viewMode: 'generar' | 'historial' | 'adelantos' | 'bonificaciones' | 'descuentos' = 'generar';
  usuarioSeleccionadoId: number | null = null;
  mesSeleccionado: string = 'ABRIL';
  anioSeleccionado: number = 2026;
  busqueda: string = '';
  cargandoHistorial = false;
  modalVisible = false;

  // --- Listas de Datos ---
  listaAdministrativos: AdministrativoResponse[] = [];
  listaPagos: PagoResponse[] = [];
  pagoGenerado: PagoResponse | null = null;

  // --- Datos de Planilla (Sincronizados) ---
  adelantosPendientes: AdelantoResponse[] = [];
  totalAdelantos: number = 0;
  sueldoBase: number = 0;
  montoTardanzas: number = 0;
  netoProyectado: number = 0;
  sueldoNetoPlanilla: number = 0;

  // --- Deducciones Manuales ---
  deduccionesManuales: DescuentoRequest[] = [];
  mostrarFormDeduccion = false;
  nuevaDeduccion: DescuentoRequest = { tipoDeduccionId: 1, monto: null as any, observaciones: '' };
  tiposDeduccion = [
    { id: 2, nombre: 'ONP o AFP' },
    { id: 3, nombre: 'ESSALUD' },
    { id: 4, nombre: 'Descuento por Hijos' },
    { id: 5, nombre: 'Otros Descuentos / Dirección' }
  ];
  // --- Bonificaciones Manuales ---
  bonificacionesManuales: { nombre: string, monto: number }[] = [];
  mostrarFormBonificacion = false;
  nuevaBonificacion = { nombre: '', monto: null as any };

  get totalBonificaciones(): number {
    return this.bonificacionesManuales.reduce((sum, item) => sum + Number(item.monto), 0);
  }

  agregarBonificacion(): void {
    if (this.nuevaBonificacion.monto > 0 && this.nuevaBonificacion.nombre.trim() !== '') {
      this.bonificacionesManuales.push({ 
        nombre: this.nuevaBonificacion.nombre, 
        monto: Number(this.nuevaBonificacion.monto) 
      });
      this.nuevaBonificacion = { nombre: '', monto: null as any };
      this.mostrarFormBonificacion = false;
      this.recalcularNeto();
    }
  }

  eliminarBonificacion(index: number): void {
    this.bonificacionesManuales.splice(index, 1);
    this.recalcularNeto();
  }

  get totalDeducciones(): number {
    return this.deduccionesManuales.reduce((sum, item) => {
      const nombreTipo = this.getNombreDeduccion(item.tipoDeduccionId).toLowerCase();
      // Regla: ESSALUD / Seguro no se descuenta del neto del administrativo
      if (nombreTipo.includes('essalud') || nombreTipo.includes('seguro')) {
        return sum; 
      }
      return sum + Number(item.monto);
    }, 0);
  }

  getNombreDeduccion(id: number): string {
    return this.tiposDeduccion.find(t => t.id == Number(id))?.nombre || 'Descuento';
  }

  recalcularNeto(): void {
    this.netoProyectado = this.sueldoNetoPlanilla - this.totalAdelantos - this.totalDeducciones + this.totalBonificaciones;
  }

  agregarDeduccion(): void {
    if (this.nuevaDeduccion.monto > 0) {
      const ded = { ...this.nuevaDeduccion, tipoDeduccionId: Number(this.nuevaDeduccion.tipoDeduccionId), monto: Number(this.nuevaDeduccion.monto) };
      this.deduccionesManuales.push(ded);
      this.nuevaDeduccion = { tipoDeduccionId: 1, monto: null as any, observaciones: '' };
      this.mostrarFormDeduccion = false;
      this.recalcularNeto();
    }
  }

  eliminarDeduccion(index: number): void {
    this.deduccionesManuales.splice(index, 1);
    this.recalcularNeto();
  }

  // --- Configuración ---
  aniosDisponibles = [2026, 2027,2028,2029];
  mesesDelAnio = [
    { nombre: 'Enero', valor: 'ENERO', num: '01' }, { nombre: 'Febrero', valor: 'FEBRERO', num: '02' },
    { nombre: 'Marzo', valor: 'MARZO', num: '03' }, { nombre: 'Abril', valor: 'ABRIL', num: '04' },
    { nombre: 'Mayo', valor: 'MAYO', num: '05' }, { nombre: 'Junio', valor: 'JUNIO', num: '06' },
    { nombre: 'Julio', valor: 'JULIO', num: '07' }, { nombre: 'Agosto', valor: 'AGOSTO', num: '08' },
    { nombre: 'Septiembre', valor: 'SEPTIEMBRE', num: '09' }, { nombre: 'Octubre', valor: 'OCTUBRE', num: '10' },
    { nombre: 'Noviembre', valor: 'NOVIEMBRE', num: '11' }, { nombre: 'Diciembre', valor: 'DICIEMBRE', num: '12' }
  ];

  constructor(
    private pagosService: PagosService,
    private administrativoService: AdministrativoService,
    private planillaService: PlanillaAdministrativoService,
    private adelantoService: AdelantoService, // <--- Inyectar aquí
    private cdr: ChangeDetectorRef 
  ) {}

  ngOnInit(): void {
    this.cargarListaPersonal();
    this.cargarHistorial();
  }

  // --- Navegación ---
  cambiarTab(modo: 'generar' | 'historial' | 'adelantos' | 'bonificaciones' | 'descuentos'): void {
    this.viewMode = modo;
    if (modo === 'historial') this.cargarHistorial();
  }

  // --- Carga de Datos ---
  cargarListaPersonal(): void {
    this.administrativoService.listarConContrato().subscribe(data => {
      this.listaAdministrativos = data;
      if (data.length > 0 && !this.usuarioSeleccionadoId) {
        this.usuarioSeleccionadoId = data[0].id;
      }
      this.obtenerDatosPlanilla();
    });
  }

  /**
   * RECIBE datos del componente hijo Adelanto.
   * Centraliza la actualización de la planilla cuando algo cambia en los vales.
   */
  onCambioAdelantos(event: { lista: AdelantoResponse[], total: number }): void {
    this.adelantosPendientes = event.lista;
    this.totalAdelantos = event.total;
    this.obtenerDatosPlanilla();
  }

 obtenerDatosPlanilla(): void {
  if (!this.usuarioSeleccionadoId) return;

  const mesNum = this.obtenerNumeroMes();
  const inicio = `${this.anioSeleccionado}-${mesNum}-01`;
  const ultimoDia = new Date(this.anioSeleccionado, parseInt(mesNum), 0).getDate();
  const fin = `${this.anioSeleccionado}-${mesNum}-${ultimoDia}`;

  // --- NUEVO: Cargar adelantos directamente desde el padre ---
  this.adelantoService.listarPendientesPorUsuario(this.usuarioSeleccionadoId).subscribe(data => {
    const mesBusqueda = parseInt(mesNum);
    
    // Filtramos igual que en el hijo para tener consistencia
    this.adelantosPendientes = data.filter(a => {
      if (!a.fechaCreacion) return false;
      const partes = a.fechaCreacion.split('-');
      return parseInt(partes[0]) === this.anioSeleccionado && parseInt(partes[1]) === mesBusqueda;
    });

    this.totalAdelantos = this.adelantosPendientes.reduce((sum, item) => sum + item.monto, 0);

    // Ahora pedimos el cálculo de la planilla una vez que ya sabemos los adelantos
    this.planillaService.calcularPlanilla(this.usuarioSeleccionadoId!, inicio, fin).subscribe({
      next: (dataPlanilla) => {
        this.sueldoBase = dataPlanilla.sueldoBase;
        this.montoTardanzas = dataPlanilla.descuentoFaltas + dataPlanilla.descuentoTardanza;
        this.sueldoNetoPlanilla = dataPlanilla.sueldoNeto;
        this.recalcularNeto();
        this.cdr.detectChanges();
      }
    });
  });
}

  // --- Acciones de Pago ---
  onGenerarPago(): void {
    const admin = this.listaAdministrativos.find(a => a.id === this.usuarioSeleccionadoId);
    if (!admin?.contratoId) return alert("El usuario no tiene un contrato activo.");

    const payload = {
      fecha: `${this.anioSeleccionado}-${this.obtenerNumeroMes()}-${new Date(this.anioSeleccionado, parseInt(this.obtenerNumeroMes()), 0).getDate()}`, 
      contratoId: admin.contratoId,
      adelantoIds: this.adelantosPendientes.map(a => a.id),
      deducciones: this.deduccionesManuales, 
      bonificaciones: this.bonificacionesManuales
    };

    this.pagosService.crear(payload).subscribe(response => {
      this.pagoGenerado = response;
      this.modalVisible = true;
      this.cargarHistorial();
    });
  }

  // --- Historial ---
  cargarHistorial(): void {
    this.cargandoHistorial = true;
    const inicio = `${this.anioSeleccionado}-01-01`;
    const fin = `${this.anioSeleccionado}-12-31`;

    this.pagosService.listarPorRango(inicio, fin).subscribe({
      next: (data) => {
        this.listaPagos = data.sort((a, b) => b.pagoId - a.pagoId);
        this.cargandoHistorial = false;
        this.cdr.detectChanges();
      },
      error: () => this.cargandoHistorial = false
    });
  }

  get pagosFiltrados(): PagoResponse[] {
    const term = this.busqueda.toLowerCase().trim();
    return term 
      ? this.listaPagos.filter(p => p.nombreCompleto.toLowerCase().includes(term) || p.dni.includes(term))
      : this.listaPagos;
  }

  verDetalleHistorial(pago: PagoResponse): void {
    this.pagoGenerado = pago;
    this.modalVisible = true;
  }

  private obtenerNumeroMes(): string {
    return this.mesesDelAnio.find(m => m.valor === this.mesSeleccionado)?.num || '01';
  }

  alCambiarFiltro() {
    this.totalAdelantos = 0;
    this.adelantosPendientes = [];
    this.deduccionesManuales = []; // Limpiar deducciones manuales también
    this.bonificacionesManuales = []; // Limpiar bonificaciones
    this.obtenerDatosPlanilla();
  }
}