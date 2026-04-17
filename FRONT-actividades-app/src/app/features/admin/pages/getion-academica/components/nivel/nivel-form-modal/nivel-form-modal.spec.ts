import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NivelFormModal } from './nivel-form-modal';

describe('NivelFormModal', () => {
  let component: NivelFormModal;
  let fixture: ComponentFixture<NivelFormModal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NivelFormModal]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NivelFormModal);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
