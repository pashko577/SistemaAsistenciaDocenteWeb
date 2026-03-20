import { Component, Inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule, MatDialog } from '@angular/material/dialog';
import { MatIconModule } from "@angular/material/icon";
import { MatButtonModule } from "@angular/material/button";

import { TipoActividadResponse } from '../../../../../core/models/Contratos/tipo-actividad-response';
import { ContratoService } from '../../../../../core/services/contrato_services';
import { TipoActividadService } from '../../../../../core/services/tipo-actividad-services';
import { AdministrativoService } from '../../../../../core/services/administrativo_services';
import { Estado, TipoPago } from '../../../../../core/models/enums/contrato-enums';

import { forkJoin } from 'rxjs';
import { DocenteService } from '../../../../../core/services/docente';

@Component({
  selector: 'app-contrato-form',
  standalone: true,
  imports: [
    CommonModule, 
    ReactiveFormsModule, 
    MatDialogModule, 
    MatIconModule, 
    MatButtonModule
  ],
  templateUrl: './contrato-form.html'
})
export class ContratoFormComponent implements OnInit {
  contratoForm: FormGroup;
  tiposActividad: TipoActividadResponse[] = [];
  usuarios: any[] = [];

  // --- PROPIEDADES PARA EL BUSCADOR ---
  usuariosFiltrados: any[] = [];
  textoBusqueda: string = '';
  mostrarResultados: boolean = false;
  usuarioSeleccionado: any = null;

  constructor(
    private fb: FormBuilder,
    private contratoService: ContratoService,
    private tipoActividadService: TipoActividadService,
    private adminService: AdministrativoService,
    private docenteService: DocenteService,
    private cd: ChangeDetectorRef, // Solución para el error NG0100
    private dialog: MatDialog,
    public dialogRef: MatDialogRef<ContratoFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    this.contratoForm = this.fb.group({
      usuarioId: [null, Validators.required],
      tipoActividadId: [null, Validators.required],
      tipoPago: [TipoPago.PAGO_MENSUAL, Validators.required],
      montoBase: [0, [Validators.required, Validators.min(1)]],
      horasJornada: [8, [Validators.required, Validators.min(1), Validators.max(24)]],
      diasLaborablesMes: [30, [Validators.required, Validators.min(1), Validators.max(31)]],
      estado: [Estado.ACTIVO, Validators.required]
    });
  }

  ngOnInit(): void {
    this.cargarDatosIniciales();
  }

 cargarDatosIniciales() {
    // 1. Cargar Áreas (Tipos de Actividad)
    this.tipoActividadService.listar().subscribe({
      next: (res) => {
        this.tiposActividad = res;
        this.verificarEdicion();
        this.cd.detectChanges();
      },
      error: (err) => console.error('Error al cargar áreas:', err)
    });
    
    // 2. Cargar Personal COMBINADO y FILTRADO
    forkJoin({
      admins: this.adminService.listar(),
      docentes: this.docenteService.listarDocentes(),
    }).subscribe({
      next: ({ admins, docentes }) => {
        // Unimos ambas listas
        const todos = [...admins, ...docentes];

        // FILTRO: Solo dejamos a los que tienen estado 'ACTIVO'
        // Si en tu base de datos el string es diferente (ej. 'Activo' o '1'), cámbialo aquí.
        this.usuarios = todos.filter(u => u.estado === 'ACTIVO');
        
        console.log('Personal Activo Cargado:', this.usuarios);
        
        this.verificarEdicion();
        this.cd.detectChanges();
      },
      error: (err) => console.error('Error al cargar el personal:', err)
    });
  }

// --- LÓGICA DEL BUSCADOR ---
  filtrarUsuarios(event: any) {
    const busqueda = event.target.value.toLowerCase();
    this.textoBusqueda = busqueda;

    if (busqueda.length > 1) {
      this.usuariosFiltrados = this.usuarios.filter(u => {
        const nombreCompleto = `${u.persona?.nombres || u.nombres} ${u.persona?.apellidos || u.apellidos}`.toLowerCase();
        const dni = u.persona?.dni || u.dni || '';
        return nombreCompleto.includes(busqueda) || dni.includes(busqueda);
      });
      this.mostrarResultados = true;
    } else {
      this.usuariosFiltrados = [];
      this.mostrarResultados = false;
    }
  }

seleccionarUsuario(u: any) {
  this.usuarioSeleccionado = u;
  const nombre = `${u.persona?.nombres || u.nombres} ${u.persona?.apellidos || u.apellidos}`;
  this.textoBusqueda = nombre;
  
  this.contratoForm.get('usuarioId')?.setValue(u.usuarioId || u.id);
  
  this.mostrarResultados = false;
  this.cd.detectChanges();
}
limpiarSeleccion() {
  this.usuarioSeleccionado = null;
  this.textoBusqueda = '';
  this.contratoForm.get('usuarioId')?.setValue(null);
  this.usuariosFiltrados = [];
  this.cd.detectChanges();
}
 private verificarEdicion() {
  if (this.data?.contratoSelected) {
    this.contratoForm.patchValue(this.data.contratoSelected);
    
    const usuario = this.usuarios.find(u => (u.usuarioId || u.id) === this.data.contratoSelected.usuarioId);
    if (usuario) {
      this.usuarioSeleccionado = usuario; // <--- Importante
      this.textoBusqueda = `${usuario.persona?.nombres || usuario.nombres} ${usuario.persona?.apellidos || usuario.apellidos}`;
    }
    this.onActividadChange();
  }
}

  onActividadChange() {
    const selectedId = this.contratoForm.get('tipoActividadId')?.value;
    const actividad = this.tiposActividad.find(t => t.id == selectedId);

    if (actividad?.tipoPlanilla === 'ADMINISTRATIVO') {
      this.contratoForm.get('tipoPago')?.setValue(TipoPago.PAGO_MENSUAL);
      this.contratoForm.get('tipoPago')?.disable(); 
    } else {
      this.contratoForm.get('tipoPago')?.enable();
    }
  }

  // Métodos para los botones "+"
  agregarNuevoUsuario() {
    // Aquí abrirías el modal de registro de personal
    console.log('Abriendo modal de nuevo administrativo/docente...');
  }

  agregarNuevaArea() {
    // Aquí abrirías el modal de nueva área
    console.log('Abriendo modal de nueva área...');
  }

guardar() {
  if (this.contratoForm.invalid) return;

  // Usamos getRawValue para incluir campos deshabilitados
  const rawValues = this.contratoForm.getRawValue();

  // Construimos el DTO exacto que espera Java
  const contratoDTO = {
    tipoPago: rawValues.tipoPago, // Debe ser 'PAGO_MENSUAL' o 'PAGO_HORA'
    montoBase: Number(rawValues.montoBase),
    horasJornada: Number(rawValues.horasJornada),
    diasLaborablesMes: Number(rawValues.diasLaborablesMes),
    usuarioId: Number(rawValues.usuarioId),
    tipoActividadId: Number(rawValues.tipoActividadId),
    estado: rawValues.estado // Debe ser 'ACTIVO', 'INACTIVO', etc.
  };

  console.log('Payload depurado enviando a Java:', contratoDTO);

  if (this.data?.contratoSelected) {
    // ACTUALIZAR
    const id = this.data.contratoSelected.id;
    this.contratoService.actualizar(id, contratoDTO).subscribe({
      next: () => this.dialogRef.close(true),
      error: (err) => this.procesarError(err)
    });
  } else {
    // CREAR
    this.contratoService.registrar(contratoDTO).subscribe({
      next: () => this.dialogRef.close(true),
      error: (err) => this.procesarError(err)
    });
  }
}

private procesarError(err: any) {
  console.error('Detalle del Error 400:', err);
  
  // Intentar obtener el mensaje de la RuntimeException de Java
  let msg = "Error en la solicitud (400).";
  
  if (err.error) {
    if (typeof err.error === 'string') msg = err.error;
    else if (err.error.message) msg = err.error.message;
  }
  
  alert(msg);
}
}