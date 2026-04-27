import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HorarioBloqueService } from '../../../../../../core/services/docente/horario_bloque_services';
import { NivelService } from '../../../../../../core/services/Clase/nivel_services';
import { HorarioBloqueRequest } from '../../../../../../core/models/Docente/horario-bloque/horario-bloque-request';
import { NivelResponse } from '../../../../../../core/models/Docente/clase/nivel-response';

@Component({
  selector: 'app-horario-bloque-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './horario-bloque-form.html',
})
export class HorarioBloqueForm implements OnInit {
  @Input() bloqueId: number | null = null;
  @Output() guardado = new EventEmitter<void>();
  @Output() cancelar = new EventEmitter<void>();

  bloqueForm!: FormGroup;
  niveles: NivelResponse[] = [];
  isEdit = false;

  constructor(
    private fb: FormBuilder,
    private horarioService: HorarioBloqueService,
    private nivelService: NivelService
  ) {
    this.initForm();
  }

  ngOnInit(): void {
    this.cargarNiveles();
    if (this.bloqueId) {
      this.isEdit = true;
      this.cargarDatos(this.bloqueId);
    }
  }

  private initForm(): void {
    this.bloqueForm = this.fb.group({
      horaInicio: ['', Validators.required],
      horaFin: ['', Validators.required],
      ordenBloque: [1, [Validators.required, Validators.min(1)]],
      nivelId: ['', Validators.required]
    });
  }

  private cargarNiveles(): void {
    this.nivelService.listar().subscribe((data: NivelResponse[]) => this.niveles = data);
  }

  private cargarDatos(id: number): void {
    this.horarioService.obtenerPorId(id).subscribe({
        next: (data) => {
            this.bloqueForm.patchValue({
                horaInicio: data.horaInicio,
                horaFin: data.horaFin,
                ordenBloque: data.ordenBloque,
                nivelId: data.nivelId
            });
        }
    });
  }

  guardar(): void {
    if (this.bloqueForm.valid) {
      const request: HorarioBloqueRequest = this.bloqueForm.value;
      
      const obs = this.isEdit && this.bloqueId
        ? this.horarioService.actualizar(this.bloqueId, request)
        : this.horarioService.crear(request);

      obs.subscribe({
        next: () => {
          this.guardado.emit();
          this.bloqueForm.reset({ ordenBloque: 1 });
        },
        error: (err) => {
            console.error('Error al guardar bloque', err);
            alert('Error al guardar: ' + (err.error?.message || 'Verifique el orden del bloque'));
        }
      });
    }
  }
}
