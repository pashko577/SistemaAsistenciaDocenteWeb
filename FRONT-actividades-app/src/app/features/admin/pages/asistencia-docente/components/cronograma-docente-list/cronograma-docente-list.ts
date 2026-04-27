import { Component, EventEmitter, OnInit, Output, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { CronogramaDocenteService } from '../../../../../../core/services/docente/cronograma_docente_services';
import { CronogramaDocenteResponse } from '../../../../../../core/models/Docente/cronograma/cronograma-docente-response';

@Component({
  selector: 'app-cronograma-docente-list',
  standalone: true,
  imports: [CommonModule, MatIconModule],
  templateUrl: './cronograma-docente-list.html'
})
export class CronogramaDocenteList implements OnInit {
  private cronogramaService = inject(CronogramaDocenteService);

  cronogramas = signal<CronogramaDocenteResponse[]>([]);
  isLoading = signal<boolean>(false);

  ngOnInit(): void {
    this.cargarCronogramas();
  }

  cargarCronogramas(): void {
    this.isLoading.set(true);
    this.cronogramaService.listarCronogramas().subscribe({
      next: (data) => {
        this.cronogramas.set(data);
        this.isLoading.set(false);
      },
      error: (err: unknown) => {
        console.error('Error al cargar cronogramas', err);
        this.isLoading.set(false);
      }
    });
  }

  eliminar(id: number): void {
    if (confirm('¿Estás seguro de eliminar este horario maestro?')) {
      this.cronogramaService.eliminar(id).subscribe({
        next: () => this.cargarCronogramas(),
        error: (err: unknown) => console.error('Error al eliminar', err)
      });
    }
  }
}