// src/app/features/admin/pages/gestion-academica/components/curso/curso-form-modal/curso-form-modal.ts
import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CursoService } from '../../../../../../../core/services/Clase/curso_services';

@Component({
  selector: 'app-curso-form-modal',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './curso-form-modal.html'
})
export class CursoFormModalComponent implements OnInit {
  @Input() curso?: any;
  @Output() onClose = new EventEmitter<void>();
  @Output() onSave = new EventEmitter<void>();

  form: FormGroup;

  constructor(private fb: FormBuilder, private cursoService: CursoService) {
    this.form = this.fb.group({
      nombreCurso: ['', [Validators.required, Validators.maxLength(100)]]
    });
  }

  ngOnInit() {
    if (this.curso) {
        this.form.patchValue({
            nombreCurso: this.curso.nombreCurso
        });
    }
  }

  onSubmit() {
    if (this.form.invalid) return;
    const request = this.curso
      ? this.cursoService.actualizar(this.curso.id, this.form.value)
      : this.cursoService.crear(this.form.value);

    request.subscribe({ 
        next: () => { 
            this.onSave.emit(); 
            this.onClose.emit(); 
        },
        error: (err) => console.error('Error al guardar curso:', err)
    });
  }
}