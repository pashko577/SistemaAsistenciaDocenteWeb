import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CursoResponse } from '../../../../../../../core/models/Docente/clase/curso-response';
import { CursoService } from '../../../../../../../core/services/Clase/curso_services';
import { CursoFormModalComponent } from '../curso-form-modal/curso-form-modal';


@Component({
  selector: 'app-curso-list',
  standalone: true,
  imports: [CommonModule, CursoFormModalComponent],
  templateUrl: './curso-list.html'
})
export class CursoListComponent implements OnInit {
  cursos = signal<CursoResponse[]>([]);
  showModal = signal(false);
  selectedCurso = signal<CursoResponse | undefined>(undefined);

  constructor(private cursoService: CursoService) { }

  ngOnInit() {
    this.cargarCursos();
  }

  cargarCursos() {
    this.cursoService.listar().subscribe({
      next: (data) => this.cursos.set(data),
      error: (err) => console.error('Error al cargar cursos', err)
    });
  }

  abrirModal(curso?: CursoResponse) {
    this.selectedCurso.set(curso);
    this.showModal.set(true);
  }

  cerrarModal() {
    this.showModal.set(false);
    this.selectedCurso.set(undefined);
  }

  eliminarCurso(id: number) {
    if (confirm('¿Está seguro de eliminar este curso?')) {
      this.cursoService.eliminar(id).subscribe({
        next: () => this.cargarCursos(),
        error: (err) => console.error('Error al eliminar curso:', err)
      });
    }
  }
}