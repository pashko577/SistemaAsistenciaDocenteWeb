import { Component, Inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { TipoActividadService } from '../../../../../core/services/tipo-actividad-services';

@Component({
  selector: 'app-tipo-actividad-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatDialogModule],
  templateUrl: './tipo-actividad-form.html'
})
export class TipoActividadFormComponent implements OnInit {
  actividadForm: FormGroup;
  isEdit: boolean = false;

  constructor(
    private fb: FormBuilder,
    private actividadService: TipoActividadService,
    public dialogRef: MatDialogRef<TipoActividadFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    this.actividadForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(3)]],
      descripcion: [''],
      tipoPlanilla: ['ADMINISTRATIVO', Validators.required], // 'ADMINISTRATIVO' o 'DOCENTE'
      estado: ['ACTIVO', Validators.required]
    });
  }

  ngOnInit(): void {
    if (this.data?.actividad) {
      this.isEdit = true;
      this.actividadForm.patchValue(this.data.actividad);
    }
  }

  guardar() {
    if (this.actividadForm.invalid) return;

    const dto = this.actividadForm.value;

    if (this.isEdit) {
      this.actividadService.actualizar(this.data.actividad.id, dto).subscribe({
        next: () => this.dialogRef.close(true),
        error: (err) => alert("Error al actualizar: " + err.message)
      });
    } else {
      this.actividadService.registrar(dto).subscribe({
        next: () => this.dialogRef.close(true),
        error: (err) => alert("Error al registrar: " + err.message)
      });
    }
  }
}