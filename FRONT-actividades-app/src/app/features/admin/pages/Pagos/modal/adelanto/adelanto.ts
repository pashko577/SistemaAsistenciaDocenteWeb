import { Component, Input, Output, EventEmitter, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AdelantoService } from '../../../../../../core/services/adelanto_services';
import { AdelantoResponse } from '../../../../../../core/models/pagos/adelanto-response';
import { AdelantoRequest } from '../../../../../../core/models/pagos/adelanto-request';
import { AdministrativoResponse } from '../../../../../../core/models/Administrativos/administrativo-response';

@Component({
  selector: 'app-adelanto',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './adelanto.html'
})
export class Adelanto implements OnChanges {
  @Input() usuarioId: number | null = null;
  @Input() mes: string = '';
  @Input() anio: number = 2026;
  @Input() listaAdministrativos: AdministrativoResponse[] = [];

  @Output() cambioAdelantos = new EventEmitter<{ lista: AdelantoResponse[], total: number }>();
  @Output() usuarioCambiado = new EventEmitter<number>(); // Para sincronizar con el padre

  adelantoForm: FormGroup;
  adelantosPendientes: AdelantoResponse[] = [];
  totalAdelantos: number = 0;
  cargando = false;

  constructor(private fb: FormBuilder, private adelantoService: AdelantoService) {
    this.adelantoForm = this.fb.group({
      nombre: ['', [Validators.required]],
      monto: [null, [Validators.required, Validators.min(1)]]
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    // Si cambia cualquiera de estos valores, recargamos la lista
    if (changes['usuarioId'] || changes['mes'] || changes['anio']) {
      this.cargarAdelantos();
    }
  }

// En adelanto.ts -> cargarAdelantos()
cargarAdelantos(): void {
  // Reset inmediato para limpiar la UI
  this.adelantosPendientes = [];
  this.totalAdelantos = 0;
  this.cambioAdelantos.emit({ lista: [], total: 0 }); // Avisar al padre que limpie

  if (!this.usuarioId) return;

  this.adelantoService.listarPendientesPorUsuario(this.usuarioId).subscribe({
    next: (data) => {
      const mesNumBusqueda = this.getMesNum(this.mes);
      
      const filtrados = data.filter(a => {
        if (!a.fechaCreacion) return false;
        // Split por '-' es la forma más segura en Perú (UTC-5)
        const partes = a.fechaCreacion.split('-');
        const anioReg = parseInt(partes[0]);
        const mesReg = parseInt(partes[1]);
        return mesReg === mesNumBusqueda && anioReg === this.anio;
      });

      this.adelantosPendientes = filtrados;
      this.totalAdelantos = filtrados.reduce((sum, item) => sum + item.monto, 0);
      
      // Emitir al padre
      this.cambioAdelantos.emit({ 
        lista: this.adelantosPendientes, 
        total: this.totalAdelantos 
      });
    }
  });
}
guardar(): void {
  if (this.adelantoForm.invalid || !this.usuarioId) return;
  this.cargando = true;

  // 1. Forzamos el formato manual. No dejamos que JS decida.
  const mesNum = this.getMesNum(this.mes);
  const mesString = mesNum.toString().padStart(2, '0');
  
  // Usamos el primer día del mes para los adelantos, o el día actual
  // Pero SIEMPRE como string: "2026-04-05"
  const hoy = new Date();
  const diaActual = hoy.getDate().toString().padStart(2, '0');
  const fechaFormateada = `${this.anio}-${mesString}-${diaActual}`;

  const request: AdelantoRequest = {
    nombre: this.adelantoForm.value.nombre,
    monto: this.adelantoForm.value.monto,
    usuarioId: this.usuarioId,
    fechaCreacion: fechaFormateada // Esto envía "2026-04-05" literal
  };

  this.adelantoService.registrar(request).subscribe({
    next: () => {
      this.adelantoForm.reset({ monto: null, nombre: '' });
      this.cargando = false;
      this.cargarAdelantos();
    },
    error: () => this.cargando = false
  });
}
  anular(id: number): void {
    if (confirm('¿Está seguro de anular este adelanto?')) {
      this.adelantoService.anular(id).subscribe(() => this.cargarAdelantos());
    }
  }

  private getMesNum(nombre: string): number {
    const meses: any = { 
      'ENERO': 1, 'FEBRERO': 2, 'MARZO': 3, 'ABRIL': 4, 
      'MAYO': 5, 'JUNIO': 6, 'JULIO': 7, 'AGOSTO': 8, 
      'SEPTIEMBRE': 9, 'OCTUBRE': 10, 'NOVIEMBRE': 11, 'DICIEMBRE': 12 
    };
    // Convertimos a mayúsculas por si acaso viene 'Abril'
    return meses[nombre.toUpperCase()] || 1;
  }
}