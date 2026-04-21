import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AsignacionListComponent } from './asignacion-list';

describe('AsignacionList', () => {
  let component: AsignacionListComponent;
  let fixture: ComponentFixture<AsignacionListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AsignacionListComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(AsignacionListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
