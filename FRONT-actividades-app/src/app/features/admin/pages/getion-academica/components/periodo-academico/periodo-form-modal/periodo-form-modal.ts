import { Component, EventEmitter, Input, OnInit, Output, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { PeriodoAcademicoService } from '../../../../../../../core/services/Clase/periodo_academico_services';
import { PeriodoAcademicoResponse } from '../../../../../../../core/models/Docente/PeriodoAcademico/periodo-academico-response';


@Component({
  selector: 'app-periodo-form-modal',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './periodo-form-modal.html'
})
export class PeriodoFormModalComponent implements OnInit {
  private fb = inject(FormBuilder);
  private periodoService = inject(PeriodoAcademicoService);

  @Input() periodo?: PeriodoAcademicoResponse;
  @Output() onClose = new EventEmitter<void>();
  @Output() onSave = new EventEmitter<void>();

  form: FormGroup;

  constructor() {
    this.form = this.fb.group({
      nombre: ['', [Validators.required]],
      fechaInicio: ['', [Validators.required]],
      fechaFin: ['', [Validators.required]]
    });
  }

  ngOnInit() {
    if (this.periodo) {
      this.form.patchValue(this.periodo);
    }
  }

  guardar() {
    if (this.form.invalid) return;

    const request = this.form.value;
    const operacion = this.periodo
      ? this.periodoService.actualizar(this.periodo.id, request)
      : this.periodoService.crear(request);

    operacion.subscribe({
      next: () => {
        this.onSave.emit();
        this.onClose.emit();
      },
      error: (err) => alert(err.error?.message || "Error al procesar")
    });
  }
}