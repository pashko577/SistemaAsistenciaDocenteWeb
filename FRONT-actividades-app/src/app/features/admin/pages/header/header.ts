// header.ts
import { Component, OnInit, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Token } from '../../../../core/services/token';
import { Auth } from '../../../../core/services/auth';
import { ThemeService } from '../../../../core/services/theme_service';
import { ALL_MODULES, NavModule } from './nav-config'; 

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class Header implements OnInit {
  username = signal<string | null>('');
  roles = signal<string[]>([]);
  isMenuOpen = false;
  
  // 1. Signal para la búsqueda
  searchQuery = signal<string>('');

  private allowedPaths = signal<string[]>([]);

  // 2. Módulos filtrados por ROL, PERMISOS y ahora también por BÚSQUEDA
  readonly filteredModules = computed(() => {
    const paths = this.allowedPaths();
    const userRoles = this.roles();
    const query = this.searchQuery().toLowerCase();

    // Primero obtenemos la base según permisos
    let baseModules = userRoles.includes('ADMIN') 
      ? ALL_MODULES 
      : ALL_MODULES.filter(m => paths.includes(m.path));

    if (paths.length === 0 && !userRoles.includes('ADMIN')) {
      baseModules = ALL_MODULES.filter(m => m.path === '/dashboard');
    }

    // Luego aplicamos el filtro de búsqueda si hay texto
    if (!query) return baseModules;
    return baseModules.filter(m => 
      m.label.toLowerCase().includes(query) || 
      m.section.toLowerCase().includes(query)
    );
  });

  readonly sections = computed(() => {
    return [...new Set(this.filteredModules().map(m => m.section))];
  });

  // 3. Función para actualizar la búsqueda desde el HTML
  onSearch(event: Event) {
    const value = (event.target as HTMLInputElement).value;
    this.searchQuery.set(value);
  }

  constructor(
    private tokenService: Token,
    private authService: Auth,
    public themeService: ThemeService
  ) {}

  ngOnInit(): void {
    this.username.set(this.tokenService.getUser());
    this.roles.set(this.tokenService.getRoles());

    // 4. Cargamos las rutas desde el localStorage
    const savedRoutes = localStorage.getItem('rutas_permitidas');
    if (savedRoutes) {
      try {
        const parsed = JSON.parse(savedRoutes);
        this.allowedPaths.set(parsed);
      } catch (e) {
        console.error("Error al parsear rutas_permitidas", e);
      }
    }
  }

  // Ahora filtramos sobre la lista ya validada
  getModulesBySection(section: string): NavModule[] {
    return this.filteredModules().filter(m => m.section === section);
  }

  toggleTheme = () => this.themeService.toggleTheme();
  logout = () => this.authService.logout();
}