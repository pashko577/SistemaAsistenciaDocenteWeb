import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PagoResponse } from '../../../../../../core/models/pagos/pago-response';


@Component({
  selector: 'app-pago-detalle-modal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './pago-detalle-modal.html',
  styleUrl: './pago-detalle-modal.css'
})
export class PagoDetalleModal {
  @Input() pago: PagoResponse | null = null;
  @Input() visible: boolean = false;
  @Output() close = new EventEmitter<void>();

  cerrar() {
    this.visible = false;
    this.close.emit();
  }

  imprimir() {
    window.print();
  }
}