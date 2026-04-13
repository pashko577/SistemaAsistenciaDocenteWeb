// features/user/shared/layout/user-layout/user-layout.ts
import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { MenuItem } from '../../menu-config';
import { UserHeader } from '../../components/user-header/user-header';
import { RolModuloService } from '../../../../../core/services/rol_modulo_services';
import { Auth } from '../../../../../core/services/auth';

@Component({
  selector: 'app-user-layout',
  standalone: true,
  imports: [CommonModule, UserHeader, RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './user-layout.html',
  styleUrl: './user-layout.css',
})
export class UserLayoutComponent implements OnInit {
  private rolModuloService = inject(RolModuloService);
  public auth = inject(Auth); 

  menuItems = signal<MenuItem[]>([]);

  ngOnInit() {
    this.cargarMenu();
  }

private cargarMenu() {
  const rolId = this.auth.getRolId();
  if (!rolId) return;

  // 1. Obtenemos las rutas legales que se guardaron en el Login
  const rutasLegales = JSON.parse(localStorage.getItem('rutas_permitidas') || '[]');

  this.rolModuloService.listarPorRol(rolId).subscribe({
    next: (asignaciones) => {
      // 2. FILTRO CRÍTICO: Solo permitimos módulos cuya ruta esté en nuestra lista blanca
      const menuDinamico: MenuItem[] = asignaciones
        .filter(a => rutasLegales.includes(a.modulo.ruta)) // <--- ESTO SOLUCIONA EL PROBLEMA
        .map(a => ({
          id: a.modulo.id,
          titulo: a.modulo.nombre,
          ruta: a.modulo.ruta,
          icono: a.modulo.icono || 'bi-circle'
        }));
      
      this.menuItems.set([...menuDinamico]);
      
      // Actualizamos el storage para mantener la consistencia
      localStorage.setItem('modulos_usuario', JSON.stringify(menuDinamico));
    }
  });
}
}