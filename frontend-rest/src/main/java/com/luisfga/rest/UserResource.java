package com.luisfga.rest;

import com.luisfga.business.LoginUseCase;
import com.luisfga.business.exceptions.LoginException;
import com.luisfga.business.exceptions.PendingEmailConfirmationException;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Path("/user")
public class UserResource {
    
    private final Logger logger = LogManager.getLogger();    
    
    @Inject LoginUseCase loginUseCase;
    
    @GET
    @Path("/authenticate/{userName}/{password}")
    @Produces(MediaType.APPLICATION_JSON)
    public String authenticate(@PathParam("userName") String userName, @PathParam("password") String password){
        
        try {
            loginUseCase.login(userName, password);
        } catch (LoginException ex) {
            logger.error("LoginException");
            return jsonifySimpleResult("LoginException");
            
        } catch (PendingEmailConfirmationException ex) {
            logger.error("PendingEmailConfirmationException");
            return jsonifySimpleResult("PendingEmailConfirmationException");
        }
        
        //gerar token e colocar no http header
        
        return jsonifySimpleResult("Ok! userName="+userName+" Logged successfully");
    }
    
    @GET
    @Path("/logout")
    @Produces(MediaType.APPLICATION_JSON)
    public String authenticate(){
        
        loginUseCase.logout();

        return jsonifySimpleResult("Session destroyed!");
    }
    
    @GET
    @Path("/secure/dashboard")
    @Produces(MediaType.APPLICATION_JSON)
    public String dashboard(){
        
        return jsonifySimpleResult("Dashboard");
    }
    
    @GET
    @Path("/authRequired")
    @Produces(MediaType.APPLICATION_JSON)
    public String authRequired(){
        
        return jsonifySimpleResult("Authentication required.");
    }
    
    private String jsonifySimpleResult(String message){
        return "{\"result\": \""+message+"\"}";
    }
}