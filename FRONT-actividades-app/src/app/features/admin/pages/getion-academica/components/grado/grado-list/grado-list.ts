import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GradoFormModalComponent } from '../grado-form-modal/grado-form-modal';
import { GradoResponse } from '../../../../../../../core/models/Docente/clase/grado-response';
import { GradoService } from '../../../../../../../core/services/Clase/grado_services';

@Component({
  selector: 'app-grado-list',
  standalone: true,
  imports: [CommonModule, GradoFormModalComponent],
  templateUrl: './grado-list.html'
})
export class GradoListComponent implements OnInit {
  grados = signal<GradoResponse[]>([]);
  showModal = signal(false);
  selectedGrado = signal<GradoResponse | undefined>(undefined);

  constructor(private gradoService: GradoService) { }

  ngOnInit() {
    this.cargarGrados();
  }

  cargarGrados() {
    this.gradoService.listar().subscribe({
      next: (data) => this.grados.set(data),
      error: (err) => console.error('Error al cargar grados', err)
    });
  }

  abrirModal(grado?: GradoResponse) {
    this.selectedGrado.set(grado);
    this.showModal.set(true);
  }

  cerrarModal() {
    this.showModal.set(false);
    this.selectedGrado.set(undefined);
  }

  eliminarGrado(id: number) {
    if (confirm('¿Está seguro de eliminar este grado?')) {
      this.gradoService.eliminar(id).subscribe({
        next: () => this.cargarGrados(),
        error: (err) => console.error('Error al eliminar grado:', err)
      });
    }
  }
}