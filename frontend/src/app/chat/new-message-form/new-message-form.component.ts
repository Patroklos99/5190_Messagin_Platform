import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormBuilder } from '@angular/forms';

@Component({
  selector: 'app-new-message-form',
  templateUrl: './new-message-form.component.html',
  styleUrls: ['./new-message-form.component.css'],
})
export class NewMessageFormComponent implements OnInit {
  messageForm = this.fb.group({
    message: '',
  });
  file: File | null = null;
  @Output()
  sendMessage = new EventEmitter<{msg:string,file:File|null}>();

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {}



  fileChanged(event: any) {
    this.file = event.target.files[0];
    event.target.files[0] = null;
  }

  onSend() {
    if (this.messageForm.valid && this.messageForm.value.message) {
      if(this.file != null)
        this.sendMessage.emit({msg:this.messageForm.value.message,file:this.file});
      else
        this.sendMessage.emit({msg:this.messageForm.value.message,file:null});
      this.messageForm.reset();
      this.file = null;
    }
  }
}

