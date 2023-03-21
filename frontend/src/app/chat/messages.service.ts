import { Injectable, Optional } from '@angular/core';
import { BehaviorSubject, firstValueFrom, Observable } from 'rxjs';
import { Message, MessageRequest } from './message.model';
import { environment } from '../../environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class MessagesService {
  messages = new BehaviorSubject<Message[]>([]);

  constructor(private httpclient:HttpClient) {}

  async postMessage(message: MessageRequest): Promise<void> {
    const reponse = await firstValueFrom(
      this.httpclient.post<Message>(
        `${environment.backendUrl}/messages`, {
          text: message.text,
          username: message.username,
          imageData: message.imageData
        }
      )
    );

    //this.messages.next([...this.messages.value,reponse]);
  }

  async fetchMessage(): Promise<Message[]>{
    const message = this.messages.value[this.messages.value.length-1];
    let params = new HttpParams();
    let id = null;
    if(message != null){
      id = message.id!;
      params = params.append("fromId",id);
      const reponse = await firstValueFrom(
         this.httpclient.get<Message[]>(
          `${environment.backendUrl}/messages`, {
            params:params
          })
      )
      return reponse;
    }
    const reponse = await firstValueFrom(
      this.httpclient.get<Message[]>(
        `${environment.backendUrl}/messages`, {
        })
    )
    return reponse;

  }

  getMessages(): Observable<Message[]>{
    let reponse = this.fetchMessage();
    reponse.then(
      list => {
        if (list) {
          for (let i = 0; i < list.length; i++) {
            this.messages.next([...this.messages.value, list[i]])
          }
        }
      });

    return this.messages.asObservable();
  }
}
