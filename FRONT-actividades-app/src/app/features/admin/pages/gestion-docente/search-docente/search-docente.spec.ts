import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchDocente } from './search-docente';

describe('SearchDocente', () => {
  let component: SearchDocente;
  let fixture: ComponentFixture<SearchDocente>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SearchDocente]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SearchDocente);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
