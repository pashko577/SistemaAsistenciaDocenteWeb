import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
// Importa los nuevos componentes (asumiendo que sigues tu estructura de carpetas)
import { NivelListComponent } from '../components/nivel/nivel-list/nivel-list';
import { CursoListComponent } from '../components/curso/curso-list/curso-list';
import { GradoListComponent } from '../components/grado/grado-list/grado-list';
import { SeccionListComponent } from '../components/seccion/seccion-list/seccion-list';
import { ClaseListComponent } from '../components/clase/clase-list/clase-list';
import { PeriodoListComponent } from '../components/periodo-academico/periodo-list/periodo-list';


@Component({
  selector: 'app-gestion-academica',
  standalone: true,
  imports: [
    CommonModule,
    NivelListComponent,
    CursoListComponent,
    GradoListComponent,
    SeccionListComponent,
    ClaseListComponent,
    PeriodoListComponent,
  ],
  templateUrl: './gestion-academica.html'
})
export class GestionAcademica {
  // Extendemos las opciones de tabs
  tabActiva = signal<'niveles' | 'cursos' | 'grados' | 'secciones' | 'clases' | 'periodos'>('niveles');

  cambiarTab(tab: 'niveles' | 'cursos' | 'grados' | 'secciones' | 'clases' | 'periodos') {
    this.tabActiva.set(tab);
  }
}