import { Injectable } from '@angular/core';
import { BehaviorSubject, firstValueFrom, Observable } from 'rxjs';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { environment } from '../../environments/environment';



@Injectable({
  providedIn: 'root',
})
export class LoginService {
  static KEY = 'username';
  static TOKEN_KEY = 'QWERTY';
  private token : string | null;

  private username = new BehaviorSubject<string | null>(null);


  constructor(private httpclient:HttpClient) {
    this.username.next(localStorage.getItem(LoginService.KEY));
    this.token = localStorage.getItem(LoginService.TOKEN_KEY);
  }

  getToken(){
    return this.token;
  }

  async login(login: { username: string; password: string }) {
    const reponse = await firstValueFrom(
      this.httpclient.post<{token:string}>(
        `${environment.backendUrl}/auth/login`,{
          username: login.username,
          password: login.password,
        }
      )
    )
      localStorage.setItem(LoginService.KEY, login.username);
      localStorage.setItem(LoginService.TOKEN_KEY, reponse.token);
      this.token = reponse.token;
      this.username.next(login.username);

  }

  async logout() {
    try {
    const reponse = await firstValueFrom(
        this.httpclient.post(
        `${environment.backendUrl}/auth/logout`,{
         }
        )
      )
    }catch (e){

    }finally {
      localStorage.removeItem(LoginService.KEY);
      localStorage.removeItem(LoginService.TOKEN_KEY);
      this.token = null;
      this.username.next(null);
    }
  }

  getUsername(): Observable<string | null> {
    return this.username.asObservable();
  }
}
