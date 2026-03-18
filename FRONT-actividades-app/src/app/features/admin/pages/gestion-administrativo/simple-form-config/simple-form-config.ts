import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-simple-form-config',
  standalone: true,
  imports: [
    CommonModule, ReactiveFormsModule, MatDialogModule, 
    MatFormFieldModule, MatInputModule, MatButtonModule
  ],
  template: `
   <div class="p-6 bg-white rounded-2xl">
    <h2 class="text-xl font-bold text-blue-700 mb-4">Registrar {{ data.titulo }}</h2>
    
    <div [formGroup]="form" class="space-y-4">
      <div class="flex flex-col">
        <label class="text-xs font-bold text-gray-600 uppercase mb-1">Nombre del {{ data.titulo }}</label>
        <input 
          formControlName="nombre" 
          type="text" 
          [placeholder]="'Ej: ' + data.placeholder"
          class="w-full px-4 py-3 border border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 outline-none transition-all"
        >
        <span *ngIf="form.get('nombre')?.touched && form.get('nombre')?.invalid" class="text-red-500 text-xs mt-1">
          El nombre es obligatorio (mín. 3 caracteres)
        </span>
      </div>
    </div>

    <div class="mt-6 flex gap-3 justify-end">
      <button (click)="dialogRef.close()" class="px-4 py-2 text-gray-600 font-semibold hover:bg-gray-100 rounded-lg transition-all">
        Cancelar
      </button>
      <button 
        (click)="guardar()" 
        [disabled]="form.invalid"
        class="px-6 py-2 bg-blue-600 text-white font-bold rounded-lg shadow-md hover:bg-blue-700 disabled:opacity-50 transition-all">
        Guardar
      </button>
    </div>
  </div>
`
})
export class SimpleFormConfig {
  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<SimpleFormConfig>,
    @Inject(MAT_DIALOG_DATA) public data: { titulo: string, placeholder: string }
  ) {
    this.form = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(3)]]
    });
  }

  guardar() {
    if (this.form.valid) {
      this.dialogRef.close(this.form.value.nombre);
    }
  }
}