import { Component, EventEmitter, OnInit, Output, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { HorarioBloqueService } from '../../../../../../core/services/docente/horario_bloque_services';
import { HorarioBloqueResponse } from '../../../../../../core/models/Docente/horario-bloque/horario-bloque-response';

@Component({
  selector: 'app-horario-bloque-list',
  standalone: true,
  imports: [CommonModule, MatIconModule],
  templateUrl: './horario-bloque-list.html'
})
export class HorarioBloqueList implements OnInit {
  private horarioService = inject(HorarioBloqueService);

  @Output() editar = new EventEmitter<number>();
  @Output() nuevo = new EventEmitter<void>();

  bloques = signal<HorarioBloqueResponse[]>([]);
  isLoading = signal<boolean>(false);

  ngOnInit(): void {
    this.cargarBloques();
  }

  cargarBloques(): void {
    this.isLoading.set(true);
    this.horarioService.listar().subscribe({
      next: (data) => {
        this.bloques.set(data);
        this.isLoading.set(false);
      },
      error: (err: unknown) => {
        console.error('Error al cargar bloques', err);
        this.isLoading.set(false);
      }
    });
  }

  eliminar(id: number): void {
    if (confirm('¿Estás seguro de eliminar este bloque?')) {
      this.horarioService.eliminar(id).subscribe({
        next: () => this.cargarBloques(),
        error: (err: unknown) => console.error('Error al eliminar', err)
      });
    }
  }
}
