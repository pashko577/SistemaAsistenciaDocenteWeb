import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IndexDocente } from './index-docente';

describe('IndexDocente', () => {
  let component: IndexDocente;
  let fixture: ComponentFixture<IndexDocente>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IndexDocente]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IndexDocente);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
