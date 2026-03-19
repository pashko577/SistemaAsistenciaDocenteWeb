import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';

@Component({
  selector: 'app-search-docente',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatIconModule],
  templateUrl: './search-docente.html'
})
export class SearchDocente implements OnInit {
  @Input() sedes: any[] = [];
  @Input() especialidades: any[] = []; // Recibe especialidades en lugar de cargos
  @Output() onFilter = new EventEmitter<any>();

  filtroForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.filtroForm = this.fb.group({
      busqueda: [''],
      sedeId: [null],
      especialidadId: [null], // Coincide con el nombre en el HTML
      estado: [null]
    });
  }

  ngOnInit(): void {
    // Escucha cambios y emite al padre (GestionDocente)
    this.filtroForm.valueChanges
      .pipe(
        debounceTime(300), 
        distinctUntilChanged()
      )
      .subscribe(val => {
        this.onFilter.emit(val);
      });
  }

  limpiar() {
    this.filtroForm.reset({
      busqueda: '',
      sedeId: null,
      especialidadId: null,
      estado: null
    });
  }
}