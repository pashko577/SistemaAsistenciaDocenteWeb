import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AsignacionList } from './asignacion-list';

describe('AsignacionList', () => {
  let component: AsignacionList;
  let fixture: ComponentFixture<AsignacionList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AsignacionList]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AsignacionList);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
