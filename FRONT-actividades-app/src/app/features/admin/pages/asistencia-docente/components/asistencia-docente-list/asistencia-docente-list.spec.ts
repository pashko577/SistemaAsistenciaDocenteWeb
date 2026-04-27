import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AsistenciaDocenteList } from './asistencia-docente-list';

describe('AsistenciaDocenteList', () => {
  let component: AsistenciaDocenteList;
  let fixture: ComponentFixture<AsistenciaDocenteList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AsistenciaDocenteList]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AsistenciaDocenteList);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
