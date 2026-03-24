import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { forkJoin } from 'rxjs';
import { AsistenciaAdministrativoService } from '../../../../../core/services/asistencia-administrativos';
import { CronogramaAdministrativoService } from '../../../../../core/services/cronograma-administrativo';
import { TipoAsistencia } from '../../../../../core/models/enums/tipo-asistencia-enum';

@Component({
  selector: 'app-asistencia-registro',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './asistencia-registro.html'
})
export class AsistenciaRegistro implements OnInit {
  fechaSeleccionada: string = new Date().toISOString().split('T')[0];
  asistenciasView: any[] = [];
  cargando = false;
  
  public TipoAsistencia = TipoAsistencia;
  listaTipos = Object.values(TipoAsistencia);

  constructor(
    private asistenciaService: AsistenciaAdministrativoService,
    private cronogramaService: CronogramaAdministrativoService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.cargarDatos();
  }

  // Método simple para cambios que no afectan la lógica de tiempo
  marcarEditado(f: any) {
    f.editado = true;
  }

  // Lógica de tiempo con 5 minutos de tolerancia
  onIngresoChange(f: any) {
    f.editado = true;
    
    if (f.horaIngreso && f.horaEntradaProg) {
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
        f.tardanza = totalMinIngreso - totalMinProg;
      }
    } else if (!f.horaIngreso) {
      f.tipoAsistencia = TipoAsistencia.FALTA;
      f.tardanza = 0;
    }
    this.cdr.detectChanges();
  }

  cargarDatos() {
    this.cargando = true;
    this.asistenciasView = [];
    
    const fechaObj = new Date(this.fechaSeleccionada + 'T00:00:00');
    const dias = ['DOMINGO', 'LUNES', 'MARTES', 'MIERCOLES', 'JUEVES', 'VIERNES', 'SABADO'];
    const diaSemana = dias[fechaObj.getDay()];

    forkJoin({
      cronogramas: this.cronogramaService.listarPorDiaSemana(diaSemana),
      asistencias: this.asistenciaService.listarPorFecha(this.fechaSeleccionada)
    }).subscribe({
      next: (res) => {
        this.cruzarDatos(res.cronogramas, res.asistencias);
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }

  private cruzarDatos(cronos: any[], asistencias: any[]) {
    this.asistenciasView = cronos.map(c => {
      const asis = asistencias.find(a => a.administrativoId === c.administrativoId);
      return {
        id: asis?.id || null,
        administrativoId: c.administrativoId,
        cronogramaId: c.id,
        nombre: `${c.nombres} ${c.apellidos}`, 
        horaEntradaProg: c.horaEntrada,
        horaIngreso: asis?.horaIngreso || '',
        salidaAlmuerzo: asis?.salidaAlmuerzo || '',
        retornoAlmuerzo: asis?.retornoAlmuerzo || '',
        horaSalida: asis?.horaSalida || '',
        terno: asis ? asis.terno : true,
        observaciones: asis?.observaciones || '', 
        tardanza: asis?.tardanza || 0,
        tipoAsistencia: asis?.tipoAsistencia || TipoAsistencia.FALTA,
        editado: false,
        guardando: false
      };
    });
  }

  guardarFila(f: any) {
    f.guardando = true;
    const dto = {
      administrativoId: f.administrativoId,
      cronogramaAdministrativoId: f.cronogramaId,
      fecha: this.fechaSeleccionada,
      horaIngreso: f.horaIngreso || null,
      horaSalida: f.horaSalida || null,
      salidaAlmuerzo: f.salidaAlmuerzo || null,
      retornoAlmuerzo: f.retornoAlmuerzo || null,
      terno: f.terno,
      observaciones: f.observaciones,
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
        alert('Error: ' + (err.error?.message || 'Error al guardar'));
        this.cdr.detectChanges();
      }
    });
  }
}