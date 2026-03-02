import { CommonModule } from '@angular/common';
import { Component, computed, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Auth } from '../../../../core/services/auth';
import { DocenteRequest } from '../../../../core/models/docente-request';
import { Docente } from '../../../../core/services/docente';
import { SedeResponse } from '../../../../core/models/sede-response';
import { TipoDocumentoResponse } from '../../../../core/models/tipo-documento-response';
import { EspecialidadDocenteResponse } from '../../../../core/models/especialidad-docente-response';
import { EspecialidadDocenteService } from '../../../../core/services/especialidad-docente';
import { TipoDocumentoService } from '../../../../core/services/tipo-documento';
import { SedeService } from '../../../../core/services/sede-service';
import { DocenteResponse } from '../../../../core/models/docente-response';

@Component({
  selector: 'app-gestion-docente',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './gestion-docente.html',
  styleUrl: './gestion-docente.css',
})
export class GestionDocente implements OnInit {
  docenteForm!: FormGroup;
  loading = false;
  successMessage = '';
  backendError = signal<string | null>(null);
  isModalOpen = signal(false);

  sedes: any[] = [];
  tiposDocumento: any[] = [];
  especialidades: any[] = [];
  docentes = signal<DocenteResponse[]>([]);
  loadingTable = signal(false);
  searchTerm = signal('');

  constructor(
    private fb: FormBuilder,
    private docenteService: Docente,
    private sedeService: SedeService,
    private especialidadService: EspecialidadDocenteService,
    private tipoDocumentoService: TipoDocumentoService
  ) {
    this.createForm();
  }

  ngOnInit(): void {
    this.loadDocentes();
    this.loadCombos();
  }

  private loadCombos(): void {
    this.sedeService.listarSedes().subscribe(data => this.sedes = data);
    this.tipoDocumentoService.listar().subscribe(data => this.tiposDocumento = data);
    this.especialidadService.listar().subscribe(data => this.especialidades = data);
  }

  private createForm(): void {
    this.docenteForm = this.fb.group({
      dni: ['', [Validators.required]],
      password: ['', [Validators.required]],
      sedeId: [null, Validators.required],
      nombres: ['', Validators.required],
      apellidos: ['', Validators.required],
      celular: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      direccion: ['', Validators.required],
      tipoDocumentoId: [null, Validators.required],
      especialidadId: [null, Validators.required],
      observaciones: ['', Validators.required]
    });
  }

  registerDocente(): void {
    if (this.docenteForm.invalid) return;

    this.loading = true;
    this.backendError.set(null);

    const rawValue = this.docenteForm.value;
    const request = {
      ...rawValue,
      celular: String(rawValue.celular),
      estado: 'NUEVO'
    };

    this.docenteService.registrarDocente(request).subscribe({
      next: () => {
        this.successMessage = 'Registrado con éxito';
        this.docenteForm.reset();
        this.loading = false;

        this.loadDocentes();
        // Opcional: Cerrar el modal después de un breve momento
        setTimeout(() => {
          this.toggleModal();
        }, 1500);
      },
      error: (err) => {
        this.loading = false;

        // 1. Intentamos obtener el mensaje del JSON { "message": "..." }
        // 2. Si el back envió un String simple (porque no actualizaste el Handler), usamos err.error
        // 3. Si no hay nada, un mensaje genérico
        const errorMsg = err.error?.message || (typeof err.error === 'string' ? err.error : 'Error al registrar docente');

        this.backendError.set(errorMsg);
        console.error('Detalle del error:', err);
      }
    });
  }

  toggleModal() {
    this.isModalOpen.update(v => !v);
    if (!this.isModalOpen()) {
      this.docenteForm.reset();
      this.backendError.set(null);
      this.successMessage = '';
    }
  }

  loadDocentes(): void {
    this.loadingTable.set(true);
    this.docenteService.listarDocentes().subscribe({
      next: (data) => {
        this.docentes.set(data);
        this.loadingTable.set(false);
      },
      error: (err) => {
        console.error('Error cargando docentes', err);
        this.loadingTable.set(false);
      }
    });
  }

  filteredDocentes = computed(() => {
    const term = this.searchTerm().toLowerCase().trim();
    if (!term) return this.docentes();

    return this.docentes().filter(doc =>
      doc.nombres.toLowerCase().includes(term) ||
      doc.apellidos.toLowerCase().includes(term) ||
      doc.dni.includes(term)
    );
  });

  onSearch(event: Event) {
    const element = event.target as HTMLInputElement;
    this.searchTerm.set(element.value);
  }
}
