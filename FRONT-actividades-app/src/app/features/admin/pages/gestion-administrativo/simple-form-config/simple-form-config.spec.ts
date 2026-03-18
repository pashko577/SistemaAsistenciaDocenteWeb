import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SimpleFormConfig } from './simple-form-config';

describe('SimpleFormConfig', () => {
  let component: SimpleFormConfig;
  let fixture: ComponentFixture<SimpleFormConfig>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SimpleFormConfig]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SimpleFormConfig);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
