import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AsistenciaAdministrativosMain } from './asistencia-adminsitrativos';

describe('AsistenciaAdminsitrativos', () => {
  let component: AsistenciaAdministrativosMain;
  let fixture: ComponentFixture<AsistenciaAdministrativosMain>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AsistenciaAdministrativosMain]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AsistenciaAdministrativosMain);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
