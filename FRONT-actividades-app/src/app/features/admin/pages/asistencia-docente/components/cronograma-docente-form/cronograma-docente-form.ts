import { Component, EventEmitter, OnInit, Output, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { DiaSemana } from '../../../../../../core/models/enums/dia-semana';
import { CronogramaDocenteService } from '../../../../../../core/services/docente/cronograma_docente_services';
import { HorarioBloqueService } from '../../../../../../core/services/docente/horario_bloque_services';
import { CronogramaDocenteRequest } from '../../../../../../core/models/Docente/cronograma/cronograma-docente-request';
import { AsignacionDocenteResponse } from '../../../../../../core/models/Docente/asignacion-docente/asignacion-docente-response';
import { HorarioBloqueResponse } from '../../../../../../core/models/Docente/horario-bloque/horario-bloque-response';

@Component({
  selector: 'app-cronograma-docente-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './cronograma-docente-form.html',
  styleUrl: './cronograma-docente-form.css',
})
export class CronogramaDocenteForm implements OnInit {
  @Output() guardado = new EventEmitter<void>();
  cronogramaForm!: FormGroup;
  asignaciones: AsignacionDocenteResponse[] = []; 
  bloques: HorarioBloqueResponse[] = [];      
  bloquesFiltrados: HorarioBloqueResponse[] = [];
  dias = Object.values(DiaSemana);

  constructor(
    private fb: FormBuilder,
    private cronogramaService: CronogramaDocenteService,
    private horarioBloqueService: HorarioBloqueService,
    private cdr: ChangeDetectorRef
  ) {
    this.initForm();
  }

  ngOnInit(): void {
    this.cargarDatosIniciales();
    
    // Escuchar cambios en la asignación para filtrar bloques por nivel
    this.cronogramaForm.get('asignacionDocenteId')?.valueChanges.subscribe(id => {
        this.filtrarBloquesPorAsignacion(id);
    });
  }

  private initForm(): void {
    this.cronogramaForm = this.fb.group({
      asignacionDocenteId: ['', Validators.required],
      horarioBloqueId: ['', Validators.required],
      diaSemana: ['', Validators.required]
    });
  }

  private cargarDatosIniciales(): void {
    this.horarioBloqueService.listar().subscribe(data => {
        this.bloques = data;
        this.bloquesFiltrados = data;
    });
    this.cronogramaService.listarAsignacionesActivas().subscribe(data => this.asignaciones = data);
  }

  private filtrarBloquesPorAsignacion(asignacionId: any): void {
      if (!asignacionId) {
          this.bloquesFiltrados = this.bloques;
          return;
      }
      
      const asig = this.asignaciones.find(a => a.id == asignacionId);
      if (asig) {
          // Buscamos el ID del nivel. 
          // Nota: El backend en AsignacionDocenteResponseDTO nos da 'nivelNombre'. 
          // Si no tenemos el nivelId, podemos filtrar por nombre o pedirlo en el DTO.
          // Para ser robustos, filtramos por aquello que coincida.
          this.bloquesFiltrados = this.bloques.filter(b => b.nivelNombre === asig.nivelNombre);
          
          // Si no hay bloques para ese nivel, mostrar todos o vacio? 
          // Mejor vacio para forzar configuracion correcta.
          if (this.bloquesFiltrados.length === 0) {
              console.warn('No hay bloques configurados para el nivel:', asig.nivelNombre);
          }
      }
      this.cronogramaForm.get('horarioBloqueId')?.setValue('');
      this.cdr.detectChanges();
  }

  guardar(): void {
    if (this.cronogramaForm.valid) {
      const request: CronogramaDocenteRequest = {
        asignacionDocenteId: Number(this.cronogramaForm.value.asignacionDocenteId),
        horarioBloqueId: Number(this.cronogramaForm.value.horarioBloqueId),
        diaSemana: this.cronogramaForm.value.diaSemana
      };
      
      this.cronogramaService.crearCronograma(request).subscribe({
        next: (res) => {
          this.cronogramaForm.reset();
          this.guardado.emit();
          alert('Horario guardado correctamente');
        },
        error: (err) => {
          console.error('Error al guardar cronograma', err);
          alert('Error: ' + (err.error?.message || 'No se pudo guardar el horario'));
        }
      });
    }
  }
}