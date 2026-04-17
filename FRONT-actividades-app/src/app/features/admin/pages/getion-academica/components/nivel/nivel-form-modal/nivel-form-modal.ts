// nivel-form-modal.component.ts
import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { NivelResponse } from '../../../../../../../core/models/Docente/clase/nivel-response';
import { NivelService } from '../../../../../../../core/services/Clase/nivel_services';


@Component({
  selector: 'app-nivel-form-modal',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './nivel-form-modal.html'
})
export class NivelFormModalComponent implements OnInit {
  @Input() nivel?: NivelResponse;
  @Output() onClose = new EventEmitter<void>();
  @Output() onSave = new EventEmitter<void>();

  form: FormGroup;

  constructor(private fb: FormBuilder, private nivelService: NivelService) {
    this.form = this.fb.group({
      nomNivel: ['', [Validators.required, Validators.minLength(3)]]
    });
  }

  ngOnInit() {
    if (this.nivel) {
      this.form.patchValue({
        nomNivel: this.nivel.nomNivel
      });
    }
  }

  guardar() {
    if (this.form.invalid) return;

    const request = this.form.value;
    const accion = this.nivel
      ? this.nivelService.actualizar(this.nivel.id, request)
      : this.nivelService.crear(request);

    accion.subscribe({
      next: () => {
        this.onSave.emit();
        this.onClose.emit();
      },
      error: (err) => console.error("Error al guardar nivel", err)
    });
  }
}