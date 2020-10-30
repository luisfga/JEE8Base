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

    user: User = {
        id: 1,
        name: 'luisfga@gmail.com',
        password: '123'
    };
    
    compTitle = 'Login';
    
    constructor(private loginService: LoginService, private messageService: MessageService){}

    tryLogin() {
        this.messageService.clear();

        this.loginService.tryLogin(this.user.name, this.user.password);
    }

    dashboard(){
        this.messageService.clear();

        this.loginService.tryDashboard();
    }

    // TODO: Remove this when we're done
    get diagnostic() { return JSON.stringify(this.user); }
}
