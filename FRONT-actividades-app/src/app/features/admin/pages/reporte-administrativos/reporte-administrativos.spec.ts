import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReporteAdministrativos } from './reporte-administrativos';

describe('ReporteAdministrativos', () => {
  let component: ReporteAdministrativos;
  let fixture: ComponentFixture<ReporteAdministrativos>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReporteAdministrativos]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReporteAdministrativos);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
