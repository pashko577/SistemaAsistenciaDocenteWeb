import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CronogramaDocenteForm } from '../components/cronograma-docente-form/cronograma-docente-form';
import { CronogramaDocenteList } from '../components/cronograma-docente-list/cronograma-docente-list';
import { HorarioBloqueForm } from '../components/horario-bloque-form/horario-bloque-form';
import { HorarioBloqueList } from '../components/horario-bloque-list/horario-bloque-list';

@Component({
  selector: 'app-asistencia-docente',
  standalone: true,
  imports: [
    CommonModule,
    CronogramaDocenteForm,
    CronogramaDocenteList,
    HorarioBloqueForm,
    HorarioBloqueList
  ],
  templateUrl: './asistencia-docente.html',
  styleUrl: './asistencia-docente.css',
})
export class AsistenciaDocente {
  pestanaActiva = signal<string>('lista');
  bloqueEditId = signal<number | null>(null);
  mostrarFormBloque = signal<boolean>(false);

  cambiarPestana(nombre: string) {
    this.pestanaActiva.set(nombre);
    this.mostrarFormBloque.set(false);
    this.bloqueEditId.set(null);
  }

  // id=null => nuevo bloque, id=número => editar
  iniciarEdicionBloque(id: number | null) {
    this.bloqueEditId.set(id);
    this.mostrarFormBloque.set(true);
  }

  finalizarEdicionBloque() {
    this.mostrarFormBloque.set(false);
    this.bloqueEditId.set(null);
  }

  onCronogramaGuardado() {
    // Al usar @if con signals, al volver a 'lista' el componente se recrea
    // ejecutando ngOnInit automáticamente — sin necesidad de ViewChild ni setTimeout
    this.cambiarPestana('lista');
  }
}