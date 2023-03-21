import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormBuilder, Validator, Validators } from '@angular/forms';
import { LoginPageComponent } from '../login-page/login-page.component';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css'],
})
export class LoginFormComponent implements OnInit {
  loginForm = this.fb.group({
    username: [null, Validators.required],
    password: [null, Validators.required],
  });

  @Output()
  login = new EventEmitter<{ username: string; password: string }>();

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {}

  onLogin() {

    if (this.loginForm.controls.username.value &&
      this.loginForm.controls.password.value && this.loginForm.valid) {
      const username = this.loginForm.controls.username.value;
      const password = this.loginForm.controls.password.value;
      let obj = { username, password }
      this.login.emit(obj);
    } else {
      this.loginForm.markAllAsTouched();
    }
  }
  showUsernameRequiredError(): boolean {
    return this.showError('username', "required");
  }

  showPasswordRequiredError(): boolean {
    return this.showError('password', "required");
  }

  private showError(field: 'username' | 'password', error: string): boolean {
    return (
      this.loginForm.controls[field].hasError(error) &&
      (this.loginForm.controls[field].dirty || this.loginForm.controls[field].touched)
    );
  }
}
