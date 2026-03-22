import { Component, OnInit } from '@angular/core'; // Añadimos OnInit
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { CronogramaAdministrativoRequest } from '../../../../../core/models/Administrativos/cronogramaadministrativo-request';
import { CronogramaAdministrativoResponse } from '../../../../../core/models/Administrativos/cronogramaadministrativo-response';
import { DiaSemana } from '../../../../../core/models/enums/dia-semana';
import { CronogramaAdministrativoService } from '../../../../../core/services/cronograma-administrativo';
// IMPORTANTE: Importa tu servicio de administrativos y el modelo de respuesta

import { AdministrativoResponse } from '../../../../../core/models/Administrativos/administrativo-response';
import { AdministrativoService } from '../../../../../core/services/administrativo_services';

@Component({
  selector: 'app-cronograma-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './cronograma-list.html'
})
export class CronogramaList implements OnInit { // Implementamos OnInit
  cronogramas: CronogramaAdministrativoResponse[] = [];
  
  // NUEVAS PROPIEDADES PARA EL BUSCADOR
  administrativos: AdministrativoResponse[] = [];
  nombreBusqueda: string = ''; 

  nuevo: CronogramaAdministrativoRequest = {
    administrativoId: 0,
    diaSemana: DiaSemana.LUNES,
    horaEntrada: '08:00',
    horaSalida: '17:00'
  };

  listaDias = Object.values(DiaSemana);

  constructor(
    private service: CronogramaAdministrativoService,
    private adminService: AdministrativoService // Inyectamos el servicio
  ) {}

  ngOnInit() {
    this.cargarAdministrativos();
  }

  // Carga la lista inicial para el Datalist
  cargarAdministrativos() {
    this.adminService.listar().subscribe({
      next: (data) => this.administrativos = data,
      error: () => console.error('Error al cargar personal')
    });
  }

  // Se ejecuta cuando el usuario selecciona o escribe en el buscador
  onAdminChange() {
    const encontrado = this.administrativos.find(
      a => `${a.nombres} ${a.apellidos}` === this.nombreBusqueda
    );

    if (encontrado) {
      this.nuevo.administrativoId = encontrado.id;
      this.buscarCronogramas();
    } else {
      this.nuevo.administrativoId = 0;
      this.cronogramas = []; // Limpiamos la tabla si no hay selección válida
    }
  }

  buscarCronogramas() {
    if (this.nuevo.administrativoId > 0) {
      this.service.listarPorAdministrativo(this.nuevo.administrativoId).subscribe(data => {
        this.cronogramas = data;
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
        this.buscarCronogramas();
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