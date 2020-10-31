import { Component } from '@angular/core';
import { User } from './user';
import { LoginService } from './login.service';
import { MessageService } from '../messages/messages.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})

export class LoginComponent {

    forwardTarget: string = "dashboard";

    user: User = {
        id: 1,
        name: 'luisfga@gmail.com',
        password: '123'
    };
    
    compTitle = 'Login';
    
    constructor(private loginService: LoginService, private messageService: MessageService){}

    tryLogin() {
        this.messageService.clear();

        this.loginService.login(this.user.name, this.user.password, this.forwardTarget);
    }

}
