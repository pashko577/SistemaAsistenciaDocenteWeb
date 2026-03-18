import { TestBed } from '@angular/core/testing';
import { EspecialidadDocenteService } from './especialidad-docente';



describe('EspecialidadDocente', () => {
  let service: EspecialidadDocenteService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EspecialidadDocenteService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
