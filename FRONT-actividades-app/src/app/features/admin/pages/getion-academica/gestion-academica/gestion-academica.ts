import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
// Importa los nuevos componentes (asumiendo que sigues tu estructura de carpetas)
import { NivelListComponent } from '../components/nivel/nivel-list/nivel-list';
import { CursoListComponent } from '../components/curso/curso-list/curso-list';
import { GradoListComponent } from '../components/grado/grado-list/grado-list';
import { SeccionListComponent } from '../components/seccion/seccion-list/seccion-list';

@Component({
  selector: 'app-gestion-academica',
  standalone: true,
  imports: [
    CommonModule,
    NivelListComponent,
    CursoListComponent,
    GradoListComponent,
    SeccionListComponent
  ],
  templateUrl: './gestion-academica.html'
})
export class GestionAcademica {
  // Extendemos las opciones de tabs
  tabActiva = signal<'niveles' | 'cursos' | 'grados' | 'secciones'>('niveles');

  cambiarTab(tab: 'niveles' | 'cursos' | 'grados' | 'secciones') {
    this.tabActiva.set(tab);
  }
}