import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GestionContrato } from './gestion-contrato';

describe('GestionContrato', () => {
  let component: GestionContrato;
  let fixture: ComponentFixture<GestionContrato>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GestionContrato]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GestionContrato);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
