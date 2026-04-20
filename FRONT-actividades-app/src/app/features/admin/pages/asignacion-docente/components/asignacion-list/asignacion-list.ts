import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { AsignacionDocenteService } from '../../../../../../core/services/asignaciondocente/asignaciondocente_services';
import { AsignacionDocenteResponse } from '../../../../../../core/models/Docente/asignacion-docente/asignacion-docente-response';
import { AsignacionFormModalComponent } from '../asignacion-form-modal/asignacion-form-modal';

@Component({
  selector: 'app-asignacion-list',
  standalone: true,
  imports: [
    CommonModule,
    MatIconModule,
    AsignacionFormModalComponent
  ],
  templateUrl: './asignacion-list.html',
  styleUrls: ['./asignacion-list.css']
})
export class AsignacionListComponent implements OnInit {
  private asignacionService = inject(AsignacionDocenteService);

  asignaciones = signal<AsignacionDocenteResponse[]>([]);
  isLoading = signal<boolean>(false);
  showModal = signal<boolean>(false);
  selectedAsignacion = signal<AsignacionDocenteResponse | undefined>(undefined);

  ngOnInit(): void {
    this.cargarAsignaciones();
  }

  cargarAsignaciones(): void {
    this.isLoading.set(true);
    this.asignacionService.listar().subscribe({
      next: (data) => {
        this.asignaciones.set(data);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Error al obtener asignaciones:', err);
        this.isLoading.set(false);
      }
    });
  }

  openCreateModal(): void {
    this.selectedAsignacion.set(undefined);
    this.showModal.set(true);
  }

  openEditModal(asignacion: AsignacionDocenteResponse): void {
    this.selectedAsignacion.set(asignacion);
    this.showModal.set(true);
  }

  closeModal(): void {
    this.showModal.set(false);
    this.selectedAsignacion.set(undefined);
  }

  deleteAsignacion(id: number): void {
    if (confirm('¿Estás seguro de que deseas eliminar esta asignación docente?')) {
      this.asignacionService.eliminar(id).subscribe({
        next: () => {
          this.cargarAsignaciones();
        },
        error: (err) => {
          console.error('Error al eliminar:', err);
          alert('Error al eliminar. Verifique logs.');
        }
      });
    }
  }
}
