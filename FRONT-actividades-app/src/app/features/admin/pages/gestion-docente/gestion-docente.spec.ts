import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GestionDocente } from './gestion-docente';

describe('GestionDocente', () => {
  let component: GestionDocente;
  let fixture: ComponentFixture<GestionDocente>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GestionDocente]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GestionDocente);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
