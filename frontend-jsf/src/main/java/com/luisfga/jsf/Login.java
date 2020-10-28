package com.luisfga.jsf;

import com.luisfga.business.LoginUseCase;
import com.luisfga.business.exceptions.EmailConfirmationSendingException;
import com.luisfga.business.exceptions.LoginException;
import com.luisfga.business.exceptions.PendingEmailConfirmationException;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named
@RequestScoped
public class Login extends JsfBeanSupport{

    private final Logger logger = LogManager.getLogger();    
    
    @EJB LoginUseCase loginUseCase;
    
    private String token;
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    private String email;
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    
    private String password;
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String logout(){
        loginUseCase.logout();
        return "/index?faces-redirect=true";
    }
    
    public String execute() {
        
        try {

            loginUseCase.login(email, password);
            
        } catch (LoginException le) { 

            // Bring the information message using the Faces Context
            String errorMessage = getMsgText("global", "action.error.authentication.exception");
            
            // Add View Faces Message
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,errorMessage, errorMessage);
            // The component id is null, so this message is considered as a view message
            FacesContext.getCurrentInstance().addMessage(null, message);
            // Return empty token for navigation handler

            return "login";
            
        } catch (PendingEmailConfirmationException pecException) {
            
            // Bring the information message using the Faces Context
            String errorMessage = getMsgText("global", "action.error.pending.email.confirmation");
            
            // Add View Faces Message
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,errorMessage, errorMessage);
            // The component id is null, so this message is considered as a view message
            FacesContext.getCurrentInstance().addMessage(null, message);            

            try {
                ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
                loginUseCase.enviarEmailConfirmacaoNovoUsuario(ctx.getContextPath(),email);

            } catch (EmailConfirmationSendingException ex) { //generaliza possíveis exceções em uma só
                logger.error("Erro ao tentar enviar email de confirmação para o usuário {"+email+"}", ex);

                // Bring the information message using the Faces Context
                String errorMessage2 = getMsgText("global", "exception.email.confirmation.sending");

                // Add View Faces Message
                FacesMessage message2 = new FacesMessage(FacesMessage.SEVERITY_ERROR,errorMessage2, errorMessage2);
                // The component id is null, so this message is considered as a view message
                FacesContext.getCurrentInstance().addMessage(null, message2);
            }
            
            return "login"; // Return empty token for navigation handler
            
        }
        
        return "/secure/dashboard?faces-redirect=true";
    }
}
