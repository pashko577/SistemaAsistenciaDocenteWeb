import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CronogramaDocenteList } from './cronograma-docente-list';

describe('CronogramaDocenteList', () => {
  let component: CronogramaDocenteList;
  let fixture: ComponentFixture<CronogramaDocenteList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CronogramaDocenteList]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CronogramaDocenteList);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
