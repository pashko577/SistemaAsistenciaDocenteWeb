import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PagosService } from '../../../../../core/services/pagos_services';
import { PagoResponse } from '../../../../../core/models/pagos/pago-response';

@Component({
  selector: 'app-pago-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './pago-list.html',
  styleUrl: './pago-list.css',
})
export class PagoList implements OnInit {
  listaPagos: PagoResponse[] = [];
  busqueda: string = '';
  cargando: boolean = false;

  // --- VARIABLES FALTANTES ---
  mesActual: string = 'ABRIL'; // Puedes hacerlo dinámico si gustas
  anioActual: number = 2026;

  constructor(private pagosService: PagosService) {}

  ngOnInit(): void {
    this.cargarPagos();
  }

  cargarPagos(): void {
    this.cargando = true;
    
    // Como tu Backend NO tiene "listarTodos", usamos el rango del año
    const inicio = `${this.anioActual}-01-01`;
    const fin = `${this.anioActual}-12-31`;

    this.pagosService.listarPorRango(inicio, fin).subscribe({
      next: (data) => {
        // Ordenamos por ID para que los últimos salgan primero
        this.listaPagos = data.sort((a, b) => b.pagoId - a.pagoId);
        this.cargando = false;
      },
      error: (err) => {
        console.error('Error al cargar historial:', err);
        this.cargando = false;
        this.listaPagos = [];
      }
    });
  }

  get pagosFiltrados() {
    if (!this.busqueda.trim()) {
      return this.listaPagos;
    }

    const filtro = this.busqueda.toLowerCase();

    return this.listaPagos.filter(pago => 
      pago.nombreCompleto.toLowerCase().includes(filtro) ||
      pago.dni.includes(filtro) ||
      pago.cargo.toLowerCase().includes(filtro) ||
      pago.sede.toLowerCase().includes(filtro)
    );
  }

  verDetalle(pago: PagoResponse) {
    // Aquí puedes implementar la lógica para abrir el modal de boleta
    console.log("Mostrando detalle del pago ID:", pago.pagoId);
  }
}