import { Injectable } from '@angular/core';
import { User } from './user';
import { Observable, of } from 'rxjs';

import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

    private loginUrl = 'https://localhost:8443/JEE8Demo/rest/user/authenticate';
    private dashboardUrl = 'https://localhost:8443/JEE8Demo/rest/user/secure/dashboard';

    constructor(private http: HttpClient) { }
  
    tryLogin(username: string, password: string):Observable<any>{
        return this.http.get<any>(this.loginUrl+'/'+username+'/'+password);
    }
    
    dashboard():Observable<any>{
        return this.http.get<any>(this.dashboardUrl);
    }
}
