import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AsistenciaAdministrativoService } from '../../../../../core/services/asistencia-administrativos';
import { CronogramaAdministrativoService } from '../../../../../core/services/cronograma-administrativo';
// Asegúrate de que las rutas de importación sean las correctas en tu proyecto


@Component({
  selector: 'app-asistencia-registro',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './asistencia-registro.html',
  styleUrls: ['./asistencia-registro.css']
})
export class AsistenciaRegistro implements OnInit {
  fechaSeleccionada: string = new Date().toISOString().split('T')[0];
  asistenciasView: any[] = [];
  cargando = false;

  // Inyectamos AMBOS servicios
  constructor(
    private asistenciaService: AsistenciaAdministrativoService,
    private cronogramaService: CronogramaAdministrativoService
  ) {}

  ngOnInit() {
    this.cargarDatos();
  }

  cargarDatos() {
    this.cargando = true;
    const fechaObj = new Date(this.fechaSeleccionada + 'T00:00:00');
    const dias = ['DOMINGO', 'LUNES', 'MARTES', 'MIERCOLES', 'JUEVES', 'VIERNES', 'SABADO'];
    const diaSemana = dias[fechaObj.getDay()];

    // Llamamos al servicio de CRONOGRAMA para el día y al de ASISTENCIA para la fecha
    this.cronogramaService.listarPorDiaSemana(diaSemana).subscribe({
      next: (cronogramas) => {
        this.asistenciaService.listarAsistenciasPorFecha(this.fechaSeleccionada).subscribe({
          next: (asistenciasBD) => {
            this.cruzarDatos(cronogramas, asistenciasBD);
            this.cargando = false;
          },
          error: () => this.cargando = false
        });
      },
      error: () => this.cargando = false
    });
  }

  private cruzarDatos(cronos: any[], asistencias: any[]) {
    this.asistenciasView = cronos.map(c => {
      const asis = asistencias.find(a => a.administrativoId === c.administrativoId);

      return {
        id: asis?.id || null,
        administrativoId: c.administrativoId,
        cronogramaId: c.id,
        nombre: c.nombreAdministrativo || `Admin ID: ${c.administrativoId}`,
        horaEntradaProg: c.horaEntrada,
        horaIngreso: asis?.horaIngreso || '',
        salidaAlmuerzo: asis?.salidaAlmuerzo || '',
        retornoAlmuerzo: asis?.retornoAlmuerzo || '',
        horaSalida: asis?.horaSalida || '',
        terno: asis ? asis.terno : true,
        observaciones: asis?.observaciones || '',
        tardanza: asis?.tardanza || 0,
        editado: false
      };
    });
  }

  guardarFila(fila: any) {
    const dto = {
      administrativoId: fila.administrativoId,
      cronogramaAdministrativoId: fila.cronogramaId,
      fecha: this.fechaSeleccionada,
      horaIngreso: fila.horaIngreso || null,
      horaSalida: fila.horaSalida || null,
      salidaAlmuerzo: fila.salidaAlmuerzo || null,
      retornoAlmuerzo: fila.retornoAlmuerzo || null,
      terno: fila.terno,
      observaciones: fila.observaciones
    };

    if (fila.id) {
      this.asistenciaService.actualizar(fila.id, dto).subscribe(res => {
        fila.tardanza = res.tardanza;
        fila.editado = false;
        alert('Asistencia actualizada');
      });
    } else {
      this.asistenciaService.registrar(dto).subscribe(res => {
        fila.id = res.id;
        fila.tardanza = res.tardanza;
        fila.editado = false;
        alert('Asistencia registrada');
      });
    }
  }
}