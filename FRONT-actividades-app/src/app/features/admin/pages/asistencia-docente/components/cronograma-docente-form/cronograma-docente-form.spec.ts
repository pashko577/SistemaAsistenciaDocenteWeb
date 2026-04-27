import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CronogramaDocenteForm } from './cronograma-docente-form';

describe('CronogramaDocenteForm', () => {
  let component: CronogramaDocenteForm;
  let fixture: ComponentFixture<CronogramaDocenteForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CronogramaDocenteForm]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CronogramaDocenteForm);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
