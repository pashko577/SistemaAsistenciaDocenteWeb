import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NivelList } from './nivel-list';

describe('NivelList', () => {
  let component: NivelList;
  let fixture: ComponentFixture<NivelList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NivelList]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NivelList);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
