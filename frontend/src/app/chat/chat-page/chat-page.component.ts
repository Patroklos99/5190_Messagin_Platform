import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, Subject, Subscription } from 'rxjs';
import { LoginService } from 'src/app/login/login.service';
import { ChatImageData, Message } from '../message.model';
import { MessagesService } from '../messages.service';
import { WebsocketService } from '../../app.WebsocketService';
import {FileReaderService} from '../FileReaderService';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-chat-page',
  templateUrl: './chat-page.component.html',
  styleUrls: ['./chat-page.component.css'],
})
export class ChatPageComponent implements OnInit, OnDestroy {
  username$ = this.loginService.getUsername();
  sockets$ = this.websocket.connect();

  messages$ :Observable<Message[]>;

  currentUsername: string | null = null;
  messages: Message[] = [];

  usernameSubscription: Subscription;
  messagesSubscription: Subscription;
  socket: Subscription;

  constructor(
    private messagesService: MessagesService,
    private loginService: LoginService,
    private router: Router,
    private websocket:WebsocketService,
    private fileReaderService:FileReaderService
  ) {

    this.messages$ = this.messagesService.getMessages();

    this.usernameSubscription = this.username$.subscribe((u) => {
      this.currentUsername = u;
    });
    this.messagesSubscription = this.messages$.subscribe((m) => {
      this.messages = m;
    });
    this.socket = this.sockets$.subscribe((u) =>{
        this.getMessage();
    });

  }
  getMessage(){
    try {
      return this.messagesService.getMessages();
    }catch (e){
      if(e instanceof HttpErrorResponse){
        this.choseError(e.status);
      }
      return null;
    }
  }


  ngOnInit(): void {}

  ngOnDestroy(): void {
    if (this.usernameSubscription) {
      this.usernameSubscription.unsubscribe();
    }
    if (this.messagesSubscription) {
      this.messagesSubscription.unsubscribe();
    }
  }

  async onSendMessage(event:{msg: string,file: File| null}) {
    if (this.currentUsername) {
      let imageData: ChatImageData | null = null;
      if(event.file != null) {
        let retour = await this.fileReaderService.readFile(event.file);
        imageData = {
          data: retour.data,
          type: retour.type
        }
      }
      try {
        await this.messagesService.postMessage({
          text: event.msg,
          username: this.currentUsername,
          imageData:imageData
        });
      }
      catch (e){
        if(e instanceof HttpErrorResponse){
          await this.choseError(e.status);
        }
      }

    }
  }

  async onQuit() {
    await this.loginService.logout();
    this.websocket.disconnect();
    await this.router.navigate(['/']);
  }


  async choseError(erreur: number) {
    if (erreur == 403) {
      await this.loginService.logout();
      this.websocket.disconnect();
      await this.router.navigate(['/']);
    }
  }
}
