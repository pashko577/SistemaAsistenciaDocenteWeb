import { Component, inject, Inject, OnInit, signal } from '@angular/core'; // Importar signal
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule, MatDialog } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { DocenteService } from '../../../../../core/services/docente';
import { SedeService } from '../../../../../core/services/sede-service';
import { SimpleFormConfig } from '../../gestion-administrativo/simple-form-config/simple-form-config';
import { EspecialidadDocenteService } from '../../../../../core/services/especialidad-docente';
import { EspecialidadDocenteResponse } from '../../../../../core/models/especialidad-docente-response';
import { TipoDocumentoService } from '../../../../../core/services/tipo-documento';
import { ThemeService } from '../../../../../core/services/theme_service';

@Component({
  selector: 'app-formulario-docente',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatDialogModule, MatIconModule, MatButtonModule],
  templateUrl: './docente-form.html'
})

export class DocenteForm implements OnInit {
  docenteForm: FormGroup;
  loading = false;
  backendError = signal<string | null>(null);
  successMessage = '';
  public themeService = inject(ThemeService)


  constructor(
    private fb: FormBuilder,
    private docenteService: DocenteService,
    private sedeService: SedeService, // Añadido
    private dialog: MatDialog, // Añadido
    private tipoDocumentoService: TipoDocumentoService,
    private especialidadService: EspecialidadDocenteService,
    private dialogRef: MatDialogRef<DocenteForm>,
    @Inject(MAT_DIALOG_DATA) public data: any

  ) {
    this.docenteForm = this.fb.group({
      dni: ['', [Validators.required, Validators.pattern('^[0-9]{8}$')]],
      password: ['', Validators.required],
      sedeId: [null, Validators.required],
      nombres: ['', Validators.required],
      apellidos: ['', Validators.required],
      celular: ['', [Validators.required, Validators.pattern('^[0-9]{9}$')]],
      email: ['', [Validators.required, Validators.email]],
      direccion: ['', Validators.required],
      tipoDocumentoId: [1, Validators.required],
      especialidadId: [null, Validators.required],
      observaciones: [''],
      estado: ['ACTIVO', Validators.required]
    });
  }

  ngOnInit(): void {
    if (this.data.docenteSelected) {
      // Ahora patchValue encontrará 'sedeId' y 'especialidadId' automáticamente
      this.docenteForm.patchValue(this.data.docenteSelected);

      this.docenteForm.get('dni')?.disable();
      this.docenteForm.get('password')?.clearValidators();
      this.docenteForm.get('password')?.updateValueAndValidity();
    }
  }
  guardar() {
    // Si es edición y el pass está vacío, limpiamos errores para que el form sea válido
    if (this.data.docenteSelected && !this.docenteForm.get('password')?.value) {
      this.docenteForm.get('password')?.setErrors(null);
    }

    if (this.docenteForm.invalid) {
      this.docenteForm.markAllAsTouched();
      return;
    }

    this.loading = true;
    const values = this.docenteForm.getRawValue();

    // Construimos el DTO exactamente como lo pide Java
    const payload: any = {
      dni: values.dni,
      nombres: values.nombres,
      apellidos: values.apellidos,
      celular: values.celular,
      email: values.email,
      direccion: values.direccion,
      sedeId: values.sedeId,
      especialidadId: values.especialidadId,
      tipoDocumentoId: values.tipoDocumentoId,
      estado: values.estado,
      observaciones: values.observaciones || 'Sin observaciones' // Java pide @NotBlank
    };

    // Solo incluimos el password si tiene contenido (nuevo o cambio)
    if (values.password && values.password.trim() !== '') {
      payload.password = values.password;
    } else if (!this.data.docenteSelected) {
      // Si es nuevo y no hay pass, error (Java pide @NotBlank)
      this.backendError.set("La contraseña es obligatoria para nuevos registros");
      this.loading = false;
      return;
    } else {
      // En edición, si no hay pass, mandamos un dummy o el back debe manejar el null
      // Depende de si tu API de 'actualizar' requiere el pass siempre.
      // Si el @NotBlank en Java está en el mismo DTO de actualización, tendrás problemas si no lo envías.
      payload.password = "KEEP_OLD_PASSWORD"; // O ajusta tu DTO de Java para que el pass sea opcional en PUT
    }

    const operacion = this.data.docenteSelected
      ? this.docenteService.actualizarDocente(this.data.docenteSelected.id, payload)
      : this.docenteService.registrarDocente(payload);

    operacion.subscribe({
      next: (res) => {
        // Obtenemos los valores actuales del formulario (donde sí están los IDs)
        const formValues = this.docenteForm.getRawValue();

        // Creamos el objeto final que el padre necesita para mapear nombres
        const docenteParaPadre = {
          ...res, // Lo que diga el back (id, nombres, etc.)
          sedeId: formValues.sedeId, // Aseguramos el ID para obtenerNombreSede
          especialidadId: formValues.especialidadId, // Aseguramos el ID para obtenerNombreEspecialidad
          estado: formValues.estado // Forzamos el estado del formulario
        };

        this.successMessage = '¡Operación exitosa!';
        // Enviamos el objeto completo
        setTimeout(() => this.dialogRef.close(docenteParaPadre), 1500);
      },
      error: (err) => {
        this.loading = false;
        this.backendError.set(err.error?.message || 'Error en los datos.');
      }
    });
  }

  cerrar() {
    this.dialogRef.close();
  }

  // docente-form.ts
  app(tipo: 'Sede' | 'Especialidad' | 'TipoDocumento') {
    const configs = {
      Sede: { titulo: 'Sede', placeholder: 'Ej. Sede Central', icon: 'business' },
      Especialidad: { titulo: 'Especialidad', placeholder: 'Ej. Matemática', icon: 'psychology' },
      TipoDocumento: { titulo: 'Tipo de Documento', placeholder: 'Ej. PEX', icon: 'description' }
    };

    const dialogRef = this.dialog.open(SimpleFormConfig, {
      width: '400px',
      data: configs[tipo]
    });

    dialogRef.afterClosed().subscribe(res => {
      // IMPORTANTE: Ahora 'res' es un objeto { nombre: string }, no un string simple.
      if (!res || !res.nombre) return;

      const nombreNuevo = res.nombre.toUpperCase(); // Estandarizamos a Mayúsculas

      if (tipo === 'Sede') {
        this.sedeService.crearSede({ nombreSede: nombreNuevo }).subscribe(resSede => {
          this.data.sedes.push(resSede);
          this.docenteForm.get('sedeId')?.setValue(resSede.id);
        });
      }
      else if (tipo === 'Especialidad') {
        this.especialidadService.crear({ nombreEspecialidad: nombreNuevo }).subscribe(resEsp => {
          this.data.especialidades.push(resEsp);
          this.docenteForm.get('especialidadId')?.setValue(resEsp.especialidadDocenteId);
        });
      }
      else if (tipo === 'TipoDocumento') {
        this.tipoDocumentoService.crearTipoDocumento({ nombreTD: nombreNuevo }).subscribe({
          next: (resTD) => {
            this.data.tiposDoc.push(resTD);
            this.docenteForm.get('tipoDocumentoId')?.setValue(resTD.id);
          },
          error: (err) => this.backendError.set('Error al crear el tipo de documento')
        });
      }
    });
  }
}