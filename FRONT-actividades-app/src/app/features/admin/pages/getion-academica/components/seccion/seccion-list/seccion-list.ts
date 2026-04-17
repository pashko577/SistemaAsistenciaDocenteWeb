import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SeccionResponse } from '../../../../../../../core/models/Docente/clase/seccion-response';
import { SeccionService } from '../../../../../../../core/services/Clase/secciones_services';
import { SeccionFormModalComponent } from '../seccion-form-modal/seccion-form-modal';


@Component({
  selector: 'app-seccion-list',
  standalone: true,
  imports: [CommonModule, SeccionFormModalComponent],
  templateUrl: './seccion-list.html'
})
export class SeccionListComponent implements OnInit {
  secciones = signal<SeccionResponse[]>([]);
  showModal = signal(false);
  selectedSeccion = signal<SeccionResponse | undefined>(undefined);

  constructor(private seccionService: SeccionService) { }

  ngOnInit() {
    this.cargarSecciones();
  }

  cargarSecciones() {
    this.seccionService.listar().subscribe({
      next: (data) => this.secciones.set(data),
      error: (err) => console.error('Error al cargar secciones', err)
    });
  }

  abrirModal(seccion?: SeccionResponse) {
    this.selectedSeccion.set(seccion);
    this.showModal.set(true);
  }

  cerrarModal() {
    this.showModal.set(false);
    this.selectedSeccion.set(undefined);
  }

  eliminarSeccion(id: number) {
    if (confirm('¿Está seguro de eliminar esta sección?')) {
      this.seccionService.eliminar(id).subscribe({
        next: () => this.cargarSecciones(),
        error: (err) => console.error('Error al eliminar sección:', err)
      });
    }
  }
}