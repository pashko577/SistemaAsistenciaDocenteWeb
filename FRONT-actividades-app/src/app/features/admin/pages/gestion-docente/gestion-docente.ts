import { ChangeDetectorRef, Component, computed, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';

import { SedeService } from '../../../../core/services/sede-service';
import { EspecialidadDocenteService } from '../../../../core/services/especialidad-docente';
import { TipoDocumentoService } from '../../../../core/services/tipo-documento';
import { DocenteResponse } from '../../../../core/models/docente-response';
import { forkJoin } from 'rxjs';

// Angular Material
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

import { DocenteForm } from './Modal-Docente/docente-form'; 
import { DocenteService } from '../../../../core/services/docente';

@Component({
  selector: 'app-gestion-docente',
  standalone: true,
  imports: [
    CommonModule, 
    ReactiveFormsModule, 
    MatDialogModule, 
    MatIconModule, 
    MatButtonModule
  ],
  templateUrl: './gestion-docente.html',
  styleUrl: './gestion-docente.css',
})
export class GestionDocente implements OnInit {
  // Signals para estado reactivo
  docentes = signal<DocenteResponse[]>([]);
  searchTerm = signal('');
  loadingTable = signal(false);

  // Combos de datos (se llenan con forkJoin)
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
        
        // Mapeo inicial para asegurar que todos tengan sus nombres de sede/especialidad
        const docentesMapeados = res.docentes.map(d => ({
          ...d,
          nombreSede: d.nombreSede || this.obtenerNombreSede(d.id),
          nombreEspecialidad: d.nombreEspecialidad || this.obtenerNombreEspecialidad(d.id)
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

  // Filtrado reactivo: oculta INACTIVOS y aplica búsqueda
  filteredDocentes = computed(() => {
    const term = this.searchTerm().toLowerCase().trim();
    const lista = this.docentes().filter(d => d.estado !== 'INACTIVO'); 
    
    if (!term) return lista;

    return lista.filter(doc =>
      doc.nombres.toLowerCase().includes(term) ||
      doc.apellidos.toLowerCase().includes(term) ||
      doc.dni.includes(term)
    );
  });

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
      // Cambio: Usamos el nuevo método 'eliminar' del Service
      this.docenteService.eliminarDocente(id).subscribe({
        next: () => {
          // Actualización local para que el computed lo oculte
          this.docentes.update(lista => 
            lista.map(d => d.id === id ? { ...d, estado: 'INACTIVO' } : d)
          );
          console.log('✅ Docente inactivado correctamente');
        },
        error: (err) => {
          console.error('❌ Error al inactivar:', err);
          alert('No se pudo inactivar al docente. Verifique la conexión.');
        }
      });
    }
  }

  onSearch(event: Event) {
    const element = event.target as HTMLInputElement;
    this.searchTerm.set(element.value);
  }

  obtenerIniciales(nombres: string, apellidos: string): string {
    if (!nombres || !apellidos) return '??';
    return (nombres.charAt(0) + apellidos.charAt(0)).toUpperCase();
  }

  obtenerNombreSede(sedeId: number | string): string {
    if (!sedeId) return 'No especificada';
    const sede = this.sedes.find(s => Number(s.id) === Number(sedeId));
    return sede ? sede.nombreSede : 'Sede ID: ' + sedeId;
  }

  obtenerNombreEspecialidad(especialidadId: number | string): string {
    if (!especialidadId) return 'No especificada';
    // Ajustado para buscar por la propiedad correcta del objeto Especialidad
    const esp = this.especialidades.find(e => 
      Number(e.id) === Number(especialidadId) || 
      Number(e.especialidadDocenteId) === Number(especialidadId)
    );
    return esp ? esp.nombreEspecialidad : 'Especialidad ID: ' + especialidadId;
  }
}