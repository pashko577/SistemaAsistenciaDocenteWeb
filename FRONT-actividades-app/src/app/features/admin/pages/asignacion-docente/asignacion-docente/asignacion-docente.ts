import { Component } from '@angular/core';
import { AsignacionListComponent } from '../components/asignacion-list/asignacion-list';


@Component({
  selector: 'app-asignacion-docente',
  standalone: true,
  imports: [AsignacionListComponent],
  templateUrl: './asignacion-docente.html',
  styleUrl: './asignacion-docente.css',
})
export class AsignacionDocente {
}