import { Injectable } from '@angular/core';
import { User } from './user';
import { Observable, of } from 'rxjs';

import { HttpClient, HttpHeaders } from '@angular/common/http';
import { JwtHelperService } from '@auth0/angular-jwt';
import * as moment from 'moment';

const jwt = new JwtHelperService();

class DecodedToken {
  exp: number;
  username: string;
}

@Injectable({
  providedIn: 'root'
})

export class LoginService {

    private loginUrl = 'https://localhost:8443/JEE8Demo/rest/user/authenticate';
    private dashboardUrl = 'https://localhost:8443/JEE8Demo/rest/user/secure/dashboard';
    private decodedToken;

    constructor(private http: HttpClient) { }
  
    public tryLogin(username: string, password: string):Observable<any>{
        return this.http.get<any>(this.loginUrl+'/'+username+'/'+password)
                    .pipe(token => {
                        return this.saveToken(token)
                    });
    }
    
    public logout(): void {
        localStorage.removeItem('auth_tkn');
        localStorage.removeItem('auth_meta');
        
        this.decodedToken = new DecodedToken();
    }
    
    private saveToken(token: any): any {
        this.decodedToken = jwt.decodeToken(token.token);
        localStorage.setItem('auth_tkn', token);
        localStorage.setItem('auth_meta', JSON.stringify(this.decodedToken));
        return token;
    }
    
    dashboard():Observable<any>{
        return this.http.get<any>(this.dashboardUrl);
    }
}
