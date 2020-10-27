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
        name: 'Windstorm',
        password: '123'
    };
    
    compTitle = 'Login';
    
    submitted = false;

    constructor(private loginService: LoginService, private messageService: MessageService){}

    tryLogin() {
        this.submitted = true;
                
        this.messageService.clear();

        var res:string;
        this.loginService.tryLogin(this.user.name, this.user.password).subscribe(result => res = result);
        
        this.messageService.clear();
        this.messageService.add(res, "Error");
        
        this.submitted = false;
    }

    // TODO: Remove this when we're done
    get diagnostic() { return JSON.stringify(this.user); }
}
