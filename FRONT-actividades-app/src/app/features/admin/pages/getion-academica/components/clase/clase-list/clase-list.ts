import { Component, OnInit, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClaseFormModalComponent } from '../clase-form-modal/clase-form-modal';
import { ClaseService } from '../../../../../../../core/services/Clase/clase_services';
import { ClaseResponse } from '../../../../../../../core/models/Docente/clase/clase-response';


@Component({
  selector: 'app-clase-list',
  standalone: true,
  imports: [CommonModule, ClaseFormModalComponent],
  templateUrl: './clase-list.html'
})
export class ClaseListComponent implements OnInit {
  private claseService = inject(ClaseService);

  clases = signal<ClaseResponse[]>([]);
  mostrarModal = signal<boolean>(false);
  claseSeleccionada = signal<ClaseResponse | undefined>(undefined);

  ngOnInit() {
    this.cargarClases();
  }

  cargarClases() {
    this.claseService.listar().subscribe({
      next: (data) => this.clases.set(data),
      error: (err) => console.error("Error al obtener clases", err)
    });
  }

  abrirModal(clase?: ClaseResponse) {
    this.claseSeleccionada.set(clase);
    this.mostrarModal.set(true);
  }

  cerrarModal() {
    this.mostrarModal.set(false);
    this.claseSeleccionada.set(undefined);
  }

  eliminar(id: number) {
    if (confirm('¿Está seguro de eliminar esta programación de clase?')) {
      this.claseService.eliminar(id).subscribe(() => this.cargarClases());
    }
  }
}