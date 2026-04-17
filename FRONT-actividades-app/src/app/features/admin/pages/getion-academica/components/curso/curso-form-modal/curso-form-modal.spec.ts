import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CursoFormModalComponent } from './curso-form-modal';

describe('CursoFormModal', () => {
  let component: CursoFormModalComponent;
  let fixture: ComponentFixture<CursoFormModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CursoFormModalComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(CursoFormModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
