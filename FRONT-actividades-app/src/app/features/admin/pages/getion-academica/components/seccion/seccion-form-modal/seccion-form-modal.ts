// src/app/features/admin/pages/gestion-academica/components/seccion/seccion-form-modal/seccion-form-modal.ts
import { Component, Input, Output, EventEmitter, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { GradoResponse } from '../../../../../../../core/models/Docente/clase/grado-response';
import { SeccionService } from '../../../../../../../core/services/Clase/secciones_services';
import { GradoService } from '../../../../../../../core/services/Clase/grado_services';


@Component({
  selector: 'app-seccion-form-modal',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './seccion-form-modal.html'
})
export class SeccionFormModalComponent implements OnInit {
  @Input() seccion?: any;
  @Output() onClose = new EventEmitter<void>();
  @Output() onSave = new EventEmitter<void>();

  grados = signal<GradoResponse[]>([]);
  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    private seccionService: SeccionService,
    private gradoService: GradoService
  ) {
    this.form = this.fb.group({
      nomSeccion: ['', [Validators.required, Validators.maxLength(50)]],
      gradoId: ['', [Validators.required]]
    });
  }

  ngOnInit() {
    // Cargamos grados para el select
    this.gradoService.listar().subscribe(data => this.grados.set(data));

    if (this.seccion) {
      this.form.patchValue({
        nomSeccion: this.seccion.nomSeccion,
        gradoId: this.seccion.gradoId
      });
    }
  }

  onSubmit() {
    if (this.form.invalid) return;

    const request = this.seccion
      ? this.seccionService.actualizar(this.seccion.id, this.form.value)
      : this.seccionService.crear(this.form.value);

    request.subscribe({
      next: () => {
        this.onSave.emit();
        this.onClose.emit();
      },
      error: (err) => console.error('Error al procesar sección:', err)
    });
  }
}