import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { TestHelper } from '../../test/test-helper';

import { LoginFormComponent } from './login-form.component';

describe('LoginFormComponent', () => {
  let component: LoginFormComponent;
  let fixture: ComponentFixture<LoginFormComponent>;
  let testHelper: TestHelper<LoginFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginFormComponent],
      imports: [ReactiveFormsModule],
    }).compileComponents();

    fixture = TestBed.createComponent(LoginFormComponent);
    component = fixture.componentInstance;
    testHelper = new TestHelper(fixture);
    fixture.detectChanges();
  });

  it('should emit username and password', () => {
    let username: string;
    let password: string;

    // On s'abonne à l'EventEmitter pour recevoir les valeurs émises.
    component.login.subscribe((event) => {
      username = event.username;
      password = event.password;
    });

    const usernameinput = testHelper.getInput('username');
    testHelper.writeInInput(usernameinput,'username');
    const passwordinput = testHelper.getInput('password');
    testHelper.writeInInput(passwordinput,'pwd');
    const button = testHelper.getButton('btn-login');

    fixture.detectChanges();

    button.click();

    expect(username!).toBe('username');
    expect(password!).toBe('pwd');
    expect(component.loginForm.valid).toBe(true);
  });

  it('username n\'est pas présent',()=>{
    let username: string;
    let password: string;

    // On s'abonne à l'EventEmitter pour recevoir les valeurs émises.
    component.login.subscribe((event) => {
      username = event.username;
      password = event.password;
    });

    const usernameinput = testHelper.getInput('username');
    testHelper.writeInInput(usernameinput,'');
    const passwordinput = testHelper.getInput('password');
    testHelper.writeInInput(passwordinput,'pwd');
    const button = testHelper.getButton('btn-login');

    fixture.detectChanges();

    button.click();

    expect(username!).toBeUndefined();
    expect(password!).toBeUndefined();
    expect(component.loginForm.valid).toBe(false);
  });

  it('pwd n\'est pas présent',()=>{
    let username: string;
    let password: string;

    // On s'abonne à l'EventEmitter pour recevoir les valeurs émises.
    component.login.subscribe((event) => {
      username = event.username;
      password = event.password;
    });

    const usernameinput = testHelper.getInput('username');
    testHelper.writeInInput(usernameinput,'username');
    const passwordinput = testHelper.getInput('password');
    testHelper.writeInInput(passwordinput,'');
    const button = testHelper.getButton('btn-login');

    fixture.detectChanges();

    button.click();
    expect(username!).toBeUndefined();
    expect(password!).toBeUndefined();
    expect(component.loginForm.valid).toBe(false);
  });
  it('username et pwd n\'est pas présent',()=>{
    let username: string;
    let password: string;

    // On s'abonne à l'EventEmitter pour recevoir les valeurs émises.
    component.login.subscribe((event) => {
      username = event.username;
      password = event.password;
    });

    const usernameinput = testHelper.getInput('username');
    testHelper.writeInInput(usernameinput,'');
    const passwordinput = testHelper.getInput('password');
    testHelper.writeInInput(passwordinput,'');
    const button = testHelper.getButton('btn-login');

    fixture.detectChanges();

    button.click();

    expect(username!).toBeUndefined();
    expect(password!).toBeUndefined();
    expect(component.loginForm.valid).toBe(false);
  });

});