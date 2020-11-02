import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

import { Observable } from 'rxjs';

import { MessageService } from '../messages/messages.service';
import { HttpClient } from '@angular/common/http';
import { JwtHelperService } from '@auth0/angular-jwt';
import * as moment from 'moment';

const jwt = new JwtHelperService();

/* Utilizado para guardar as informações do JWT decodificado.
   Deve conter todas os dados do token enviado pelo servidor */
class DecodedToken {
  exp: number;
  sub: string;
}

@Injectable({
  providedIn: 'root'
})

export class LoginService {

    //private loginUrl = 'https://localhost:8443/JEE8Demo/rest/user/authenticate';
    private loginUrl = 'http://localhost:8080/JEE8Demo/rest/user/authenticate';
    //private dashboardUrl = 'https://localhost:8443/JEE8Demo/rest/user/secure/dashboard';
    private dashboardUrl = 'http://localhost:8080/JEE8Demo/rest/user/secure/dashboard';
    private decodedToken: DecodedToken;

    constructor(private http: HttpClient, private messageService: MessageService, private router: Router) { }
  
    public login(username: string, password: string, forwardTarget: string){
        
        if (!this.isLoggedIn()){
 
            this.http
                .get<any>(this.loginUrl+'/'+username+'/'+password)
                .subscribe((result) => {
                    console.log("result.code = " + result.code);
                    if(result.code == "JWT"){
                        this.saveToken(result);
                        this.router.navigate([forwardTarget]);
                    } else {
                        this.messageService.add(result.message, result.code);
                    }
                });
        } else {
            this.router.navigate([forwardTarget]);
        }

    }
    
    private saveToken(result: any): any {
        this.decodedToken = jwt.decodeToken(result.message);
        console.log('Token decodificado = ' + JSON.stringify(this.decodedToken));
        localStorage.setItem('auth_tkn', result.message);
        console.log("Token armazenado = " + result.message);
        localStorage.setItem('auth_meta', JSON.stringify(this.decodedToken));
        this.messageService.add("Login for " + this.decodedToken.sub, result.code);
        return result;
    }

    public logout(): void {
        localStorage.removeItem('auth_tkn');
        localStorage.removeItem('auth_meta');
        
        this.decodedToken = new DecodedToken();
    }
    
    public isLoggedIn() : boolean {
        //verificar se existe um token no storage
        const authTkn = localStorage.getItem("auth_tkn");
        
        //se exitir
        if (authTkn){
            this.decodedToken = jwt.decodeToken(authTkn);
            
            console.log("Moment = " + moment());
            console.log("Expiração = " + this.decodedToken.exp*1000);
            //verificar se está na validade
            //se não estiver, deletar do storage (logout)
            if(moment().isAfter(this.decodedToken.exp*1000)){
                console.log("Token expirado");
                
                /* se estiver expirado, evita tratamento no servidor. 
                   Deletando qualquer token antigo, o servidor vai tratar 
                   como um login inicial normal */
                this.logout();
                return false;
            } else {
                console.log("Token válido");
                return true;
            }
            
        } else {
            console.log("Token inexistente");
            return false;
        }
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

