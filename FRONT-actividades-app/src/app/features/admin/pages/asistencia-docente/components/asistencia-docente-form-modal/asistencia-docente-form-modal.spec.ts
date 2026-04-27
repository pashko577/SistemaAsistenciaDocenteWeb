import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AsistenciaDocenteFormModal } from './asistencia-docente-form-modal';

describe('AsistenciaDocenteFormModal', () => {
  let component: AsistenciaDocenteFormModal;
  let fixture: ComponentFixture<AsistenciaDocenteFormModal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AsistenciaDocenteFormModal]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AsistenciaDocenteFormModal);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
