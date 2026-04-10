import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RolesService } from '../../../../../core/services/roles_services';
import { ModulosService } from '../../../../../core/services/modulo_services';
import { RolModuloService } from '../../../../../core/services/rol_modulo_services';
import { Rol } from '../../../../../core/models/RolModule/rol';
import { ModuloResponse } from '../../../../../core/models/RolModule/modulo-response';


@Component({
  selector: 'app-permisos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './permisos.html'
})
export class PermisosComponent implements OnInit {
  private rolesService = inject(RolesService);
  private modulosService = inject(ModulosService);
  private rolModuloService = inject(RolModuloService);

  roles: Rol[] = [];
  modulos: ModuloResponse[] = [];
  rolSeleccionadoId?: number;

  // Mapa para rastrear qué módulos están activos para el rol seleccionado
  permisosActivos: { [key: number]: boolean } = {};

  ngOnInit() {
    // Carga inicial de datos maestros
    this.rolesService.obtenerTodos().subscribe(data => this.roles = data);
    this.modulosService.listar().subscribe(data => this.modulos = data);
  }

onRolChange() {
  if (!this.rolSeleccionadoId) return;

  this.permisosActivos = {};

  // Asegurémonos de que los módulos existan antes de marcar los activos
  this.modulosService.listar().subscribe(modulos => {
    this.modulos = modulos; // <--- Refrescar la lista de módulos

    this.rolModuloService.listarPorRol(this.rolSeleccionadoId!).subscribe(asignaciones => {
      asignaciones.forEach(a => {
        this.permisosActivos[a.moduloId] = true;
      });
    });
  });
}

 togglePermiso(moduloId: number) {
  if (!this.rolSeleccionadoId) return;

  const estaActivo = this.permisosActivos[moduloId];

  if (!estaActivo) {
    // Activar permiso
    this.rolModuloService.asignarModulo({
      rolId: this.rolSeleccionadoId,
      moduloId
    }).subscribe(() => {
      this.permisosActivos[moduloId] = true;
    });
  } else {
    // Desactivar permiso (Llamada al endpoint DELETE)
    this.rolModuloService.desasignarModulo(this.rolSeleccionadoId, moduloId).subscribe({
      next: () => {
        this.permisosActivos[moduloId] = false;
      },
      error: (err) => console.error('Error al quitar permiso', err)
    });
  }
}
}
