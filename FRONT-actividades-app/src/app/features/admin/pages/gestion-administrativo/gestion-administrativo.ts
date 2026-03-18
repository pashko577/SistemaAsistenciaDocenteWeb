import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { AdministrativoService } from '../../../../core/services/administrativo_services';
import { SedeResponse } from '../../../../core/models/sede-response';
import { CargoAdministrativoResponse } from '../../../../core/models/cargo-administrativo-response';
import { TipoDocumentoResponse } from '../../../../core/models/tipo-documento-response';
import { forkJoin } from 'rxjs';

// Angular Material
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog, MatDialogModule } from '@angular/material/dialog'; 
import { AdministrativoFormComponent } from './Modal-Administrativo/administrativo-form';

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
    MatDialogModule
  ],
  templateUrl: './gestion-administrativo.html'
})
export class GestionAdministrativo implements OnInit {
  listaAdmins: AdministrativoCard[] = [];
  sedes: SedeResponse[] = [];
  cargos: CargoAdministrativoResponse[] = [];
  tiposDoc: TipoDocumentoResponse[] = [];

  constructor(
    private adminService: AdministrativoService,
    private cdr: ChangeDetectorRef,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.cargarDatosIniciales();
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

        this.listaAdmins = res.admins.map((admin: any) => ({
          ...admin,
          nombreSede: admin.nombreSede || this.obtenerNombreSede(admin.sedeId),
          nombreCargo: admin.nombreCargo || this.obtenerNombreCargo(admin.cargoAdministrativoId)
        }));

        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('❌ Error al cargar datos:', err);
      }
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
    });

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
      // Actualizamos la lista local con el objeto editado
      const index = this.listaAdmins.findIndex(a => a.id === result.id);
      if (index !== -1) {
        this.listaAdmins[index] = {
          ...result,
          nombreSede: this.obtenerNombreSede(result.sedeId),
          nombreCargo: this.obtenerNombreCargo(result.cargoAdministrativoId)
        };
        this.listaAdmins = [...this.listaAdmins]; // Disparar detección de cambios
        this.cdr.detectChanges();
      }
    }
  });
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
  if (confirm('¿Estás seguro de que deseas eliminar este administrativo?')) {
    this.adminService.eliminar(id).subscribe({
      next: () => {
        // Filtramos la lista local para que desaparezca la card de inmediato
        this.listaAdmins = this.listaAdmins.filter(a => a.id !== id);
      },
      error: (err) => console.error('Error al eliminar:', err)
    });
  }
}


}

