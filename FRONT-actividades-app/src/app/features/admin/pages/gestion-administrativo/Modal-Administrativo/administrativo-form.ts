import { Component, Inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule, MatDialog } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

// Servicios
import { AdministrativoService } from '../../../../../core/services/administrativo_services';
import { SedeService } from '../../../../../core/services/sede-service';
import { cargoAdministrativoService } from '../../../../../core/services/cargoAdministrativo_services';
import { TipoDocumentoService } from '../../../../../core/services/tipo-documento';
import { SimpleFormConfig } from '../simple-form-config/simple-form-config';

@Component({
  selector: 'app-administrativo-form',
  standalone: true,
  imports: [
    CommonModule, ReactiveFormsModule, MatDialogModule,
    MatIconModule, MatButtonModule
  ],
  templateUrl: './administrativo-form.html'
})
export class AdministrativoFormComponent implements OnInit {
  adminForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private adminService: AdministrativoService,
    private sedeService: SedeService,
    private cargoService: cargoAdministrativoService,
    private tipoDocService: TipoDocumentoService,
    private dialog: MatDialog,
    private dialogRef: MatDialogRef<AdministrativoFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    this.adminForm = this.fb.group({
      dni: ['', [Validators.required, Validators.pattern('^[0-9]{8}$')]],
      password: ['', Validators.required],
      sedeId: [null, Validators.required],
      nombres: ['', Validators.required],
      apellidos: ['', Validators.required],
      celular: ['', [Validators.required, Validators.pattern('^[0-9]{9}$')]],
      email: ['', [Validators.required, Validators.email]],
      direccion: ['', Validators.required],
      tipoDocumentoId: [1, Validators.required],
      cargoAdministrativoId: [null, Validators.required],
      estado: ['ACTIVO', Validators.required]
    });
  }

  ngOnInit(): void {
    if (this.data.adminSelected) {
      this.adminForm.patchValue(this.data.adminSelected);
      this.adminForm.get('dni')?.disable();
      this.adminForm.get('password')?.clearValidators();
      this.adminForm.get('password')?.updateValueAndValidity();

      // Si quieres que el estado no se pueda cambiar manualmente en el formulario:
    // this.adminForm.get('estado')?.disable();
    }
  }

  /**
   * Método 'app' invocado desde el HTML para registros rápidos
   */
  app(tipo: 'Sede' | 'Cargo' | 'Documento') {
    const configs = {
      Sede: { titulo: 'Sede', placeholder: 'Sede Central', mostrarPlanilla: false },
      Cargo: { titulo: 'Cargo', placeholder: 'Administrador', mostrarPlanilla: false },
      Documento: { titulo: 'Tipo de Documento', placeholder: 'PTP', mostrarPlanilla: false }
    };

    const dialogRefConfig = this.dialog.open(SimpleFormConfig, {
      width: '400px',
      data: configs[tipo]
    });

    dialogRefConfig.afterClosed().subscribe(res => {
      // 1. Validamos que el resultado del modal exista
      if (!res || !res.nombre) return;

      // 2. Extraemos solo el texto y lo pasamos a mayúsculas
      const nombreTexto = res.nombre.toUpperCase();

      if (tipo === 'Sede') {
        // Tu sedeService parece recibir un objeto
        this.sedeService.crearSede({ nombreSede: nombreTexto }).subscribe(resSede => {
          this.data.sedes.push(resSede);
          this.adminForm.get('sedeId')?.setValue(resSede.id);
        });
      }
      else if (tipo === 'Cargo') {
        // AQUÍ: Pasamos solo el string 'nombreTexto' para que coincida con tu Service
        this.cargoService.registrarCargo(nombreTexto).subscribe(resCargo => {
          this.data.cargos.push(resCargo);
          this.adminForm.get('cargoAdministrativoId')?.setValue(resCargo.id);
        });
      }
      else if (tipo === 'Documento') {
        // Tu tipoDocService parece recibir un objeto
        this.tipoDocService.crearTipoDocumento({ nombreTD: nombreTexto }).subscribe(resTD => {
          this.data.tiposDoc.push(resTD);
          this.adminForm.get('tipoDocumentoId')?.setValue(resTD.id);
        });
      }
    });
  }
  guardar() {
  if (this.adminForm.invalid) {
    this.adminForm.markAllAsTouched();
    return;
  }

    const formValues = this.adminForm.getRawValue();
    const payload = {
      ...formValues,
      celular: String(formValues.celular),
      sedeId: Number(formValues.sedeId),
      tipoDocumentoId: Number(formValues.tipoDocumentoId),
      cargoAdministrativoId: Number(formValues.cargoAdministrativoId),
      estado: formValues.estado || 'ACTIVO'
    };

    const operacion = this.data.adminSelected
      ? this.adminService.actualizar(this.data.adminSelected.id, payload)
      : this.adminService.registrar(payload);

    operacion.subscribe({
      next: (response) => this.dialogRef.close(response),
      error: (err) => alert('Error al procesar la solicitud')
    });
  }

  cerrar() {
    this.dialogRef.close();
  }
}
