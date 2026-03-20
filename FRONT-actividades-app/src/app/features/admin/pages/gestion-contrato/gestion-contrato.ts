import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { ContratoFormComponent } from './modal Contrato/contrato-form';
import { ContratoResponse } from '../../../../core/models/Contratos/contrato-response';
import { ContratoService } from '../../../../core/services/contrato_services';


@Component({
  selector: 'app-gestion-contrato',
  standalone: true,
  imports: [CommonModule, MatDialogModule, MatIconModule, ContratoFormComponent],
  templateUrl: './gestion-contrato.html'
})
export class GestionContrato implements OnInit {
  contratos: ContratoResponse[] = [];
  loading = false;

  constructor(
    private dialog: MatDialog,
    private contratoService: ContratoService, // Inyectamos el servicio
    private cdr: ChangeDetectorRef // 2. Inyectar en el constructor
  ) {}

  ngOnInit(): void {
    this.cargarContratos();
  }

  cargarContratos() {
    this.loading = true;
    this.contratoService.listar().subscribe({
      next: (res) => {
        this.contratos = [...res]; // 3. Usar el operador spread para crear una nueva referencia
        this.loading = false;
        this.cdr.detectChanges(); // 4. Forzar la detección de cambios
      },
      error: (err) => {
        console.error('Error al cargar contratos', err);
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }
  abrirFormulario(contrato?: ContratoResponse) {
    const dialogRef = this.dialog.open(ContratoFormComponent, {
      width: '650px',
      // Eliminamos el panelClass si no tienes estilos globales, 
      // pero el diseño de Tailwind que te pasé se verá genial.
      data: { contratoSelected: contrato }
    });

    dialogRef.afterClosed().subscribe(result => {
      // Si el modal devolvió "true" (porque guardó con éxito), recargamos la lista
      if (result) {
        this.cargarContratos();
      }
    });
  }

  eliminarContrato(id: number) {
    if (confirm('¿Estás seguro de eliminar este contrato?')) {
      this.contratoService.eliminar(id).subscribe(() => {
        this.cargarContratos();
      });
    }
  }

  get contratosActivos(): number {
  return this.contratos.filter(c => c.estado === 'ACTIVO').length;
}

get totalInversion(): number {
  return this.contratos.reduce((acc, c) => acc + c.montoBase, 0);
}



}