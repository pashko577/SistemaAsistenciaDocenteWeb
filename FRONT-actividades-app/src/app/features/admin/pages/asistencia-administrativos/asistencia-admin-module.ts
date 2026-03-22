import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

// Importa los componentes standalone
import { AsistenciaRegistro } from './asistencia-registro/asistencia-registro';
import { CronogramaList } from './cronograma-list/cronograma-list';

@NgModule({
  declarations: [
    // VACÍO: Los componentes standalone no se declaran aquí
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    // IMPORTADOS: Aquí es donde deben ir ahora
    AsistenciaRegistro, 
    CronogramaList
  ],
  exports: [
    AsistenciaRegistro,
    CronogramaList
  ]
})
export class AsistenciaAdminModule { }