import { Component, Input, Output, EventEmitter, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { NivelResponse } from '../../../../../../../core/models/Docente/clase/nivel-response';
import { GradoService } from '../../../../../../../core/services/Clase/grado_services';
import { NivelService } from '../../../../../../../core/services/Clase/nivel_services';

@Component({
  selector: 'app-grado-form-modal',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './grado-form-modal.html'
})
export class GradoFormModalComponent implements OnInit {
  @Input() grado?: any;
  @Output() onClose = new EventEmitter<void>();
  @Output() onSave = new EventEmitter<void>();

  niveles = signal<NivelResponse[]>([]);
  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    private gradoService: GradoService,
    private nivelService: NivelService
  ) {
    // CORRECCIÓN: Nombres de campos según tu Diagrama ER
    this.form = this.fb.group({
      numGrado: ['', [Validators.required]],
      nivelId: ['', [Validators.required]]
    });
  }

  ngOnInit() {
    // 1. Cargar niveles para el selector del modal
    this.nivelService.listar().subscribe(data => this.niveles.set(data));

    // 2. Si es edición, parchear los valores asegurando el mapeo correcto
    if (this.grado) {
      this.form.patchValue({
        numGrado: this.grado.numGrado,
        nivelId: this.grado.nivelId
      });
    }
  }

  guardar() {
    if (this.form.invalid) return;

    const data = this.form.value;
    const obs = this.grado
      ? this.gradoService.actualizar(this.grado.id, data)
      : this.gradoService.crear(data);

    obs.subscribe({
      next: () => {
        this.onSave.emit();
        this.onClose.emit();
      },
      error: (err) => console.error('Error al procesar grado:', err)
    });
  }
}