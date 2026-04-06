import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Adelanto } from './adelanto';

describe('Adelanto', () => {
  let component: Adelanto;
  let fixture: ComponentFixture<Adelanto>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Adelanto]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Adelanto);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
