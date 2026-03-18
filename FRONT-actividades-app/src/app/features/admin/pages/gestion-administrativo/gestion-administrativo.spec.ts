import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GestionAdministrativo } from './gestion-administrativo';

describe('GestionAdministrativo', () => {
  let component: GestionAdministrativo;
  let fixture: ComponentFixture<GestionAdministrativo>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GestionAdministrativo]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GestionAdministrativo);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
