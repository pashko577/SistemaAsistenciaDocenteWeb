import { Component, OnInit } from '@angular/core';
import { CommonModule, registerLocaleData } from '@angular/common';
import { FormsModule } from '@angular/forms';
import localeEs from '@angular/common/locales/es';

// --- IMPORTACIONES DE ANGULAR MATERIAL ---
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatTooltipModule } from '@angular/material/tooltip';

// Modelos e Interfaces
import { AsistenciaAdministrativoResponse } from '../../../../core/models/Administrativos/asistenciaAdminsitrativos-Response';
import { AdministrativoResponse } from '../../../../core/models/Administrativos/administrativo-response';
import { PlanillaAdministrativoDTO } from '../../../../core/models/Administrativos/planillaAdministrativoDTO';

// Servicios
import { AsistenciaAdministrativoService } from '../../../../core/services/asistencia-administrativos';
import { AdministrativoService } from '../../../../core/services/administrativo_services';
import { PlanillaAdministrativoService } from '../../../../core/services/planilla-administrativos.services';

registerLocaleData(localeEs);

@Component({
  selector: 'app-reporte-administrativos',
  standalone: true,
  imports: [
    CommonModule, 
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatAutocompleteModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatTooltipModule
  ],
  templateUrl: './reporte-administrativos.html',
  styleUrl: './reporte-administrativos.css',
})
export class ReporteAdministrativos implements OnInit {
  // Listas
  listaAdministrativos: AdministrativoResponse[] = [];
  administrativosFiltrados: AdministrativoResponse[] = []; 
  
  // Filtros Dinámicos
  terminoBusqueda: string = '';
  mesSeleccionado: number = new Date().getMonth() + 1;
  anioSeleccionado: number = new Date().getFullYear(); // Año actual automático
  anios: number[] = [];
  administrativoId: number | null = null;
  nombreSeleccionado: string = '';

  // Datos del reporte
  reporte: AsistenciaAdministrativoResponse[] = [];
  totalMinutosTardanza: number = 0;
  montoDescuento: number = 0;
  sueldoBase: number = 0;

  meses = [
    { id: 1, nombre: 'ENERO' }, { id: 2, nombre: 'FEBRERO' }, { id: 3, nombre: 'MARZO' },
    { id: 4, nombre: 'ABRIL' }, { id: 5, nombre: 'MAYO' }, { id: 6, nombre: 'JUNIO' },
    { id: 7, nombre: 'JULIO' }, { id: 8, nombre: 'AGOSTO' }, { id: 9, nombre: 'SETIEMBRE' },
    { id: 10, nombre: 'OCTUBRE' }, { id: 11, nombre: 'NOVIEMBRE' }, { id: 12, nombre: 'DICIEMBRE' }
  ];

  constructor(
    private asistenciaService: AsistenciaAdministrativoService,
    private administrativoService: AdministrativoService,
    private planillaService: PlanillaAdministrativoService
  ) { }

  ngOnInit() {
    this.cargarAdministrativos();
    this.generarListaAnios();
  }

  generarListaAnios() {
    const anioActual = new Date().getFullYear();
    const anioInicio = 2024; // Año de inicio del sistema
    this.anios = [];
    // Genera desde el inicio hasta el año actual + 1 para previsión
    for (let i = anioInicio; i <= anioActual + 1; i++) {
      this.anios.push(i);
    }
  }

  cargarAdministrativos() {
    this.administrativoService.listar().subscribe({
      next: (data) => {
        this.listaAdministrativos = data;
        this.administrativosFiltrados = data;
      },
      error: (err) => console.error('Error al cargar lista', err)
    });
  }

  filtrarPersonal() {
    const busqueda = this.terminoBusqueda.toLowerCase().trim();
    this.administrativosFiltrados = busqueda.length >= 1 
      ? this.listaAdministrativos.filter(a => 
          a.nombres.toLowerCase().includes(busqueda) || a.apellidos.toLowerCase().includes(busqueda))
      : this.listaAdministrativos;
  }

  seleccionarAdministrativo(admin: AdministrativoResponse) {
    this.administrativoId = admin.id;
    this.terminoBusqueda = `${admin.apellidos}, ${admin.nombres}`;
    this.nombreSeleccionado = this.terminoBusqueda;
    this.consultarReporte();
  }

  consultarReporte() {
    if (!this.administrativoId) return;
    
    const idBusqueda = Number(this.administrativoId);
    const mesStr = this.mesSeleccionado.toString().padStart(2, '0');
    const inicio = `${this.anioSeleccionado}-${mesStr}-01`;
    const ultimoDia = new Date(this.anioSeleccionado, this.mesSeleccionado, 0).getDate();
    const fin = `${this.anioSeleccionado}-${mesStr}-${ultimoDia}`;

    this.planillaService.calcularPlanilla(idBusqueda, inicio, fin).subscribe({
      next: (data: PlanillaAdministrativoDTO) => {
        this.sueldoBase = data.sueldoBase;
        this.totalMinutosTardanza = data.minutosTardanza;
        this.montoDescuento = data.descuentoFaltas + data.descuentoTardanza;
      }
    });

    this.asistenciaService.listarPorPeriodo(idBusqueda, inicio, fin).subscribe({
      next: (data) => this.reporte = [...data]
    });
  }

  imprimir() { window.print(); }
}