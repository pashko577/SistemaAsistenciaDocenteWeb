import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClaseFormModalComponent } from './clase-form-modal';

describe('ClaseFormModal', () => {
  let component: ClaseFormModalComponent;
  let fixture: ComponentFixture<ClaseFormModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClaseFormModalComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ClaseFormModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
