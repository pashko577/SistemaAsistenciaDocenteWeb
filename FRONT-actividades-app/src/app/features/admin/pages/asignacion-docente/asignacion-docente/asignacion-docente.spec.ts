import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AsignacionDocente } from './asignacion-docente';

describe('AsignacionDocente', () => {
  let component: AsignacionDocente;
  let fixture: ComponentFixture<AsignacionDocente>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AsignacionDocente]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AsignacionDocente);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
