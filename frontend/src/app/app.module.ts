import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { LoginPageComponent } from './login/login-page/login-page.component';
import { LoginFormComponent } from './login/login-form/login-form.component';

import { ChatPageComponent } from './chat/chat-page/chat-page.component';
import { MessagesComponent } from './chat/messages/messages.component';
import { NewMessageFormComponent } from './chat/new-message-form/new-message-form.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { AuthInterceptor } from './login/auth.interceptor';

@NgModule({
  declarations: [
    AppComponent,
    LoginPageComponent,
    LoginFormComponent,
    ChatPageComponent,
    MessagesComponent,
    NewMessageFormComponent,
    NewMessageFormComponent
  ],
  imports: [BrowserModule, AppRoutingModule, ReactiveFormsModule,HttpClientModule],
  providers: [{
    provide: HTTP_INTERCEPTORS,
    useClass: AuthInterceptor,
    multi: true,
  },],
  bootstrap: [AppComponent],
})
export class AppModule {}
