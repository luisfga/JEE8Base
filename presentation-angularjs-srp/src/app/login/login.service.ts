import { Injectable } from '@angular/core';
import { User } from './user';
import { Observable, of } from 'rxjs';

import { MessageService } from '../messages/messages.service';

//desenv
import { result } from '../login/mock-result';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(public messageService: MessageService) { }
  
  tryLogin(username: string, password: string):Observable<string>{
      return of(result);
  }
}
