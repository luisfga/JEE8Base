import { Injectable } from '@angular/core';
import { User } from './user';
import { Observable, of } from 'rxjs';

import { MessageService } from '../messages/messages.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { JwtHelperService } from '@auth0/angular-jwt';
import * as moment from 'moment';

const jwt = new JwtHelperService();

class DecodedToken {
  exp: number;
  sub: string;
}

@Injectable({
  providedIn: 'root'
})

export class LoginService {

    private loginUrl = 'https://localhost:8443/JEE8Demo/rest/user/authenticate';
    private dashboardUrl = 'https://localhost:8443/JEE8Demo/rest/user/secure/dashboard';
    private decodedToken;

    constructor(private http: HttpClient, private messageService: MessageService) { }
  
    public tryLogin(username: string, password: string){
        this.login(username, password).subscribe((result) => {
            this.saveToken(result);
        });
    }
  
    private login(username: string, password: string):Observable<any>{
        return this.http.get<any>(this.loginUrl+'/'+username+'/'+password);
    }
    
    private saveToken(result: any): any {
        this.decodedToken = jwt.decodeToken(result.message);
        console.log('Decoded token = ' + JSON.stringify(this.decodedToken));
        localStorage.setItem('auth_tkn', result.message);
        console.log("stored token = " + result.message);
        localStorage.setItem('auth_meta', JSON.stringify(this.decodedToken));
        this.messageService.add("Login for " + this.decodedToken.sub, result.code);
        return result;
    }

    public logout(): void {
        localStorage.removeItem('auth_tkn');
        localStorage.removeItem('auth_meta');
        
        this.decodedToken = new DecodedToken();
    }
    
    public isLoggedIn() {
        const exp = this.decodedToken.exp;
        return !exp && moment().isBefore(exp);
    }
    
    private dashboard():Observable<any>{
        return this.http.get<any>(this.dashboardUrl);
    }
    
    public tryDashboard(){
        this.dashboard().subscribe((result) => {
            this.messageService.add(result.message, result.code);
        });
    }
}

