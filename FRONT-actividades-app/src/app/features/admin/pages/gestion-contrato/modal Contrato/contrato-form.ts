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
import { TipoActividadFormComponent } from '../modal-tipo-actividad/tipo-actividad-form';
import { SimpleFormConfig } from '../../gestion-administrativo/simple-form-config/simple-form-config';
// Ajusta la ruta según donde tengas tus interfaces/enums
import { TipoPlanilla } from '../../../../../core/models/enums/contrato-enums';
import { TipoActividadRequest } from '../../../../../core/models/Contratos/tipo-actividad-request';

// contrato-form.component.ts

// Ajusta la ruta (../) según tu estructura de carpetas

@Component({
  selector: 'app-contrato-form',
  standalone: true,
  imports: [
    CommonModule, 
    ReactiveFormsModule, 
    MatDialogModule, 
    MatIconModule, 
    MatButtonModule,
    TipoActividadFormComponent
  ],
  templateUrl: './contrato-form.html'
})
export class ContratoFormComponent implements OnInit {
  contratoForm: FormGroup;
  tiposActividad: TipoActividadResponse[] = [];
  usuarios: any[] = [];
  tiposActividadFiltrados: TipoActividadResponse[] = [];

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


app(tipo: 'Actividad') {
  // Determinamos la sugerencia inicial
  let sugerencia = null;
  if (this.usuarioSeleccionado) {
    sugerencia = (!!(this.usuarioSeleccionado.especialidadId || this.usuarioSeleccionado.nombreEspecialidad)) 
                 ? 'DOCENTE' : 'ADMINISTRATIVO';
  }

  const dialogRefConfig = this.dialog.open(SimpleFormConfig, {
    width: '400px',
    data: { 
      titulo: 'Área / Actividad', 
      placeholder: 'Matemática, Limpieza...',
      mostrarPlanilla: true,    // <-- Activa el selector en el modal
      planillaSugerida: sugerencia 
    }
  });

  dialogRefConfig.afterClosed().subscribe(resultado => {
    // resultado ahora es un objeto: { nombre: '...', tipoPlanilla: '...' }
    if (!resultado || !resultado.nombre) return;

    const payload: any = {
      nombre: resultado.nombre.toUpperCase(),
      tipoPlanilla: resultado.tipoPlanilla,
      estado: 'ACTIVO'
    };

    this.tipoActividadService.registrar(payload).subscribe({
      next: (res) => {
        // 1. Agregar a la lista maestra
        this.tiposActividad.push(res);
        
        // 2. Refrescar el filtro visual
        this.refiltrarActividades(); 

        // 3. Selección automática inteligente:
        // Solo seleccionamos si la nueva área coincide con lo que el usuario actual necesita
        if (!this.usuarioSeleccionado || res.tipoPlanilla === sugerencia) {
          this.contratoForm.get('tipoActividadId')?.setValue(res.id);
          this.onActividadChange(); // Disparar lógica de pago
        }
        
        this.cd.detectChanges();
      },
      error: (err) => this.procesarError(err)
    });
  });
}
  private refiltrarActividades() {
    if (this.usuarioSeleccionado) {
       const esDocente = !!(this.usuarioSeleccionado.especialidadId || this.usuarioSeleccionado.nombreEspecialidad);
       const planillaSugerida = esDocente ? 'DOCENTE' : 'ADMINISTRATIVO';
       this.tiposActividadFiltrados = this.tiposActividad.filter(t => t.tipoPlanilla === planillaSugerida);
    } else {
       this.tiposActividadFiltrados = [...this.tiposActividad];
    }
  }

 cargarDatosIniciales() {
    // Usamos forkJoin para asegurarnos de tener TODO antes de intentar filtrar o editar
    forkJoin({
      actividades: this.tipoActividadService.listar(),
      admins: this.adminService.listar(),
      docentes: this.docenteService.listarDocentes(),
    }).subscribe({
      next: ({ actividades, admins, docentes }) => {
        this.tiposActividad = actividades;
        this.usuarios = [...admins, ...docentes].filter(u => u.estado === 'ACTIVO');

        // IMPORTANTE: Si estamos editando, los datos ya deben estar listos
        this.verificarEdicion();
        
        this.cd.detectChanges();
      },
      error: (err) => console.error('Error al cargar datos iniciales:', err)
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

// En seleccionarUsuario(u: any)
seleccionarUsuario(u: any) {
    this.usuarioSeleccionado = u;
    this.textoBusqueda = `${u.nombres || u.persona?.nombres} ${u.apellidos || u.persona?.apellidos}`;
    
    const idFinal = u.usuarioId || u.id; 
    this.contratoForm.get('usuarioId')?.setValue(idFinal);

    // DETERMINAR FILTRO INICIAL
    // Si tiene especialidadId, es prioritariamente DOCENTE, sino ADMINISTRATIVO
    const esDocente = !!(u.especialidadId || u.nombreEspecialidad);
    const planillaSugerida = esDocente ? 'DOCENTE' : 'ADMINISTRATIVO';

    // FILTRADO: Mostramos la planilla sugerida
    this.tiposActividadFiltrados = this.tiposActividad.filter(t => t.tipoPlanilla === planillaSugerida);

    // OPCIONAL: Si quieres que SIEMPRE aparezcan todas, simplemente comenta el filtro anterior y usa:
    // this.tiposActividadFiltrados = [...this.tiposActividad];

    this.contratoForm.get('tipoActividadId')?.enable();
    this.contratoForm.get('tipoActividadId')?.setValue(null);
    this.mostrarResultados = false;
    this.cd.detectChanges();
}

// En limpiarSeleccion()
limpiarSeleccion() {
    this.usuarioSeleccionado = null;
    this.textoBusqueda = '';
    this.contratoForm.get('usuarioId')?.setValue(null);
    this.contratoForm.get('tipoActividadId')?.setValue(null);
    this.contratoForm.get('tipoActividadId')?.disable(); // Lo bloqueamos de nuevo
    this.usuariosFiltrados = [];
    this.cd.detectChanges();
  }
private verificarEdicion() {
  if (this.data?.contratoSelected) {
    const selected = this.data.contratoSelected;
    
    // 1. Buscamos al usuario usando el usuarioId del contrato
    const usuario = this.usuarios.find(u => (u.usuarioId || u.id) === selected.usuarioId);
    
    if (usuario) {
      this.usuarioSeleccionado = usuario;
      this.textoBusqueda = `${usuario.nombres || usuario.persona?.nombres} ${usuario.apellidos || usuario.persona?.apellidos}`;
      
      // 2. FILTRO CRUCIAL: Generar la lista filtrada ANTES de parchar el formulario
      // Verificamos si es docente por sus propiedades únicas
      const esDocente = !!(usuario.especialidadId || usuario.nombreEspecialidad);
      const planillaRequerida = esDocente ? 'DOCENTE' : 'ADMINISTRATIVO';

      this.tiposActividadFiltrados = this.tiposActividad.filter(t => t.tipoPlanilla === planillaRequerida);
      
      // 3. Habilitar el control para que acepte el valor
      this.contratoForm.get('tipoActividadId')?.enable();
    }

    // 4. Parchamos los valores. 
    // IMPORTANTE: Asegúrate de que selected.tipoActividadId sea un número.
    this.contratoForm.patchValue({
      usuarioId: selected.usuarioId,
      tipoActividadId: selected.tipoActividadId, // <--- Esto ahora funcionará porque la lista ya existe
      tipoPago: selected.tipoPago,
      montoBase: selected.montoBase,
      horasJornada: selected.horasJornada,
      diasLaborablesMes: selected.diasLaborablesMes,
      estado: selected.estado
    });

    // Ejecutamos la lógica de bloqueo de pago si es administrativo
    this.onActividadChange();
    this.cd.detectChanges();
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

 // Importa el componente del modal de actividad arriba
// import { TipoActividadFormComponent } from '../tipo-actividad-form/tipo-actividad-form.component';

agregarNuevaArea() {
  const dialogRef = this.dialog.open(TipoActividadFormComponent, {
    width: '450px',
    // Puedes pasarle el tipo de planilla sugerido si ya hay un usuario seleccionado
    data: { 
      sugerirPlanilla: this.usuarioSeleccionado ? 
        (!!(this.usuarioSeleccionado.especialidadId || this.usuarioSeleccionado.nombreEspecialidad) ? 'DOCENTE' : 'ADMINISTRATIVO') 
        : null 
    }
  });

  dialogRef.afterClosed().subscribe(result => {
    if (result) {
      // 1. Volvemos a pedir las actividades al servidor
      this.tipoActividadService.listar().subscribe({
        next: (actividades) => {
          this.tiposActividad = actividades;
          
          // 2. Si hay un usuario seleccionado, volvemos a filtrar la lista
          if (this.usuarioSeleccionado) {
            const esDocente = !!(this.usuarioSeleccionado.especialidadId || this.usuarioSeleccionado.nombreEspecialidad);
            const planillaSugerida = esDocente ? 'DOCENTE' : 'ADMINISTRATIVO';
            this.tiposActividadFiltrados = this.tiposActividad.filter(t => t.tipoPlanilla === planillaSugerida);
          } else {
            this.tiposActividadFiltrados = [...this.tiposActividad];
          }
          
          this.cd.detectChanges();
        }
      });
    }
  });
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