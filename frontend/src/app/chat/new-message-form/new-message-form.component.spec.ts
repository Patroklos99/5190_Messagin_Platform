import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewMessageFormComponent } from './new-message-form.component';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';

describe('NewMessageFormComponent', () => {
  let component: NewMessageFormComponent;
  let fixture: ComponentFixture<NewMessageFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NewMessageFormComponent ],
      imports:[ReactiveFormsModule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NewMessageFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
