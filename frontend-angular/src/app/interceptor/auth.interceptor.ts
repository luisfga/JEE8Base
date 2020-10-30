import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpEvent, HttpHandler, HttpRequest, HttpResponse, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
    
    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        
        const authTkn = localStorage.getItem("auth_tkn");
        console.log("authTkn no interceptor = " + authTkn);
        
        if(authTkn) {
            
            var headers = new HttpHeaders()
                                    .set("Authorization","Bearer "+authTkn)
                                    .set("Content-Type","application/json");

            
            const cloned = req.clone({headers});
            console.log("Cloned Headers -> "+JSON.stringify(cloned.headers));

            console.log("Requisição clonada - ("+cloned.method+")");
            return next.handle(cloned);
        }

        console.log("Requisição normal - ("+req.method+")");
        return next.handle(req);
    }
}