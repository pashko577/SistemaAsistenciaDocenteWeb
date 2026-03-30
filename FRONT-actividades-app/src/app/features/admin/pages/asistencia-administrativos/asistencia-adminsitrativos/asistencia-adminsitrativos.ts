import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AsistenciaRegistro } from '../asistencia-registro/asistencia-registro';
import { CronogramaList } from '../cronograma-list/cronograma-list';
import { MatIcon } from "@angular/material/icon";


@Component({
  selector: 'app-asistencia-administrativos-main',
  standalone: true,
  imports: [CommonModule, AsistenciaRegistro, CronogramaList, MatIcon],
  templateUrl: './asistencia-adminsitrativos.html'
})
export class AsistenciaAdministrativosMain {
  tab: 'registro' | 'horarios' = 'registro';
}