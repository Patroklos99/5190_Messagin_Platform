import { Component, OnInit } from '@angular/core';
import { LoginService } from '../login.service';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.css'],
})
export class LoginPageComponent implements OnInit {
  errorInvalid = false;
  errorConnect = false;

  constructor(private router: Router,private loginService:LoginService) {
  }

  ngOnInit(): void {}


  async onLogin(login: { username: string; password: string }) {

    try {
      await this.loginService.login(login);
      await this.router.navigate(['/chat'])
    }catch (e){
      if(e instanceof HttpErrorResponse){
        this.choseError(e.status);
      }
    }

  }

  showInvalidError() {
    return this.errorInvalid;
  }

  showConnectionError() {
    return this.errorConnect;
  }

  choseError(erreur: number){
    if(erreur == 403){
      this.errorInvalid = true;
    }
    else{
      this.errorConnect = true;
    }
  }
}
