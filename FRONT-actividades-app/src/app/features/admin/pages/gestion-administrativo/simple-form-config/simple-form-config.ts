// simple-form-config.component.ts
import { Component, inject, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { ThemeService } from '../../../../../core/services/theme_service';
import { OverlayContainer } from '@angular/cdk/overlay'; // <--- Importante para modales

@Component({
  selector: 'app-simple-form-config',
  standalone: true,
  imports: [
    CommonModule, 
    ReactiveFormsModule, 
    MatDialogModule,
    MatFormFieldModule, 
    MatInputModule, 
    MatButtonModule,
    MatIconModule
  ],
 template: `
    <div class="p-6 rounded-2xl shadow-xl bg-white dark:bg-gray-800 border border-gray-100 dark:border-gray-700 transition-colors duration-300">
      
      <div class="flex items-center gap-3 mb-6">
        <div class="w-10 h-10 bg-gradient-to-br from-blue-500 to-indigo-600 rounded-xl flex items-center justify-center shadow-lg shadow-blue-500/20">
          <mat-icon class="text-white">add_circle</mat-icon>
        </div>
        <div>
           <h2 class="text-xl font-extrabold text-gray-900 dark:text-white tracking-tight">
             Registrar {{ data.titulo }}
           </h2>
           <p class="text-[10px] text-gray-500 dark:text-gray-400 uppercase font-bold tracking-widest">Configuración de sistema</p>
        </div>
      </div>

      <div [formGroup]="form" class="space-y-6">
        
        <div class="flex flex-col">
          <label class="text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase mb-2 flex items-center gap-1.5 ml-1">
            <mat-icon class="text-blue-500 text-sm" style="font-size: 14px; width: 14px; height: 14px;">label</mat-icon>
            Nombre del {{ data.titulo }}
          </label>
          <input
            formControlName="nombre"
            type="text"
            [placeholder]="'Ej: ' + data.placeholder"
            class="w-full px-4 py-3 bg-gray-50 dark:bg-gray-900/50 border border-gray-200 dark:border-gray-700 rounded-xl focus:ring-2 focus:ring-blue-500 outline-none transition-all uppercase text-gray-800 dark:text-gray-100 placeholder:text-gray-400 dark:placeholder:text-gray-600 font-medium"
          >
          <div *ngIf="isFieldInvalid('nombre')" class="flex items-center gap-1 mt-2 text-red-500 ml-1">
            <mat-icon style="font-size: 14px; width: 14px; height: 14px;">error</mat-icon>
            <span class="text-[11px] font-bold">{{ getErrorMessage('nombre') }}</span>
          </div>
        </div>

        <div *ngIf="data.mostrarPlanilla" class="flex flex-col">
          <label class="text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase mb-2 flex items-center gap-1.5 ml-1">
            <mat-icon class="text-blue-500 text-sm" style="font-size: 14px; width: 14px; height: 14px;">assignment</mat-icon>
            Tipo de Planilla
          </label>
          <select
            formControlName="tipoPlanilla"
            class="w-full px-4 py-3 bg-gray-50 dark:bg-gray-900/50 border border-gray-200 dark:border-gray-700 rounded-xl focus:ring-2 focus:ring-blue-500 outline-none text-gray-800 dark:text-gray-100 font-medium"
          >
            <option [ngValue]="null" disabled>-- Seleccione Tipo --</option>
            <option value="ADMINISTRATIVO">ADMINISTRATIVO</option>
            <option value="DOCENTE">DOCENTE</option>
          </select>
        </div>
      </div>

      <div class="mt-8 flex gap-3">
        <button (click)="dialogRef.close()" 
          class="flex-1 px-4 py-3 border border-gray-200 dark:border-gray-700 text-gray-600 dark:text-gray-400 hover:bg-gray-50 dark:hover:bg-gray-700/50 rounded-xl font-bold transition-all text-xs uppercase tracking-wider">
          Cancelar
        </button>
        <button (click)="guardar()" [disabled]="form.invalid"
          class="flex-1 px-4 py-3 bg-gradient-to-r from-blue-600 to-indigo-600 hover:from-blue-700 hover:to-indigo-700 text-white rounded-xl font-bold shadow-lg shadow-blue-500/20 disabled:opacity-40 disabled:grayscale transition-all text-xs uppercase tracking-wider">
          Guardar Registro
        </button>
      </div>
    </div>
  `,
  styles: [`
    :host { display: block; background: transparent; }
    /* Estilo para que el select no se vea blanco puro en dark */
    select option { background: white; color: black; }
    .dark select option { background: #1f2937; color: white; }
  `]
})
export class SimpleFormConfig {
  form: FormGroup;
    public themeService = inject(ThemeService);


  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<SimpleFormConfig>,
    @Inject(MAT_DIALOG_DATA) public data: {
      titulo: string,
      placeholder: string,
      mostrarPlanilla?: boolean,
      planillaSugerida?: string
    }
  ) {
    // Crear formulario base
    this.form = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(3)]]
    });

    // Agregar control de planilla si es necesario
    if (this.data.mostrarPlanilla) {
      this.form.addControl(
        'tipoPlanilla',
        this.fb.control(this.data.planillaSugerida || null, Validators.required)
      );
    }
  }

  guardar() {
    if (this.form.valid) {
      // Marcar todos los campos como touched para mostrar errores
      Object.keys(this.form.controls).forEach(key => {
        this.form.get(key)?.markAsTouched();
      });
      
      // Devolver el objeto completo
      this.dialogRef.close(this.form.value);
    } else {
      // Mostrar errores si el formulario es inválido
      Object.keys(this.form.controls).forEach(key => {
        this.form.get(key)?.markAsTouched();
      });
    }
  }
  
  // Método helper para verificar si un campo es inválido
  isFieldInvalid(fieldName: string): boolean {
    const field = this.form.get(fieldName);
    return field ? field.invalid && field.touched : false;
  }
  
  // Método helper para obtener mensaje de error
  getErrorMessage(fieldName: string): string {
    const field = this.form.get(fieldName);
    if (!field) return '';
    
    if (field.hasError('required')) {
      return 'Este campo es obligatorio';
    }
    if (field.hasError('minlength')) {
      return `Mínimo ${field.errors?.['minlength'].requiredLength} caracteres`;
    }
    return '';
  }
}