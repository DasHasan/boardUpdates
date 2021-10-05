import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateListComponent } from './update-list.component';

describe('UpdateListComponent', () => {
  let component: UpdateListComponent;
  let fixture: ComponentFixture<UpdateListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UpdateListComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UpdateListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
