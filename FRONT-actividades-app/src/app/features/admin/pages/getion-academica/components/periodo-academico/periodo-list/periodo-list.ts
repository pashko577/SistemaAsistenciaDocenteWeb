import { Component, OnInit, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PeriodoAcademicoService } from '../../../../../../../core/services/Clase/periodo_academico_services';
import { PeriodoAcademicoResponse } from '../../../../../../../core/models/Docente/PeriodoAcademico/periodo-academico-response';
import { PeriodoFormModalComponent } from '../periodo-form-modal/periodo-form-modal';


@Component({
  selector: 'app-periodo-list',
  standalone: true,
  imports: [CommonModule, PeriodoFormModalComponent],
  templateUrl: './periodo-list.html'
})
export class PeriodoListComponent implements OnInit {
  private periodoService = inject(PeriodoAcademicoService);

  periodos = signal<PeriodoAcademicoResponse[]>([]);
  mostrarModal = signal<boolean>(false);
  periodoSeleccionado = signal<PeriodoAcademicoResponse | undefined>(undefined);

  ngOnInit() {
    this.cargarPeriodos();
  }

  cargarPeriodos() {
    this.periodoService.listar().subscribe({
      next: (data) => this.periodos.set(data),
      error: (err) => console.error("Error al cargar periodos", err)
    });
  }

  abrirModal(periodo?: PeriodoAcademicoResponse) {
    this.periodoSeleccionado.set(periodo);
    this.mostrarModal.set(true);
  }

  cerrarModal() {
    this.mostrarModal.set(false);
    this.periodoSeleccionado.set(undefined);
  }

  eliminar(id: number) {
    if (confirm('¿Desea eliminar este periodo? Esto podría afectar a las clases programadas.')) {
      this.periodoService.eliminar(id).subscribe(() => this.cargarPeriodos());
    }
  }
}