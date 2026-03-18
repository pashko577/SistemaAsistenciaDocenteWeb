import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';

@Component({
  selector: 'app-search-administrativo',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatIconModule],
  templateUrl: './search-adminsitrativo.html'
})
export class SearchAdministrativo implements OnInit {
  @Input() sedes: any[] = [];
  @Input() cargos: any[] = [];
  @Output() onFilter = new EventEmitter<any>();

  filtroForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.filtroForm = this.fb.group({
      busqueda: [''],
      sedeId: [null],
      cargoId: [null],
      estado: ['']
    });
  }

  ngOnInit(): void {
    // Detecta cambios automáticamente con un pequeño retraso para no saturar
    this.filtroForm.valueChanges
      .pipe(
        debounceTime(300), 
        distinctUntilChanged()
      )
      .subscribe(val => {
        this.onFilter.emit(val);
      });
  }

  // Este es el método que llamamos desde el botón de la escoba
  limpiar() {
    this.filtroForm.reset({
      busqueda: '',
      sedeId: null,
      cargoId: null,
      estado: null
    });
    // Al resetear, valueChanges se dispara solo y emite el filtro vacío al padre
  }
}