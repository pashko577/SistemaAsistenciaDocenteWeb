import { ChangeDetectorRef, Component, computed, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';

import { SedeService } from '../../../../core/services/sede-service';
import { EspecialidadDocenteService } from '../../../../core/services/especialidad-docente';
import { TipoDocumentoService } from '../../../../core/services/tipo-documento';
import { DocenteResponse } from '../../../../core/models/docente-response';
import { DocenteService } from '../../../../core/services/docente';
import { forkJoin } from 'rxjs';

// Angular Material
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

import { DocenteForm } from './Modal-Docente/docente-form'; 
import { SearchDocente } from "./search-docente/search-docente";

@Component({
  selector: 'app-gestion-docente',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatIconModule,
    MatButtonModule,
    SearchDocente
  ],
  templateUrl: './gestion-docente.html',
  styleUrl: './gestion-docente.css',
})
export class GestionDocente implements OnInit {
  // Signals para estado reactivo
  docentes = signal<DocenteResponse[]>([]);
  loadingTable = signal(false);

  // Signal para manejar todos los filtros simultáneamente
  filtros = signal({
    busqueda: '',
    sedeId: null as number | null,
    especialidadId: null as number | null,
    estado: null as string | null
  });

  // Combos de datos
  sedes: any[] = [];
  tiposDocumento: any[] = [];
  especialidades: any[] = [];

  constructor(
    private docenteService: DocenteService,
    private sedeService: SedeService,
    private especialidadService: EspecialidadDocenteService,
    private tipoDocumentoService: TipoDocumentoService,
    private dialog: MatDialog,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.cargarDatosIniciales();
  }

  cargarDatosIniciales() {
    this.loadingTable.set(true);
    forkJoin({
      sedes: this.sedeService.listarSedes(),
      tipos: this.tipoDocumentoService.listar(),
      especialidades: this.especialidadService.listar(),
      docentes: this.docenteService.listarDocentes()
    }).subscribe({
      next: (res) => {
        this.sedes = res.sedes;
        this.tiposDocumento = res.tipos;
        this.especialidades = res.especialidades;
        
        // Mapeo corregido: usamos sedeId y especialidadId del DTO para buscar los nombres
        const docentesMapeados = res.docentes.map(d => ({
          ...d,
          nombreSede: d.nombreSede || this.obtenerNombreSede(d.sedeId),
          nombreEspecialidad: d.nombreEspecialidad || this.obtenerNombreEspecialidad(d.especialidadId)
        }));

        this.docentes.set(docentesMapeados);
        this.loadingTable.set(false);
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('❌ Error cargando datos:', err);
        this.loadingTable.set(false);
      }
    });
  }

  // Lógica de filtrado multivariable
  filteredDocentes = computed(() => {
    const f = this.filtros();
    const lista = this.docentes();

    return lista.filter(doc => {
      // 1. Filtro de búsqueda (DNI, Nombre o Apellido)
      const term = f.busqueda.toLowerCase().trim();
      const cumpleTexto = !term || 
        doc.nombres.toLowerCase().includes(term) || 
        doc.apellidos.toLowerCase().includes(term) || 
        doc.dni.includes(term);

      // 2. Filtro de Sede
      const cumpleSede = !f.sedeId || Number(doc.sedeId) === Number(f.sedeId);

      // 3. Filtro de Especialidad
      const cumpleEspecialidad = !f.especialidadId || Number(doc.especialidadId) === Number(f.especialidadId);

      // 4. Filtro de Estado: Si es null, mostramos todo menos INACTIVO por defecto
      const cumpleEstado = f.estado 
        ? doc.estado === f.estado 
        : doc.estado !== 'INACTIVO';

      return cumpleTexto && cumpleSede && cumpleEspecialidad && cumpleEstado;
    });
  });

  // Método que recibe los filtros desde el componente hijo (SearchDocente)
  manejarFiltros(filtrosRecibidos: any) {
    this.filtros.set({
      busqueda: filtrosRecibidos.busqueda || '',
      sedeId: filtrosRecibidos.sedeId,
      especialidadId: filtrosRecibidos.especialidadId,
      estado: filtrosRecibidos.estado
    });
  }

  abrirModal(docente?: DocenteResponse) {
    const dialogRef = this.dialog.open(DocenteForm, {
      width: '100%',
      maxWidth: '650px',
      data: {
        sedes: this.sedes,
        tiposDoc: this.tiposDocumento,
        especialidades: this.especialidades,
        docenteSelected: docente 
      },
      disableClose: true
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        const docenteConNombres = {
          ...result,
          nombreSede: this.obtenerNombreSede(result.sedeId),
          nombreEspecialidad: this.obtenerNombreEspecialidad(result.especialidadId)
        };

        this.docentes.update(listaActual => {
          const index = listaActual.findIndex(d => d.id === result.id);
          if (index !== -1) {
            const nuevaLista = [...listaActual];
            nuevaLista[index] = docenteConNombres;
            return nuevaLista;
          }
          return [docenteConNombres, ...listaActual];
        });
        this.cdr.detectChanges();
      }
    });
  }

  eliminarDocente(id: number) {
    if (confirm('¿Estás seguro de inactivar a este docente?')) {
      this.docenteService.eliminarDocente(id).subscribe({
        next: () => {
          this.docentes.update(lista => 
            lista.map(d => d.id === id ? { ...d, estado: 'INACTIVO' } : d)
          );
        },
        error: (err) => {
          console.error('❌ Error al inactivar:', err);
          alert('No se pudo inactivar al docente.');
        }
      });
    }
  }

  obtenerNombreSede(sedeId: number | string): string {
    if (!sedeId) return 'No especificada';
    const sede = this.sedes.find(s => Number(s.id) === Number(sedeId));
    return sede ? sede.nombreSede : 'Sede ID: ' + sedeId;
  }

  obtenerNombreEspecialidad(especialidadId: number | string): string {
    if (!especialidadId) return 'No especificada';
    const esp = this.especialidades.find(e => 
      Number(e.id) === Number(especialidadId) || 
      Number(e.especialidadDocenteId) === Number(especialidadId)
    );
    return esp ? esp.nombreEspecialidad : 'Especialidad ID: ' + especialidadId;
  }
}