import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IndexAdministrativo } from './index-administrativo';

describe('IndexAdministrativo', () => {
  let component: IndexAdministrativo;
  let fixture: ComponentFixture<IndexAdministrativo>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IndexAdministrativo]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IndexAdministrativo);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
