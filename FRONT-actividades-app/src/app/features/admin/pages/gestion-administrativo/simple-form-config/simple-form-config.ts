// simple-form-config.component.ts
import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

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
    <div class="p-6 card rounded-2xl shadow-xl">
      <!-- Header -->
      <div class="flex items-center gap-3 mb-5">
        <div class="w-10 h-10 bg-gradient-to-br from-blue-500 to-blue-600 rounded-xl flex items-center justify-center shadow-md">
          <mat-icon class="text-white text-xl">add_circle</mat-icon>
        </div>
        <h2 class="text-xl font-bold text-primary">
          Registrar {{ data.titulo }}
        </h2>
      </div>

      <!-- Formulario -->
      <div [formGroup]="form" class="space-y-5">
        <!-- Campo Nombre -->
        <div class="flex flex-col">
          <label class="text-xs font-bold text-secondary uppercase mb-1.5 flex items-center gap-1">
            <mat-icon class="text-blue-500 dark:text-blue-400 text-sm" style="font-size: 14px; width: 14px; height: 14px;">badge</mat-icon>
            Nombre del {{ data.titulo }}
          </label>
          <input
            formControlName="nombre"
            type="text"
            [placeholder]="'Ej: ' + data.placeholder"
            class="w-full px-4 py-3 bg-secondary border border-light rounded-xl focus:ring-2 focus:ring-blue-500 outline-none transition-all uppercase text-primary placeholder:text-muted"
          >
          <div *ngIf="form.get('nombre')?.touched && form.get('nombre')?.invalid" 
               class="flex items-center gap-1 mt-1.5 text-red-500 dark:text-red-400">
            <mat-icon class="text-xs" style="font-size: 14px; width: 14px; height: 14px;">error_outline</mat-icon>
            <span class="text-xs">El nombre es obligatorio (mín. 3 caracteres)</span>
          </div>
        </div>

        <!-- Campo Tipo de Planilla (condicional) -->
        <div *ngIf="data.mostrarPlanilla" class="flex flex-col">
          <label class="text-xs font-bold text-secondary uppercase mb-1.5 flex items-center gap-1">
            <mat-icon class="text-blue-500 dark:text-blue-400 text-sm" style="font-size: 14px; width: 14px; height: 14px;">description</mat-icon>
            Tipo de Planilla
          </label>
          <select
            formControlName="tipoPlanilla"
            class="w-full px-4 py-3 bg-secondary border border-light rounded-xl focus:ring-2 focus:ring-blue-500 outline-none font-medium text-primary"
          >
            <option [ngValue]="null" disabled class="text-muted">-- Seleccione Tipo --</option>
            <option value="ADMINISTRATIVO" class="text-primary">📋 ADMINISTRATIVO</option>
            <option value="DOCENTE" class="text-primary">📚 DOCENTE</option>
          </select>
          <div *ngIf="form.get('tipoPlanilla')?.touched && form.get('tipoPlanilla')?.invalid" 
               class="flex items-center gap-1 mt-1.5 text-red-500 dark:text-red-400">
            <mat-icon class="text-xs" style="font-size: 14px; width: 14px; height: 14px;">error_outline</mat-icon>
            <span class="text-xs">Debe seleccionar un tipo de planilla</span>
          </div>
        </div>

        <!-- Info adicional (si existe) -->
        <div *ngIf="data.mostrarPlanilla" 
             class="p-3 bg-blue-50 dark:bg-blue-900/20 rounded-lg border border-blue-200 dark:border-blue-800">
          <p class="text-xs text-blue-700 dark:text-blue-300 flex items-center gap-1">
            <mat-icon class="text-sm" style="font-size: 14px; width: 14px; height: 14px;">info</mat-icon>
            La planilla determinará el tipo de contrato y beneficios
          </p>
        </div>
      </div>

      <!-- Botones de acción -->
      <div class="mt-7 flex gap-3">
        <button 
          type="button"
          (click)="dialogRef.close()" 
          class="flex-1 px-4 py-3 border-2 border-light text-secondary hover:bg-secondary rounded-xl font-semibold transition-all flex items-center justify-center gap-2">
          <mat-icon class="text-sm" style="font-size: 16px; width: 16px; height: 16px;">close</mat-icon>
          Cancelar
        </button>
        <button
          type="button"
          (click)="guardar()"
          [disabled]="form.invalid"
          class="flex-1 px-4 py-3 bg-gradient-to-r from-blue-600 to-blue-700 hover:from-blue-700 hover:to-blue-800 text-white rounded-xl font-bold shadow-lg disabled:opacity-50 disabled:cursor-not-allowed transition-all flex items-center justify-center gap-2">
          <mat-icon class="text-sm" style="font-size: 16px; width: 16px; height: 16px;">save</mat-icon>
          Guardar
        </button>
      </div>
    </div>
  `,
  styles: [`
    :host {
      display: block;
    }
    
    /* Animación de entrada */
    @keyframes slideIn {
      from {
        opacity: 0;
        transform: translateY(-20px);
      }
      to {
        opacity: 1;
        transform: translateY(0);
      }
    }
    
    .card {
      animation: slideIn 0.3s ease-out;
    }
    
    /* Estilos para el select */
    select option {
      background-color: var(--bg-primary);
      color: var(--text-primary);
      padding: 8px;
    }
    
    /* Mejora visual para campos inválidos */
    input.ng-invalid.ng-touched,
    select.ng-invalid.ng-touched {
      border-color: #ef4444 !important;
    }
    
    input.ng-invalid.ng-touched:focus,
    select.ng-invalid.ng-touched:focus {
      ring-color: #ef4444 !important;
    }
  `]
})
export class SimpleFormConfig {
  form: FormGroup;

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