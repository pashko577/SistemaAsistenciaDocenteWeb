import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClaseList } from '../../periodo-academico/clase-list';

describe('ClaseList', () => {
  let component: ClaseList;
  let fixture: ComponentFixture<ClaseList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClaseList]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ClaseList);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
