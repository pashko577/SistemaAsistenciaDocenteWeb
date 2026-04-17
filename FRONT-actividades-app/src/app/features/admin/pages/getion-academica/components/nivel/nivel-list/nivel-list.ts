import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NivelResponse } from '../../../../../../../core/models/Docente/clase/nivel-response';
import { NivelService } from '../../../../../../../core/services/Clase/nivel_services';
import { NivelFormModalComponent } from '../nivel-form-modal/nivel-form-modal';


@Component({
  selector: 'app-nivel-list',
  standalone: true,
  imports: [CommonModule, NivelFormModalComponent],
  templateUrl: './nivel-list.html'
})
export class NivelListComponent implements OnInit {
  niveles = signal<NivelResponse[]>([]);
  showModal = signal(false);
  selectedNivel = signal<NivelResponse | undefined>(undefined);

  constructor(private nivelService: NivelService) { }

  ngOnInit() {
    this.cargarNiveles();
  }

  cargarNiveles() {
    this.nivelService.listar().subscribe({
      next: (data) => this.niveles.set(data),
      error: (err) => console.error('Error al cargar niveles:', err)
    });
  }

  abrirModal(nivel?: NivelResponse) {
    this.selectedNivel.set(nivel);
    this.showModal.set(true);
  }

  cerrarModal() {
    this.showModal.set(false);
    this.selectedNivel.set(undefined);
  }

  eliminarNivel(id: number) {
    if (confirm('¿Está seguro de eliminar este nivel? Se podrían ver afectados los grados y secciones asociados.')) {
      this.nivelService.eliminar(id).subscribe({
        next: () => this.cargarNiveles(),
        error: (err) => console.error('Error al eliminar nivel:', err)
      });
    }
  }
}