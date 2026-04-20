import { Component, OnInit, inject, Input, Output, EventEmitter, signal, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { forkJoin } from 'rxjs';
import { AsignacionDocenteService } from '../../../../../../core/services/asignaciondocente/asignaciondocente_services';
import { AsignacionDocenteResponse } from '../../../../../../core/models/Docente/asignacion-docente/asignacion-docente-response';

@Component({
  selector: 'app-asignacion-form-modal',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatIconModule
  ],
  templateUrl: './asignacion-form-modal.html',
  styleUrl: './asignacion-form-modal.css'
})
export class AsignacionFormModalComponent implements OnInit {
  private fb = inject(FormBuilder);
  private asignacionService = inject(AsignacionDocenteService);
  private cdr = inject(ChangeDetectorRef);

  @Input() asignacion?: AsignacionDocenteResponse;
  @Output() onClose = new EventEmitter<void>();
  @Output() onSave = new EventEmitter<void>();

  asignacionForm: FormGroup;
  isEdit = false;
  isLoadingData = signal(true);

  // Signals para las listas auxiliares
  docentes = signal<any[]>([]);
  clases = signal<any[]>([]);
  tiposActividad = signal<any[]>([]);

  constructor() {
    this.asignacionForm = this.fb.group({
      docenteId: [null, Validators.required],
      claseId: [null, Validators.required],
      tipoActividadId: [null, Validators.required],
      estado: ['NUEVO', Validators.required],
      observaciones: ['']
    });
  }

  ngOnInit(): void {
    this.loadInitialData();
  }

  private loadInitialData(): void {
    this.isLoadingData.set(true);

    // Cargamos todos los datos necesarios en paralelo
    forkJoin({
      docentes: this.asignacionService.listarDocentes(),
      clases: this.asignacionService.listarClases(),
      tipos: this.asignacionService.listarTiposActividad()
    }).subscribe({
      next: (results) => {
        this.docentes.set(results.docentes);
        this.clases.set(results.clases);
        this.tiposActividad.set(results.tipos);

        // Si es edición, ahora que los combos tienen datos, aplicamos el valor
        if (this.asignacion) {
          this.isEdit = true;
          this.asignacionForm.patchValue({
            docenteId: this.asignacion.docenteId,
            claseId: this.asignacion.claseId,
            tipoActividadId: this.asignacion.tipoActividadId,
            estado: this.asignacion.estado,
            observaciones: this.asignacion.observaciones
          });
        }
        this.isLoadingData.set(false);
        // Forzamos la detección de cambios para asegurar que los selects se pueblen en el primer render
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error al cargar datos auxiliares:', err);
        this.isLoadingData.set(false);
        this.cdr.detectChanges();
      }
    });
  }

  save(): void {
    if (this.asignacionForm.invalid) return;

    const data = this.asignacionForm.value;
    const observer = {
      next: () => {
        this.onSave.emit();
        this.onClose.emit();
      },
      error: (err: any) => {
        console.error('Error al guardar asignación:', err);
        alert(err.error?.message || 'Error al procesar la solicitud');
      }
    };

    if (this.isEdit && this.asignacion) {
      this.asignacionService.actualizar(this.asignacion.id, data).subscribe(observer);
    } else {
      this.asignacionService.registrar(data).subscribe(observer);
    }
  }

  closeModal(): void {
    this.onClose.emit();
  }
}
