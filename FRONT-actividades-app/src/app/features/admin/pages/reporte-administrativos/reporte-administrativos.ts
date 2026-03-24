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

import * as ExcelJS from 'exceljs';
import { saveAs } from 'file-saver';

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

async exportarExcel() {
  if (this.reporte.length === 0) return;

  const workbook = new ExcelJS.Workbook();
  const worksheet = workbook.addWorksheet('Reporte Asistencia');

  // --- CONFIGURACIÓN DE COLUMNAS ---
  worksheet.columns = [
    { header: 'DÍA / FECHA', key: 'fecha', width: 25 },
    { header: 'INGRESO', key: 'ingreso', width: 15 },
    { header: 'ALMUERZO (S|R)', key: 'almuerzo', width: 20 },
    { header: 'SALIDA', key: 'salida', width: 15 },
    { header: 'TERNO', key: 'terno', width: 12 },
    { header: 'TARDANZA', key: 'tardanza', width: 15 },
    { header: '', key: 'gap', width: 5 }, // Columna de separación
    { header: 'RESUMEN', key: 'resumen', width: 25 }
  ];

  // --- ESTILOS DE ENCABEZADO TIPO CARD (A1:F2) ---
  worksheet.mergeCells('A1:F1');
  const titleCell = worksheet.getCell('A1');
  titleCell.value = 'REPORTE DE ASISTENCIA - COLEGIO JAMES BALDWIN';
  titleCell.font = { bold: true, size: 14, color: { argb: 'FF1E293B' } }; // Slate 800
  titleCell.alignment = { horizontal: 'center' };

  worksheet.mergeCells('A2:F2');
  const nameCell = worksheet.getCell('A2');
  nameCell.value = this.nombreSeleccionado.toUpperCase();
  nameCell.font = { bold: true, size: 12, color: { argb: 'FF2563EB' } }; // Blue 600
  nameCell.alignment = { horizontal: 'center' };

  // --- ETIQUETA DE MES (AMARILLO) ---
  worksheet.mergeCells('A3:F3');
  const periodCell = worksheet.getCell('A3');
  const nombreMes = this.meses[this.mesSeleccionado - 1].nombre;
  periodCell.value = `${nombreMes} ${this.anioSeleccionado}`;
  periodCell.fill = { type: 'pattern', pattern: 'solid', fgColor: { argb: 'FFFACC15' } }; // Yellow 400
  periodCell.font = { bold: true, color: { argb: 'FF000000' } };
  periodCell.alignment = { horizontal: 'center' };

  // --- CABECERA DE TABLA (Fila 5) ---
  const headerRow = worksheet.getRow(5);
  const headers = ['DÍA / FECHA', 'INGRESO', 'ALMUERZO', 'SALIDA', 'TERNO', 'TARDANZA'];
  
  headers.forEach((h, i) => {
    const cell = headerRow.getCell(i + 1);
    cell.value = h;
    cell.fill = { type: 'pattern', pattern: 'solid', fgColor: { argb: 'FFF8FAFC' } }; // Slate 50
    cell.font = { bold: true, size: 10, color: { argb: 'FF64748B' } }; // Slate 500
    cell.border = { bottom: { style: 'medium', color: { argb: 'FFE2E8F0' } } };
    cell.alignment = { horizontal: 'center' };
  });

  // --- CUERPO DE LA TABLA ---
  this.reporte.forEach((item, index) => {
    const rowIndex = index + 6;
    const row = worksheet.getRow(rowIndex);

    // Formatear Fecha y Día
    const fechaDoc = new Date(item.fecha);
    const diaNombre = fechaDoc.toLocaleDateString('es-ES', { weekday: 'long' }).toUpperCase();
    const fechaCorta = fechaDoc.toLocaleDateString('es-ES');

    row.getCell(1).value = `${diaNombre}\n${fechaCorta}`;
    row.getCell(1).alignment = { wrapText: true, vertical: 'middle' };
    
    row.getCell(2).value = item.horaIngreso || '--:--';
    
    row.getCell(3).value = `${item.salidaAlmuerzo || '--'} | ${item.retornoAlmuerzo || '--'}`;
    
    row.getCell(4).value = item.horaSalida || '--:--';
    
    row.getCell(5).value = item.terno ? '👔 SÍ' : '-';
    
    row.getCell(6).value = item.tardanza > 0 ? `+${item.tardanza} min` : '-';

    // Estilos de fila
    row.eachCell((cell, colNumber) => {
      cell.alignment = { horizontal: 'center', vertical: 'middle' };
      cell.border = { bottom: { style: 'thin', color: { argb: 'FFF1F5F9' } } };
      
      // Tardanza en rojo (como en tu HTML)
      if (colNumber === 6 && item.tardanza > 0) {
        cell.font = { color: { argb: 'FFDC2626' }, bold: true };
        cell.fill = { type: 'pattern', pattern: 'solid', fgColor: { argb: 'FFFEE2E2' } };
      }
    });
  });

  // --- CUADROS DE TOTALES (ESTILO CARD DERECHA) ---
  // Tardanza Total
  const tardanzaTitle = worksheet.getCell('H6');
  tardanzaTitle.value = 'TARDANZA TOTAL';
  tardanzaTitle.font = { bold: true, size: 9, color: { argb: 'FF64748B' } };
  tardanzaTitle.fill = { type: 'pattern', pattern: 'solid', fgColor: { argb: 'FFF8FAFC' } };
  tardanzaTitle.border = { left: { style: 'medium', color: { argb: 'FFEF4444' } } };

  const tardanzaValue = worksheet.getCell('H7');
  tardanzaValue.value = `${this.totalMinutosTardanza} min`;
  tardanzaValue.font = { bold: true, size: 14, color: { argb: 'FFDC2626' } };

  // Descuento (Estilo Dark Card)
  const descTitle = worksheet.getCell('H9');
  descTitle.value = 'DESCUENTO TOTAL';
  descTitle.font = { bold: true, size: 9, color: { argb: 'FFA1A1AA' } };
  descTitle.fill = { type: 'pattern', pattern: 'solid', fgColor: { argb: 'FF18181B' } }; // Zinc 900

  const descValue = worksheet.getCell('H10');
  descValue.value = `S/ ${this.montoDescuento.toFixed(2)}`;
  descValue.font = { bold: true, size: 16, color: { argb: 'FFFFFFFF' } };
  descValue.fill = { type: 'pattern', pattern: 'solid', fgColor: { argb: 'FF18181B' } };
  descValue.alignment = { horizontal: 'center' };

  // --- FIRMAS (AL FINAL) ---
  const lastRow = this.reporte.length + 10;
  worksheet.getCell(`B${lastRow}`).value = '__________________________';
  worksheet.getCell(`B${lastRow + 1}`).value = 'FIRMA DEL EMPLEADO';
  worksheet.getCell(`E${lastRow}`).value = '__________________________';
  worksheet.getCell(`E${lastRow + 1}`).value = 'RECURSOS HUMANOS';
  
  [worksheet.getCell(`B${lastRow + 1}`), worksheet.getCell(`E${lastRow + 1}`)].forEach(c => {
    c.alignment = { horizontal: 'center' };
    c.font = { size: 8, color: { argb: 'FF94A3B8' }, bold: true };
  });

  // --- DESCARGAR ---
  const buffer = await workbook.xlsx.writeBuffer();
  const blob = new Blob([buffer], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
  saveAs(blob, `Asistencia_${this.nombreSeleccionado}_${nombreMes}.xlsx`);
}
// Función auxiliar para el nombre del día
obtenerNombreDia(fecha: string | Date): string {
  return new Date(fecha).toLocaleDateString('es-PE', { weekday: 'long' }).toUpperCase();
}
}