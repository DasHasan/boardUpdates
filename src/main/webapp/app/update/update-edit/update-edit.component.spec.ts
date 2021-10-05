import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateEditComponent } from './update-edit.component';

describe('UpdateEditComponent', () => {
  let component: UpdateEditComponent;
  let fixture: ComponentFixture<UpdateEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UpdateEditComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UpdateEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
