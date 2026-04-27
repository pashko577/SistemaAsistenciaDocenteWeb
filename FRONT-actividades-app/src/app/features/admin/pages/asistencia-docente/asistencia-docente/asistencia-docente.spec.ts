import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AsistenciaDocente } from './asistencia-docente';

describe('AsistenciaDocente', () => {
  let component: AsistenciaDocente;
  let fixture: ComponentFixture<AsistenciaDocente>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AsistenciaDocente]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AsistenciaDocente);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
