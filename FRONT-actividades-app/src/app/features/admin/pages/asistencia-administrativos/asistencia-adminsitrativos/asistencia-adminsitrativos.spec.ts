import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AsistenciaAdminsitrativos } from './asistencia-adminsitrativos';

describe('AsistenciaAdminsitrativos', () => {
  let component: AsistenciaAdminsitrativos;
  let fixture: ComponentFixture<AsistenciaAdminsitrativos>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AsistenciaAdminsitrativos]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AsistenciaAdminsitrativos);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
