import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IndexUser } from './index-user';

describe('IndexUser', () => {
  let component: IndexUser;
  let fixture: ComponentFixture<IndexUser>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IndexUser]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IndexUser);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
