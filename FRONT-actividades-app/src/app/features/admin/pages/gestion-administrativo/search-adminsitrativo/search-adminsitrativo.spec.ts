import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SearchAdministrativo } from './search-adminsitrativo';


describe('SearchAdminsitrativo', () => {
  let component: SearchAdministrativo;
  let fixture: ComponentFixture<SearchAdministrativo>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SearchAdministrativo]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SearchAdministrativo);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
