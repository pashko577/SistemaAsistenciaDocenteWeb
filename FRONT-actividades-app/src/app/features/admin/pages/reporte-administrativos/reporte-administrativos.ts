import { Component, OnInit } from '@angular/core';
import { CommonModule, registerLocaleData } from '@angular/common';
import { FormsModule } from '@angular/forms';
import localeEs from '@angular/common/locales/es';

// Angular Material
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatTooltipModule } from '@angular/material/tooltip';

// Modelos y Servicios
import { AsistenciaAdministrativoResponse } from '../../../../core/models/Administrativos/asistenciaAdminsitrativos-Response';
import { AdministrativoResponse } from '../../../../core/models/Administrativos/administrativo-response';
import { PlanillaAdministrativoDTO } from '../../../../core/models/Administrativos/planillaAdministrativoDTO';
import { AsistenciaAdministrativoService } from '../../../../core/services/asistencia-administrativos';
import { AdministrativoService } from '../../../../core/services/administrativo_services';
import { PlanillaAdministrativoService } from '../../../../core/services/planilla-administrativos.services';

import * as ExcelJS from 'exceljs';
import { saveAs } from 'file-saver';

registerLocaleData(localeEs);

@Component({
  selector: 'app-reporte-administrativos',
  standalone: true,
  imports: [
    CommonModule, FormsModule, MatFormFieldModule, MatInputModule,
    MatAutocompleteModule, MatSelectModule, MatButtonModule,
    MatIconModule, MatCardModule, MatTooltipModule
  ],
  templateUrl: './reporte-administrativos.html',
  styleUrls: ['./reporte-administrativos.css'],
})
export class ReporteAdministrativos implements OnInit {
  listaAdministrativos: AdministrativoResponse[] = [];
  administrativosFiltrados: AdministrativoResponse[] = []; 
  
  terminoBusqueda: string = '';
  mesSeleccionado: number = new Date().getMonth() + 1;
  anioSeleccionado: number = new Date().getFullYear();
  anios: number[] = [];
  administrativoId: number | null = null;
  nombreSeleccionado: string = '';
  cargoAdministrativo: string = '';
  dniAdministrativo: string = '';
  mostrarDropdown = false;

  // Datos del Reporte
  reporte: AsistenciaAdministrativoResponse[] = [];
  totalMinutosTardanza: number = 0;
  montoDescuento: number = 0;
  sueldoBase: number = 0;
  conteoFaltas: number = 0;
  conteoPermisos: number = 0;
  cargando: boolean = false;

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
    this.anios = [];
    for (let i = 2025; i <= anioActual + 2; i++) {
      this.anios.push(i);
    }
  }

  cargarAdministrativos() {
    this.administrativoService.listar().subscribe({
      next: (data) => {
        this.listaAdministrativos = data;
        this.administrativosFiltrados = data;
      },
      error: (err) => console.error('Error cargando administrativos:', err)
    });
  }

 filtrarPersonal() {
  const busqueda = this.terminoBusqueda.toLowerCase().trim();
  
  // Si no hay búsqueda, mostramos todos (o podrías ocultar la lista)
  if (!busqueda) {
    this.administrativosFiltrados = [...this.listaAdministrativos];
    return;
  }

  this.administrativosFiltrados = this.listaAdministrativos.filter(a => 
    `${a.apellidos} ${a.nombres}`.toLowerCase().includes(busqueda) ||
    a.dni.includes(busqueda) // ¡Tip extra! Permite buscar también por DNI
  );
}

  seleccionarAdministrativo(admin: AdministrativoResponse) {
    this.administrativoId = admin.id;
    this.nombreSeleccionado = `${admin.apellidos}, ${admin.nombres}`;
    this.terminoBusqueda = this.nombreSeleccionado;
    this.cargoAdministrativo = admin.nombreCargo;
    this.dniAdministrativo = admin.dni;
    this.mostrarDropdown = false; // Cerramos al seleccionar
    this.consultarReporte();
  }

  consultarReporte() {
    if (!this.administrativoId) return;
    
    this.cargando = true;
    
    const mesStr = this.mesSeleccionado.toString().padStart(2, '0');
    const inicio = `${this.anioSeleccionado}-${mesStr}-01`;
    const ultimoDia = new Date(this.anioSeleccionado, this.mesSeleccionado, 0).getDate();
    const fin = `${this.anioSeleccionado}-${mesStr}-${ultimoDia}`;

    // 1. Obtener cálculos detallados
    this.planillaService.calcularPlanilla(this.administrativoId, inicio, fin).subscribe({
      next: (data: PlanillaAdministrativoDTO) => {
        this.sueldoBase = data.sueldoBase;
        this.totalMinutosTardanza = data.minutosTardanza;
        this.conteoFaltas = data.faltas;
        this.conteoPermisos = data.permisos;
        this.montoDescuento = (data.descuentoFaltas || 0) + (data.descuentoTardanza || 0);
      },
      error: (err) => {
        console.error('Error calculando planilla:', err);
        this.cargando = false;
      }
    });

    // 2. Obtener lista de registros para la tabla
    this.asistenciaService.listarPorPeriodo(this.administrativoId, inicio, fin).subscribe({
      next: (data) => {
        this.reporte = [...data];
        this.cargando = false;
      },
      error: (err) => {
        console.error('Error cargando asistencias:', err);
        this.cargando = false;
      }
    });
  }

  obtenerNombreMes(mesId: number): string {
    const mes = this.meses.find(m => m.id === mesId);
    return mes ? mes.nombre : '';
  }

  imprimir() { 
    window.print(); 
  }

  async exportarExcel() {
    if (this.reporte.length === 0) return;

    const workbook = new ExcelJS.Workbook();
    const worksheet = workbook.addWorksheet('Asistencia');
    const nombreMes = this.obtenerNombreMes(this.mesSeleccionado);

    // Configuración de estilos
    worksheet.properties.defaultRowHeight = 25;
    
    // Columnas
    worksheet.columns = [
      { header: 'FECHA', key: 'fecha', width: 25 },
      { header: 'INGRESO', key: 'ingreso', width: 12 },
      { header: 'ALMUERZO', key: 'almuerzo', width: 18 },
      { header: 'SALIDA', key: 'salida', width: 12 },
      { header: 'TERNO', key: 'terno', width: 10 },
      { header: 'TARDANZA', key: 'tardanza', width: 15 }
    ];

    // Estilo del header
    worksheet.getRow(1).height = 30;
    worksheet.getRow(1).font = { bold: true, size: 12 };
    
    // Título
    worksheet.mergeCells('A1:F1');
    const title = worksheet.getCell('A1');
    title.value = `REPORTE DE ASISTENCIA - ${nombreMes} ${this.anioSeleccionado}`;
    title.font = { bold: true, size: 14, color: { argb: 'FF064E3B' } };
    title.alignment = { horizontal: 'center', vertical: 'middle' };

    // Datos del empleado
    worksheet.mergeCells('A2:F2');
    worksheet.getCell('A2').value = `EMPLEADO: ${this.nombreSeleccionado.toUpperCase()} | DNI: ${this.dniAdministrativo} | CARGO: ${this.cargoAdministrativo || 'No definido'}`;
    worksheet.getCell('A2').alignment = { horizontal: 'center', vertical: 'middle' };
    worksheet.getCell('A2').font = { size: 11, color: { argb: 'FF4B5563' } };

    // Datos
    this.reporte.forEach(item => {
      worksheet.addRow({
        fecha: new Date(item.fecha).toLocaleDateString('es-PE', { 
          weekday: 'long', 
          day: '2-digit', 
          month: '2-digit', 
          year: 'numeric' 
        }).toUpperCase(),
        ingreso: item.horaIngreso || '--:--',
        almuerzo: `${item.salidaAlmuerzo || '--'} | ${item.retornoAlmuerzo || '--'}`,
        salida: item.horaSalida || '--:--',
        terno: item.terno ? 'SÍ' : 'NO',
        tardanza: item.tardanza > 0 ? `${item.tardanza} min` : '-'
      });
    });

    // Resumen
    worksheet.addRow([]);
    worksheet.addRow({ fecha: 'RESUMEN:', tardanza: `Faltas: ${this.conteoFaltas}` });
    worksheet.addRow({ tardanza: `Permisos: ${this.conteoPermisos}` });
    worksheet.addRow({ tardanza: `Total Tardanza: ${this.totalMinutosTardanza} min` });
    worksheet.addRow({ tardanza: `DESCUENTO TOTAL: S/ ${this.montoDescuento.toFixed(2)}` });

    // Estilos para el resumen
    const lastRow = worksheet.lastRow;
    if (lastRow) {
      lastRow.font = { bold: true, color: { argb: 'FFDC2626' } };
    }

    // Aplicar bordes
    worksheet.eachRow((row) => {
      row.eachCell((cell) => {
        cell.border = {
          top: { style: 'thin' },
          left: { style: 'thin' },
          bottom: { style: 'thin' },
          right: { style: 'thin' }
        };
      });
    });

    const buffer = await workbook.xlsx.writeBuffer();
    saveAs(new Blob([buffer]), `Asistencia_${this.nombreSeleccionado.replace(/\s/g, '_')}_${nombreMes}_${this.anioSeleccionado}.xlsx`);
  }
}