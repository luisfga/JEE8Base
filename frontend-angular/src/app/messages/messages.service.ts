import { Injectable } from '@angular/core';
import { Message } from './Message';

@Injectable({
  providedIn: 'root',
})
export class MessageService {
    
  messages: Message[] = [];

  add(text: string, type: string) {
    this.messages.push({text, type});
  }

  clear() {
    this.messages = [];
  }
}
