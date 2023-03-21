import { Injectable } from '@angular/core';
import { connect, Observable, Subject } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class WebsocketService {
  private ws: WebSocket | null = null;
  private events: Subject<'notif'> | null = null;
  private disconnected: boolean = false;

  constructor() {}
  public connect(): Observable<'notif'> {

      try {
        this.ws = new WebSocket(`${environment.wsUrl}/notifications`);
      }catch (e){
        setTimeout(()=>{
          this.connect();
        },2000);
      }


    if(this.events == null)
      this.events = new Subject<'notif'>();

    if(this.ws != null) {
      this.ws.onmessage = (event) => {
        if (this.events != null)
          return this.events.next('notif');
      };
      this.ws.onclose = (event) => {
        if(this.disconnected){
          this.events?.complete();
        }else{
          setTimeout(()=>{
            this.connect();
          }, 2000);
        }

      }
      this.ws.onerror = () => {
        //if(this.events != null)
          //this.events.error('error');
        //console.log("error")
        //this.ws?.close();
      }
    }
    return this.events.asObservable();
  }
  public disconnect() {
    this.disconnected = true;
    this.ws?.close();
    this.ws = null;
  }
}
