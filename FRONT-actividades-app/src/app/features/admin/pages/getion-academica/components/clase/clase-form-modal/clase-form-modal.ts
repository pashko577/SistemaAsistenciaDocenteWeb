import { Component, OnInit, Input, Output, EventEmitter, signal, inject, computed } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ClaseResponse } from '../../../../../../../core/models/Docente/clase/clase-response';
import { ClaseService } from '../../../../../../../core/services/Clase/clase_services';
import { CursoService } from '../../../../../../../core/services/Clase/curso_services';
import { SeccionService } from '../../../../../../../core/services/Clase/secciones_services';
import { PeriodoAcademicoService } from '../../../../../../../core/services/Clase/periodo_academico_services';

@Component({
  selector: 'app-clase-form-modal',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './clase-form-modal.html'
})
export class ClaseFormModalComponent implements OnInit {
  @Input() clase?: ClaseResponse;
  @Output() onClose = new EventEmitter<void>();
  @Output() onSave = new EventEmitter<void>();

  // Inyección moderna con inject
  private fb = inject(FormBuilder);
  private claseService = inject(ClaseService);
  private cursoService = inject(CursoService);
  private seccionService = inject(SeccionService);
  private periodoService = inject(PeriodoAcademicoService);

  form: FormGroup;
  cursos = signal<any[]>([]);
  secciones = signal<any[]>([]);
  periodos = signal<any[]>([]);


  constructor() {
    this.form = this.fb.group({
      tiempoClase: [60, [Validators.required, Validators.min(15)]],
      aula: ['', [Validators.required]],
      cursoId: ['', [Validators.required]],
      seccionId: ['', [Validators.required]],
      periodoAcademicoId: ['', [Validators.required]] // Eliminamos el valor estático '1'
    });
  }

  ngOnInit() {
    // Carga de Cursos
    this.cursoService.listar().subscribe(data => this.cursos.set(data));

    // Carga de Secciones
    this.seccionService.listar().subscribe(data => this.secciones.set(data));

    //Carga de Periodos con lógica de autoselección
    this.periodoService.listar().subscribe(data => {
      this.periodos.set(data);

      // Si es una creación (no hay clase seleccionada), buscamos el periodo ACTIVO
      if (!this.clase && data.length > 0) {
        const activo = data.find(p => p.estado === 'ACTIVO');
        if (activo) {
          this.form.patchValue({ periodoAcademicoId: activo.id });
        }
      }
    });

    // 4. Carga de datos si es edición
    if (this.clase) {
      this.form.patchValue(this.clase);
    }
  }

  seccionesAgrupadas = computed(() => {
    const data = this.secciones();
    const grupos: { nivel: string, secciones: any[] }[] = [];

    data.forEach(s => {
      let grupo = grupos.find(g => g.nivel === s.nivelNombre);
      if (!grupo) {
        grupo = { nivel: s.nivelNombre, secciones: [] };
        grupos.push(grupo);
      }
      grupo.secciones.push(s);
    });

    return grupos;
  });

  guardar() {
    if (this.form.invalid) return;

    const request = this.form.value;
    const obs = this.clase
      ? this.claseService.actualizar(this.clase.id, request)
      : this.claseService.crear(request);

    obs.subscribe({
      next: () => {
        this.onSave.emit();
        this.onClose.emit();
      },
      error: (err) => {
        console.error("Error al procesar clase", err);
        alert(err.error?.message || "Error al guardar la clase");
      }
    });
  }
}