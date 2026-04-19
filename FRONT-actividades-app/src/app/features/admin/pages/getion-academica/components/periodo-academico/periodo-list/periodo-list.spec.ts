import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PeriodoList } from './periodo-list';

describe('PeriodoList', () => {
  let component: PeriodoList;
  let fixture: ComponentFixture<PeriodoList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PeriodoList]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PeriodoList);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
