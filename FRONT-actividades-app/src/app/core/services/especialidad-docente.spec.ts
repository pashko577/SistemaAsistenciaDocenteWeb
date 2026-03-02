import { TestBed } from '@angular/core/testing';

import { EspecialidadDocente } from './especialidad-docente';

describe('EspecialidadDocente', () => {
  let service: EspecialidadDocente;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EspecialidadDocente);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
