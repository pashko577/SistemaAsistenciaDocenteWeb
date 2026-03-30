import { Component, OnInit, ChangeDetectorRef } from '@angular/core'; // Inyectamos CDR
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { CronogramaAdministrativoRequest } from '../../../../../core/models/Administrativos/cronogramaadministrativo-request';
import { CronogramaAdministrativoResponse } from '../../../../../core/models/Administrativos/cronogramaadministrativo-response';
import { DiaSemana } from '../../../../../core/models/enums/dia-semana';
import { CronogramaAdministrativoService } from '../../../../../core/services/cronograma-administrativo';
import { AdministrativoResponse } from '../../../../../core/models/Administrativos/administrativo-response';
import { AdministrativoService } from '../../../../../core/services/administrativo_services';
import { MatIcon } from "@angular/material/icon";

@Component({
  selector: 'app-cronograma-list',
  standalone: true,
  imports: [CommonModule, FormsModule, MatIcon],
  templateUrl: './cronograma-list.html'
})
export class CronogramaList implements OnInit {
  cronogramas: CronogramaAdministrativoResponse[] = [];
  administrativos: AdministrativoResponse[] = [];
  nombreBusqueda: string = ''; 
  cargando: boolean = false; // Propiedad necesaria para el spinner

  nuevo: CronogramaAdministrativoRequest = {
    administrativoId: 0,
    diaSemana: DiaSemana.LUNES,
    horaEntrada: '08:00',
    horaSalida: '17:00'
  };

  listaDias = Object.values(DiaSemana);

  constructor(
    private service: CronogramaAdministrativoService,
    private adminService: AdministrativoService,
    private cdr: ChangeDetectorRef // Inyectamos el detector de cambios
  ) {}

  ngOnInit() {
    this.cargarAdministrativos();
  }

  cargarAdministrativos() {
    this.adminService.listar().subscribe({
      next: (data) => {
        this.administrativos = data;
        this.cdr.detectChanges(); // Asegura que el datalist se llene de inmediato
      },
      error: () => console.error('Error al cargar personal')
    });
  }

  onAdminChange() {
    const encontrado = this.administrativos.find(
      a => `${a.nombres} ${a.apellidos}` === this.nombreBusqueda
    );

    if (encontrado) {
      this.nuevo.administrativoId = encontrado.id;
      this.buscarCronogramas();
    } else {
      this.nuevo.administrativoId = 0;
      this.cronogramas = [];
      this.cdr.detectChanges();
    }
  }

  buscarCronogramas() {
    if (this.nuevo.administrativoId > 0) {
      this.cargando = true;
      this.service.listarPorAdministrativo(this.nuevo.administrativoId).subscribe({
        next: (data) => {
          this.cronogramas = data;
          this.cargando = false;
          this.cdr.detectChanges(); // <--- Aquí eliminamos el "lag" visual
        },
        error: () => {
          this.cargando = false;
          this.cdr.detectChanges();
        }
      });
    }
  }

  guardar() {
    if (this.nuevo.administrativoId === 0) {
      alert('Por favor, seleccione un administrativo válido de la lista.');
      return;
    }

    this.service.crear(this.nuevo).subscribe({
      next: () => {
        alert('Turno registrado correctamente');
        this.buscarCronogramas(); // Refresca y detecta cambios
      },
      error: (err) => alert(err.error?.message || 'Error al guardar')
    });
  }

  eliminar(id: number) {
    if(confirm('¿Seguro que desea eliminar este turno?')) {
      this.service.eliminar(id).subscribe(() => {
        this.buscarCronogramas();
      });
    }
  }
}