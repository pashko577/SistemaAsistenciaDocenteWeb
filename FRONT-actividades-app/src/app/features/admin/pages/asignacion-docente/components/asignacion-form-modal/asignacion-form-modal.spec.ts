import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AsignacionFormModal } from './asignacion-form-modal';

describe('AsignacionFormModal', () => {
  let component: AsignacionFormModal;
  let fixture: ComponentFixture<AsignacionFormModal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AsignacionFormModal]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AsignacionFormModal);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
