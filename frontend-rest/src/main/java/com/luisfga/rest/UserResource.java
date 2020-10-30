package com.luisfga.rest;

import com.luisfga.business.LoginUseCase;
import com.luisfga.business.exceptions.LoginException;
import com.luisfga.business.exceptions.PendingEmailConfirmationException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.IOException;
import java.security.Key;
import java.util.Date;
import java.util.logging.Level;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.DatatypeConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Path("/user")
public class UserResource {
    
    private final String ERROR = "ERROR";
    private final String INFO = "INFO";
    
    private final Logger logger = LogManager.getLogger();
    
    @Inject LoginUseCase loginUseCase;
    
    @GET
    @Path("/authenticate/{userName}/{password}")
    @Produces(MediaType.APPLICATION_JSON)
    public String authenticate(
            @PathParam("userName") String userName, 
            @PathParam("password") String password,
            @Context HttpServletRequest request,
            @Context HttpServletResponse response){
        
        try {
            loginUseCase.login(userName, password);
            
            return jsonifySimpleResult(provideToken(request, response), INFO);
            
        } catch (LoginException ex) {
            logger.error("LoginException");
            return jsonifySimpleResult("LoginException", ERROR);
            
        } catch (PendingEmailConfirmationException ex) {
            logger.error("PendingEmailConfirmationException");
            return jsonifySimpleResult("PendingEmailConfirmationException", ERROR);
            
        } catch (IOException ex) {
            logger.error("IOException: " + ex.getMessage());
            return jsonifySimpleResult("IOException", ERROR);
        }
        
        //gerar token e colocar no http header
        
//        return jsonifySimpleResult("Ok! userName="+userName+" Logged successfully", INFO);
    }
    
    private String provideToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SignatureAlgorithm sigAlg = SignatureAlgorithm.HS256;

        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("secret");
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, sigAlg.getJcaName());
//        System.out.println(request.getUserPrincipal().getName());
        JwtBuilder builder = Jwts.builder()
                .setSubject(request.getUserPrincipal().getName())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(sigAlg, signingKey);

        response.setStatus(HttpServletResponse.SC_OK);
        String tokenResult = builder.compact();
        logger.debug("Retornando JWT: " + tokenResult);
        return tokenResult;
    }
    
    @GET
    @Path("/logout")
    @Produces(MediaType.APPLICATION_JSON)
    public String authenticate(){
        
        loginUseCase.logout();

        return jsonifySimpleResult("Session destroyed!", INFO);
    }
    
    @GET
    @Path("/secure/dashboard")
    @Produces(MediaType.APPLICATION_JSON)
    public String dashboard(){
        
        return jsonifySimpleResult("Dashboard", INFO);
    }
    
    @GET
    @Path("/authRequired")
    @Produces(MediaType.APPLICATION_JSON)
    public String authRequired(){
        
        return jsonifySimpleResult("Authentication required.", ERROR);
    }
    
    private String jsonifySimpleResult(String message, String code){
//        logger.debug("JSONifying {\"message\": \""+message+"\", \"code\": \""+code+"\"}");
        return "{\"message\": \""+message+"\", \"code\": \""+code+"\"}";
    }
}
