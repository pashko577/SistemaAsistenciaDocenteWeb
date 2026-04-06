import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule, registerLocaleData } from '@angular/common';
import { PagoResponse } from '../../../../../../core/models/pagos/pago-response';
import localeEs from '@angular/common/locales/es';



registerLocaleData(localeEs, 'es');
registerLocaleData(localeEs, 'es-PE');
@Component({
  selector: 'app-pago-detalle-modal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './pago-detalle-modal.html',
  styleUrl: './pago-detalle-modal.css'
})
export class PagoDetalleModal implements OnInit {
  @Input() pago: PagoResponse | null = null;
  @Input() visible: boolean = true; // Por defecto true para que funcione con *ngIf
  @Output() close = new EventEmitter<void>();


  today: Date = new Date();
  ngOnInit() {
    // Si el componente se crea via *ngIf, nos aseguramos que sea visible
    this.visible = true;
    this.today = new Date();
  }

  cerrar() {
    this.visible = false;
    this.close.emit();
  }

imprimir() {
  const contenido = document.getElementById('boleta-final')?.innerHTML;
  
  // Creamos una ventana nueva
  const ventanaImpresion = window.open('', '_blank', 'height=600,width=800');

  if (ventanaImpresion && contenido) {
    ventanaImpresion.document.write(`
      <html>
        <head>
          <title>Boleta de Pago - James Baldwin</title>
          <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css">
          <style>
            /* Forzamos que los colores aparezcan en la impresión */
            .bg-amarillo-institucional { 
              background-color: #ffff00 !important; 
              -webkit-print-color-adjust: exact; 
              print-color-adjust: exact;
            }
            .bg-gris-claro { 
              background-color: #f3f4f6 !important; 
              -webkit-print-color-adjust: exact; 
              print-color-adjust: exact;
            }
            table { border-collapse: collapse; width: 100%; }
            th, td { border: 1px solid black !important; }
            body { font-family: sans-serif; padding: 0; margin: 0; }
            
            /* Ajuste para que la boleta ocupe bien el espacio */
            #boleta-final { 
              box-shadow: none !important; 
              margin: 0 auto !important; 
              padding: 20px !important;
            }
          </style>
        </head>
        <body>
          <div class="p-4">
            ${contenido}
          </div>
        </body>
      </html>
    `);

    ventanaImpresion.document.close();
    ventanaImpresion.focus();
    
    // CRÍTICO: Esperar a que los estilos y fuentes carguen antes de imprimir
    // Si se imprime muy rápido, sale en blanco.
    setTimeout(() => {
      ventanaImpresion.print();
      ventanaImpresion.close();
    }, 800); // Subimos a 800ms para asegurar carga en redes lentas
  }
}
}