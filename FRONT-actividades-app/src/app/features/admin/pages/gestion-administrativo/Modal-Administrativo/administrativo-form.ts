import { Component, Inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { AdministrativoService } from '../../../../../core/services/administrativo_services';



@Component({
    selector: 'app-administrativo-form',
    standalone: true,
    imports: [
        CommonModule,
        ReactiveFormsModule,
        MatDialogModule,
        MatIconModule,
        MatButtonModule
    ],
    templateUrl: './administrativo-form.html'
})
export class AdministrativoFormComponent implements OnInit {
    adminForm: FormGroup;

    constructor(
        private fb: FormBuilder,
        private adminService: AdministrativoService,
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
    // 1. Verificar si estamos editando
    if (this.data.adminSelected) {
        // Llenamos el formulario con los datos recibidos
        this.adminForm.patchValue(this.data.adminSelected);
        
        // El DNI y Password suelen ser especiales en edición
        this.adminForm.get('dni')?.disable(); // El DNI no debería cambiarse
        this.adminForm.get('password')?.clearValidators(); // Password opcional al editar
        this.adminForm.get('password')?.updateValueAndValidity();
    }
}

  guardar() {
    if (this.adminForm.invalid) {
        this.adminForm.markAllAsTouched();
        return;
    }

    // ✅ USA getRawValue() para incluir el DNI deshabilitado
    const formValues = this.adminForm.getRawValue();

    const payload = {
        ...formValues,
        celular: String(formValues.celular),
        sedeId: Number(formValues.sedeId),
        tipoDocumentoId: Number(formValues.tipoDocumentoId),
        cargoAdministrativoId: Number(formValues.cargoAdministrativoId)
    };

    if (this.data.adminSelected) {
        this.adminService.actualizar(this.data.adminSelected.id, payload).subscribe({
            next: (response) => this.dialogRef.close(response),
            error: (err) => console.error(err)
        });
    } else {
        // MODO REGISTRAR (Lo que ya tenías)
        this.adminService.registrar(payload).subscribe({
            next: (response) => this.dialogRef.close(response),
            error: (err) => alert('Error al registrar')
        });
    }
    }

    
    cerrar() {
        this.dialogRef.close();
    }
    
}