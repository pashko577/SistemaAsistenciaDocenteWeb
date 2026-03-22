import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { CronogramaAdministrativoRequest } from '../../../../../core/models/Administrativos/cronogramaadministrativo-request';
import { CronogramaAdministrativoResponse } from '../../../../../core/models/Administrativos/cronogramaadministrativo-response';
import { DiaSemana } from '../../../../../core/models/enums/dia-semana';
import { CronogramaAdministrativoService } from '../../../../../core/services/cronograma-administrativo';


@Component({
  selector: 'app-cronograma-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './cronograma-list.html'
})
export class CronogramaList {
  cronogramas: CronogramaAdministrativoResponse[] = [];
  
  // Usamos el Enum para inicializar
  nuevo: CronogramaAdministrativoRequest = {
    administrativoId: 0,
    diaSemana: DiaSemana.LUNES,
    horaEntrada: '08:00',
    horaSalida: '17:00'
  };

  // Para el combo de días en el HTML
  listaDias = Object.values(DiaSemana);

  constructor(private service: CronogramaAdministrativoService) {}

  // Buscar cronogramas cuando se cambia el ID
  buscarCronogramas() {
    if (this.nuevo.administrativoId > 0) {
      this.service.listarPorAdministrativo(this.nuevo.administrativoId).subscribe(data => {
        this.cronogramas = data;
      });
    }
  }

  guardar() {
    this.service.crear(this.nuevo).subscribe({
      next: () => {
        alert('Turno registrado correctamente');
        this.buscarCronogramas(); // Refrescar tabla
      },
      error: (err) => alert(err.error?.message || 'Error al guardar')
    });
  }

  eliminar(id: number) {
    if(confirm('¿Seguro que desea eliminar este turno?')) {
      this.service.eliminar(id).subscribe(() => this.buscarCronogramas());
    }
  }
}