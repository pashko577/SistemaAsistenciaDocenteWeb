import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { forkJoin, finalize, of } from 'rxjs';
import { RolesService } from '../../../../../core/services/roles_services';
import { ModuloService } from '../../../../../core/services/modulo_services';
import { RolModuloService } from '../../../../../core/services/rol_modulo_services';
import { Auth } from '../../../../../core/services/auth'; 
import { Rol } from '../../../../../core/models/RolModule/rol';
import { ModuloResponse } from '../../../../../core/models/RolModule/modulo-response';
import { RolModuloResponse } from '../../../../../core/models/RolModule/rolmodulo-response';

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
  private auth = inject(Auth);

  roles: Rol[] = [];
  modulos: ModuloResponse[] = [];
  rolSeleccionadoId?: number;

  permisosActivos = signal<{ [key: number]: boolean }>({});
  isLoading = signal<boolean>(false);
  esAdmin = signal<boolean>(false);

  ngOnInit() {
    this.esAdmin.set(this.auth.getRole() === 'ADMIN');
    this.cargarDatosIniciales();
  }

  private cargarDatosIniciales() {
    this.isLoading.set(true);

    // PASO 1: Cargamos siempre todos los módulos para tener la metadata (Rutas, Descripciones)
    forkJoin({
      roles: this.esAdmin() ? this.rolesService.obtenerTodos() : of([] as Rol[]),
      todosLosModulos: this.modulosService.listar()
    })
    .subscribe({
      next: (res) => {
        this.roles = res.roles;
        const totalModulos = res.todosLosModulos;

        if (this.esAdmin()) {
          // Si es Admin, mostramos la lista completa
          this.modulos = totalModulos;
          this.isLoading.set(false);
        } else {
          // Si es Administrativo, cruzamos sus permisos con la lista total
          const myRolId = this.auth.getRolId();
          if (myRolId) {
            this.rolSeleccionadoId = myRolId;
            this.cargarPermisosYFiltrar(myRolId, totalModulos);
          } else {
            this.isLoading.set(false);
          }
        }
      },
      error: () => this.isLoading.set(false)
    });
  }

  private cargarPermisosYFiltrar(rolId: number, todosLosModulos: ModuloResponse[]) {
    this.rolModuloService.listarPorRol(rolId)
      .pipe(finalize(() => this.isLoading.set(false)))
      .subscribe(asignaciones => {
        // Obtenemos los IDs de los módulos que tiene permitidos
        const idsPermitidos = asignaciones.map(a => a.modulo.id);
        
        // Filtramos la lista total para que solo vea los suyos, pero con TODA la info (ruta, desc)
        this.modulos = todosLosModulos.filter(m => idsPermitidos.includes(m.id));

        // Actualizamos el estado de los switches
        const mapa: { [key: number]: boolean } = {};
        idsPermitidos.forEach(id => mapa[id] = true);
        this.permisosActivos.set(mapa);
      });
  }

  onRolChange() {
    if (!this.esAdmin() || !this.rolSeleccionadoId) return;

    this.isLoading.set(true);
    this.rolModuloService.listarPorRol(Number(this.rolSeleccionadoId))
      .pipe(finalize(() => this.isLoading.set(false)))
      .subscribe(asignaciones => {
        const mapa: { [key: number]: boolean } = {};
        asignaciones.forEach(a => mapa[a.modulo.id] = true);
        this.permisosActivos.set(mapa);
      });
  }

  togglePermiso(moduloId: number) {
  if (!this.esAdmin() || !this.rolSeleccionadoId) return;

  const estaActivo = !!this.permisosActivos()[moduloId];
  const rolId = Number(this.rolSeleccionadoId);

  if (estaActivo) {
    // Caso: Eliminar permiso
    this.rolModuloService.desasignar(rolId, moduloId).subscribe({
      next: () => this.actualizarEstadoLocal(moduloId, false),
      error: (err) => console.error('Error al desasignar:', err)
      
    });
  } else {
    // Caso: Asignar permiso
    this.rolModuloService.asignar({ rolId, moduloId }).subscribe({
      next: () => this.actualizarEstadoLocal(moduloId, true),
      error: (err) => console.error('Error al asignar:', err)
    });
  }
}

// Método auxiliar para mantener el código DRY (Don't Repeat Yourself)
private actualizarEstadoLocal(moduloId: number, activado: boolean) {
  this.permisosActivos.update(prev => {
    const nuevo = { ...prev };
    if (!activado) {
      delete nuevo[moduloId];
    } else {
      nuevo[moduloId] = true;
    }
    return nuevo;
  });
}
}