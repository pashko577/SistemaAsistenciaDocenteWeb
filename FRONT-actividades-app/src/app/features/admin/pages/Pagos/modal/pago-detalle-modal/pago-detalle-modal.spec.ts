import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PagoDetalleModal } from './pago-detalle-modal';

describe('PagoDetalleModal', () => {
  let component: PagoDetalleModal;
  let fixture: ComponentFixture<PagoDetalleModal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PagoDetalleModal]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PagoDetalleModal);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
