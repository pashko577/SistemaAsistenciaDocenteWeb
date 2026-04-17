import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GradoFormModal } from './grado-form-modal';

describe('GradoFormModal', () => {
  let component: GradoFormModal;
  let fixture: ComponentFixture<GradoFormModal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GradoFormModal]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GradoFormModal);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
