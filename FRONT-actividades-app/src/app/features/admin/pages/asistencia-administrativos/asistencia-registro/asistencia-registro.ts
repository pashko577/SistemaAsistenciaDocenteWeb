import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms'; // Añadido ReactiveFormsModule
import { debounceTime, forkJoin, map, startWith } from 'rxjs';
import { AsistenciaAdministrativoService } from '../../../../../core/services/asistencia-administrativos';
import { CronogramaAdministrativoService } from '../../../../../core/services/cronograma-administrativo';
import { TipoAsistencia } from '../../../../../core/models/enums/tipo-asistencia-enum';
import { MatIcon } from "@angular/material/icon";
import { AdministrativoService } from '../../../../../core/services/administrativo_services';

@Component({
  selector: 'app-asistencia-registro',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, MatIcon], // Añadido ReactiveFormsModule
  templateUrl: './asistencia-registro.html'
})
export class AsistenciaRegistro implements OnInit {

  adminControl = new FormControl(''); 
  administrativosFiltrados: any[] = []; 
  
  mesSeleccionado: number = new Date().getMonth() + 1;
  anioSeleccionado: number = new Date().getFullYear();
  adminIdSeleccionado: number | null = null;
  
  administrativos: any[] = [];
  asistenciasView: any[] = [];
  cargando = false;
  mostrarResultados = false; // Control para la visibilidad del dropdown

  public TipoAsistencia = TipoAsistencia;
  listaTipos = Object.values(TipoAsistencia);
  meses = [
    { v: 1, n: 'Enero' }, { v: 2, n: 'Febrero' }, { v: 3, n: 'Marzo' }, { v: 4, n: 'Abril' },
    { v: 5, n: 'Mayo' }, { v: 6, n: 'Junio' }, { v: 7, n: 'Julio' }, { v: 8, n: 'Agosto' },
    { v: 9, n: 'Septiembre' }, { v: 10, n: 'Octubre' }, { v: 11, n: 'Noviembre' }, { v: 12, n: 'Diciembre' }
  ];

  constructor(
    private asistenciaService: AsistenciaAdministrativoService,
    private cronogramaService: CronogramaAdministrativoService,
    private adminService: AdministrativoService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit() {
    this.cargarAdministrativos();
    this.setupBuscador();
  }

  cargarAdministrativos() {
    this.adminService.listar().subscribe(res => {
      this.administrativos = res;
      // Forzar una primera actualización del filtro al cargar los datos
      this.adminControl.updateValueAndValidity();
    });
  }

  setupBuscador() {
    this.adminControl.valueChanges.pipe(
      startWith(''),
      debounceTime(300),
      map(value => {
        // Si el valor es un string y no el nombre completo del seleccionado, filtramos
        const nombre = typeof value === 'string' ? value : '';
        return this._filtrar(nombre);
      })
    ).subscribe(filtrados => {
      this.administrativosFiltrados = filtrados;
      this.mostrarResultados = true;
      this.cdr.detectChanges();
    });
  }

  private _filtrar(value: string): any[] {
    if (!value || value.length < 2) return [];
    const filterValue = value.toLowerCase();
    return this.administrativos.filter(admin => 
      `${admin.nombres} ${admin.apellidos}`.toLowerCase().includes(filterValue)
    );
  }

  seleccionarAdmin(admin: any) {
    this.adminIdSeleccionado = admin.id;
    this.mostrarResultados = false; // Cerramos la lista al seleccionar
    
    // Seteamos el valor sin disparar de nuevo el buscador infinito
    this.adminControl.setValue(`${admin.nombres} ${admin.apellidos}`, { emitEvent: false });
    
    this.cargarDatosMensuales();
  }

  // Limpiar buscador
  limpiarSeleccion() {
    this.adminIdSeleccionado = null;
    this.adminControl.setValue('');
    this.asistenciasView = [];
  }

  // --- Lógica de Negocio (Asistencia) ---

  marcarEditado(f: any) {
    f.editado = true;
  }

  onIngresoChange(f: any) {
    f.editado = true;
    if (f.horaIngreso && f.horaEntradaProg && f.horaEntradaProg !== '--:--') {
      const [hI, mI] = f.horaIngreso.split(':').map(Number);
      const [hP, mP] = f.horaEntradaProg.split(':').map(Number);
      const totalMinIngreso = (hI * 60) + mI;
      const totalMinProg = (hP * 60) + mP;
      const tolerancia = 5;

      if (totalMinIngreso <= (totalMinProg + tolerancia)) {
        f.tipoAsistencia = TipoAsistencia.ASISTIO;
        f.tardanza = 0;
      } else {
        f.tipoAsistencia = TipoAsistencia.TARDANZA;
        f.tardanza = totalMinIngreso - totalMinProg - tolerancia;
      }
    }
    this.cdr.detectChanges();
  }

  cargarDatosMensuales() {
    if (!this.adminIdSeleccionado) return;

    this.cargando = true;
    this.asistenciasView = [];

    const fechaInicio = `${this.anioSeleccionado}-${String(this.mesSeleccionado).padStart(2, '0')}-01`;
    const ultimoDia = new Date(this.anioSeleccionado, this.mesSeleccionado, 0).getDate();
    const fechaFin = `${this.anioSeleccionado}-${String(this.mesSeleccionado).padStart(2, '0')}-${ultimoDia}`;

    forkJoin({
      cronogramas: this.cronogramaService.listarPorAdministrativo(this.adminIdSeleccionado),
      asistencias: this.asistenciaService.listarPorPeriodo(this.adminIdSeleccionado, fechaInicio, fechaFin)
    }).subscribe({
      next: (res) => {
        this.generarFilasMes(res.cronogramas, res.asistencias, ultimoDia);
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: () => this.cargando = false
    });
  }

  private generarFilasMes(cronos: any[], asistencias: any[], dias: number) {
    const nombresDias = ['DOMINGO', 'LUNES', 'MARTES', 'MIERCOLES', 'JUEVES', 'VIERNES', 'SABADO'];
    
    for (let i = 1; i <= dias; i++) {
      const fechaActual = `${this.anioSeleccionado}-${String(this.mesSeleccionado).padStart(2, '0')}-${String(i).padStart(2, '0')}`;
      const fechaObj = new Date(fechaActual + 'T00:00:00');
      const diaSemana = nombresDias[fechaObj.getDay()];

      const cronoDelDia = cronos.find(c => c.diaSemana === diaSemana);
      const asisExistente = asistencias.find(a => a.fecha === fechaActual);

      this.asistenciasView.push({
        id: asisExistente?.id || null,
        fecha: fechaActual,
        diaNombre: diaSemana,
        numeroDia: i,
        administrativoId: this.adminIdSeleccionado,
        cronogramaId: cronoDelDia?.id || null,
        horaEntradaProg: cronoDelDia?.horaEntrada || '--:--',
        horaIngreso: asisExistente?.horaIngreso || '',
        salidaAlmuerzo: asisExistente?.salidaAlmuerzo || '',
        retornoAlmuerzo: asisExistente?.retornoAlmuerzo || '',
        horaSalida: asisExistente?.horaSalida || '',
        terno: asisExistente ? asisExistente.terno : true,
        tardanza: asisExistente?.tardanza || 0,
        tipoAsistencia: asisExistente?.tipoAsistencia || (cronoDelDia ? TipoAsistencia.FALTA : 'LIBRE'),
        editado: false,
        guardando: false
      });
    }
  }

  guardarFila(f: any) {
    if (!f.cronogramaId && f.tipoAsistencia !== 'PERMISO') {
      alert('Este día no tiene un horario programado.');
      return;
    }

    f.guardando = true;
    const dto = {
      administrativoId: f.administrativoId,
      cronogramaAdministrativoId: f.cronogramaId,
      fecha: f.fecha,
      horaIngreso: f.horaIngreso || null,
      horaSalida: f.horaSalida || null,
      salidaAlmuerzo: f.salidaAlmuerzo || null,
      retornoAlmuerzo: f.retornoAlmuerzo || null,
      terno: f.terno,
      tipoAsistencia: f.tipoAsistencia
    };

    const request = f.id
      ? this.asistenciaService.actualizar(f.id, dto)
      : this.asistenciaService.registrar(dto);

    request.subscribe({
      next: (res) => {
        f.id = res.id;
        f.tardanza = res.tardanza;
        f.tipoAsistencia = res.tipoAsistencia;
        f.editado = false;
        f.guardando = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        f.guardando = false;
        alert('Error al guardar: ' + (err.error?.message || 'Error desconocido'));
      }
    });
  }
}