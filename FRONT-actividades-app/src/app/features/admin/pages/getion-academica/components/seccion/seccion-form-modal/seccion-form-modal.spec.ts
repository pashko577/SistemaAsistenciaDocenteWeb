import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SeccionFormModal } from './seccion-form-modal';

describe('SeccionFormModal', () => {
  let component: SeccionFormModal;
  let fixture: ComponentFixture<SeccionFormModal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SeccionFormModal]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SeccionFormModal);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
