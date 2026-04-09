// header.ts
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Token } from '../../../../core/services/token';
import { Auth } from '../../../../core/services/auth';
import { ThemeService } from '../../../../core/services/theme_service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class Header implements OnInit {
  username: string | null = '';
  roles: string[] = [];
  isMenuOpen = false;
  
  // NO inicializar aquí - solo declarar el tipo
  currentTheme: any; 

  constructor(
    private tokenService: Token,
    private authService: Auth,
    public themeService: ThemeService // ✅ Se inyecta correctamente
  ) {
    this.username = this.tokenService.getUser();
    this.roles = this.tokenService.getRoles();
  }

  ngOnInit(): void {
    // ✅ Inicializar después de que el servicio esté disponible
    this.currentTheme = this.themeService.currentTheme;
    console.log('🎯 Tema actual en Header:', this.currentTheme());
  }

  toggleTheme(): void {
    console.log('🖱️ Toggle theme clickeado');
    this.themeService.toggleTheme();
  }

  logout(): void {
    this.authService.logout();
  }
}