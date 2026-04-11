import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { forkJoin, finalize } from 'rxjs'; // Importamos forkJoin
import { RolesService } from '../../../../../core/services/roles_services';
import { ModuloService } from '../../../../../core/services/modulo_services';
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
  private modulosService = inject(ModuloService);
  private rolModuloService = inject(RolModuloService);

  roles: Rol[] = [];
  modulos: ModuloResponse[] = [];
  rolSeleccionadoId?: number;

  // Signals para reactividad inmediata
  permisosActivos = signal<{ [key: number]: boolean }>({});
  isLoading = signal<boolean>(false);

ngOnInit() {
  this.isLoading.set(true);

  // Ejecutamos ambas peticiones en paralelo
  forkJoin({
    roles: this.rolesService.obtenerTodos(),
    modulos: this.modulosService.listar()
  })
  .pipe(finalize(() => this.isLoading.set(false)))
  .subscribe({
    next: (res) => {
      this.roles = res.roles;
      this.modulos = res.modulos;
    },
    error: (err) => console.error('Error al cargar datos:', err)
  });
}

  onRolChange() {
    if (!this.rolSeleccionadoId) {
      this.permisosActivos.set({});
      return;
    }

    this.isLoading.set(true);

    this.rolModuloService.listarPorRol(Number(this.rolSeleccionadoId))
      .pipe(finalize(() => this.isLoading.set(false)))
      .subscribe({
        next: (asignaciones) => {
          const mapa: { [key: number]: boolean } = {};
          asignaciones.forEach(a => mapa[a.moduloId] = true);
          this.permisosActivos.set(mapa);
        }
      });
  }

  togglePermiso(moduloId: number) {
    if (!this.rolSeleccionadoId) return;

    const estaActivo = !!this.permisosActivos()[moduloId];

    if (!estaActivo) {
      this.rolModuloService.asignar({
        rolId: Number(this.rolSeleccionadoId),
        moduloId
      }).subscribe(() => {
        // Actualización reactiva del Signal
        this.permisosActivos.update(prev => ({ ...prev, [moduloId]: true }));
      });
    } else {
      this.rolModuloService.desasignar(Number(this.rolSeleccionadoId), moduloId).subscribe({
        next: () => {
          this.permisosActivos.update(prev => {
            const copia = { ...prev };
            delete copia[moduloId];
            return copia;
          });
        }
      });
    }
  }
}
