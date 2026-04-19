import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PeriodoFormModal } from './periodo-form-modal';

describe('PeriodoFormModal', () => {
  let component: PeriodoFormModal;
  let fixture: ComponentFixture<PeriodoFormModal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PeriodoFormModal]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PeriodoFormModal);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
