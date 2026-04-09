import { Component, OnInit, ChangeDetectorRef, ViewChild, HostBinding } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { AdministrativoService } from '../../../../core/services/administrativo_services';
import { SedeResponse } from '../../../../core/models/sede-response';
import { CargoAdministrativoResponse } from '../../../../core/models/Administrativos/cargo-administrativo-response';
import { TipoDocumentoResponse } from '../../../../core/models/tipo-documento-response';
import { forkJoin } from 'rxjs';

// Angular Material
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { AdministrativoFormComponent } from './Modal-Administrativo/administrativo-form';
import { SearchAdministrativo } from "./search-adminsitrativo/search-adminsitrativo";
import { ThemeService } from '../../../../core/services/theme_service';

export interface AdministrativoCard {
  id: number;
  dni: string;
  nombres: string;
  apellidos: string;
  celular: number | string;
  email: string;
  direccion: string;
  sedeId: number;
  nombreSede: string;
  cargoAdministrativoId: number;
  nombreCargo: string;
  estado: string;
}

@Component({
  selector: 'app-gestion-administrativo',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatIconModule,
    MatButtonModule,
    MatDialogModule,
    SearchAdministrativo
  ],
  templateUrl: './gestion-administrativo.html'
})
export class GestionAdministrativo implements OnInit {

  @HostBinding('class.dark') get isDarkMode() {
    return this.themeService.isDarkMode();
  }


  @ViewChild(SearchAdministrativo) buscadorComponente!: SearchAdministrativo;
  listaAdmins: AdministrativoCard[] = [];
  listaFiltrada: AdministrativoCard[] = [];
  buscadorHijo: AdministrativoCard[] = []
  sedes: SedeResponse[] = [];
  cargos: CargoAdministrativoResponse[] = [];
  tiposDoc: TipoDocumentoResponse[] = [];

  constructor(
    private adminService: AdministrativoService,
     public themeService: ThemeService,
    private cdr: ChangeDetectorRef,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void {
    this.cargarDatosIniciales();


  }



manejarFiltros(filtros: any) {
  const { busqueda, sedeId, cargoId, estado } = filtros;

  this.listaFiltrada = this.listaAdmins.filter(admin => {
    const term = busqueda?.toLowerCase().trim() || '';
    const cumpleBusqueda = !term ||
      admin.nombres.toLowerCase().includes(term) ||
      admin.apellidos.toLowerCase().includes(term) ||
      admin.dni.includes(term);

    const cumpleSede = !sedeId || Number(admin.sedeId) === Number(sedeId);
    const cumpleCargo = !cargoId || Number(admin.cargoAdministrativoId) === Number(cargoId);

    // NUEVA LÓGICA DE ESTADO:
    // 1. Si el usuario eligió un estado específico (ej. 'INACTIVO'), se muestra.
    // 2. Si el selector está vacío (!estado), mostramos todo MENOS los inactivos.
    const cumpleEstado = estado
      ? admin.estado === estado
      : admin.estado !== 'INACTIVO';

    return cumpleBusqueda && cumpleSede && cumpleCargo && cumpleEstado;
  });

  this.cdr.detectChanges();
}
  cargarDatosIniciales() {
  forkJoin({
    sedes: this.adminService.listarSedes(),
    cargos: this.adminService.listarCargos(),
    tipos: this.adminService.listarTiposDocumento(),
    admins: this.adminService.listar()
  }).subscribe({
    next: (res) => {
      this.sedes = res.sedes;
      this.cargos = res.cargos;
      this.tiposDoc = res.tipos;

      // Guardamos la base de datos completa (incluyendo INACTIVOS)
      this.listaAdmins = res.admins.map((admin: any) => ({
        ...admin,
        nombreSede: admin.nombreSede || this.obtenerNombreSede(admin.sedeId),
        nombreCargo: admin.nombreCargo || this.obtenerNombreCargo(admin.cargoAdministrativoId)
      }));

      // FILTRO INICIAL: Solo mostrar los que NO están INACTIVOS al entrar
      this.listaFiltrada = this.listaAdmins.filter(a => a.estado !== 'INACTIVO');

      this.cdr.detectChanges();
    },
    error: (err) => console.error('❌ Error:', err)
  });
}

  abrirModal() {
    const dialogRef = this.dialog.open(AdministrativoFormComponent, {
      width: '100%',
      maxWidth: '650px',
      data: {
        sedes: this.sedes,
        cargos: this.cargos,
        tiposDoc: this.tiposDoc
      },
      disableClose: true


    }

    );


    dialogRef.afterClosed().subscribe(result => {
      // result contiene la respuesta del servidor enviada desde el modal
      if (result) {
        const nuevoAdmin: AdministrativoCard = {
          ...result,
          nombreSede: this.obtenerNombreSede(result.sedeId),
          nombreCargo: this.obtenerNombreCargo(result.cargoAdministrativoId)
        };

        // Agregamos al inicio de la lista
        this.listaAdmins = [nuevoAdmin, ...this.listaAdmins];
        this.listaFiltrada = [...this.listaAdmins]; // Actualizar vista
        this.cdr.detectChanges();
      }
    });

  }
  // Añade esta función para abrir el modal en modo edición
  editarAdmin(admin: AdministrativoCard) {
    const dialogRef = this.dialog.open(AdministrativoFormComponent, {
      width: '100%',
      maxWidth: '650px',
      data: {
        sedes: this.sedes,
        cargos: this.cargos,
        tiposDoc: this.tiposDoc,
        adminSelected: admin // <--- Pasamos el administrativo a editar
      },
      disableClose: true
    });

   dialogRef.afterClosed().subscribe(result => {
    if (result) {
      // 1. Buscamos el índice en la lista maestra
      const index = this.listaAdmins.findIndex(a => a.id === result.id);

      if (index !== -1) {
        // 2. Reemplazamos el objeto con los nuevos datos mapeados
        this.listaAdmins[index] = {
          ...result,
          nombreSede: this.obtenerNombreSede(result.sedeId),
          nombreCargo: this.obtenerNombreCargo(result.cargoAdministrativoId)
        };

        // 3. Sincronizamos la lista filtrada para que el cambio se vea YA
        // IMPORTANTE: Volvemos a aplicar los filtros actuales para que si
        // cambiaste el estado a INACTIVO, la card desaparezca solita.
        if (this.buscadorComponente) {
           this.manejarFiltros(this.buscadorComponente.filtroForm.value);
        } else {
           this.listaFiltrada = [...this.listaAdmins];
        }

        this.cdr.detectChanges();
      }
    }
  });

  }
// Método para limpiar desde el botón "Ver todos" o "Limpiar" del padre
limpiarFiltros() {
  if (this.buscadorComponente) {
    this.buscadorComponente.limpiar(); // Esto debe poner el estado en 'ACTIVO' o null
  }

  // En lugar de copiar toda la listaAdmins, filtramos los inactivos
  this.listaFiltrada = this.listaAdmins.filter(admin => admin.estado !== 'INACTIVO');

  this.cdr.detectChanges();
}

  // --- MÉTODOS DE APOYO ---

  obtenerNombreCargo(cargoId: number): string {
    if (!cargoId) return 'No especificado';
    const cargo = this.cargos.find(c => Number(c.id) === Number(cargoId));
    return cargo ? cargo.nombreCargo : 'Cargo ID: ' + cargoId;
  }

  obtenerNombreSede(sedeId: number): string {
    if (!sedeId) return 'No especificada';
    const sede = this.sedes.find(s => Number(s.id) === Number(sedeId));
    return sede ? sede.nombreSede : 'Sede ID: ' + sedeId;
  }

  obtenerIniciales(nombres: string, apellidos: string): string {
    if (!nombres || !apellidos) return '??';
    return (nombres.charAt(0) + apellidos.charAt(0)).toUpperCase();
  }

  getEstadoIcon(estado: string): string {
    const icons: { [key: string]: string } = {
      'ACTIVO': 'check_circle',
      'NUEVO': 'fiber_new',
      'INACTIVO': 'cancel',
      'SUSPENDIDO': 'warning'
    };
    return icons[estado] || 'help';
  }

eliminarAdmin(id: number) {
  if (confirm('¿Estás seguro de que deseas inactivar a este administrativo?')) {
    this.adminService.eliminar(id).subscribe({
      next: () => {
        // Buscamos al administrativo en la lista maestra
        const index = this.listaAdmins.findIndex(a => a.id === id);

        if (index !== -1) {
          // Cambiamos el estado localmente
          this.listaAdmins[index].estado = 'INACTIVO';

          // Forzamos al filtro a ejecutarse de nuevo
          // Esto hará que la card desaparezca de la vista si el filtro dice "Activos"
          if (this.buscadorComponente) {
            this.manejarFiltros(this.buscadorComponente.filtroForm.value);
          } else {
            this.listaFiltrada = this.listaAdmins.filter(a => a.estado !== 'INACTIVO');
          }

          this.cdr.detectChanges();
        }
      },
      error: (err) => console.error('Error:', err)
    });
  }
}

}

