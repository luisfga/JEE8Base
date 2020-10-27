package com.luisfga.jsf;

import com.luisfga.business.PasswordResetUseCase;
import com.luisfga.business.exceptions.ForbidenOperationException;
import com.luisfga.business.exceptions.TimeHasExpiredException;
import java.util.StringTokenizer;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * OBS: esta classe tem uma utilização incomum do atributo token. É utilizado também junto com o banco de dados
 * para validar token recebido de link externo e passar para externalOpenWindowToken.
 * @author luigi
 */
@Named
@RequestScoped
public class PasswordReset {
    
    private final Logger logger = LogManager.getLogger();
    
    @EJB private PasswordResetUseCase passwordResetUseCase;

    private String password;
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    
    private String passwordConfirmation;
    public String getPasswordConfirmation() { return passwordConfirmation; }
    public void setPasswordConfirmation(String passwordConfirmation) { this.passwordConfirmation = passwordConfirmation; }

    private String email;
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    
    private String externalOpenWindowToken;
    public String getExternalOpenWindowToken() {
        return externalOpenWindowToken;
    }
    public void setExternalOpenWindowToken(String externalOpenWindowToken) {
        this.externalOpenWindowToken = externalOpenWindowToken;
    }
    
    private String encodedUserEmail;
    public String getEncodedUserEmail() {
        return encodedUserEmail;
    }
    public void setEncodedUserEmail(String encodedUserEmail) {
        this.encodedUserEmail = encodedUserEmail;
    }
    
    private String windowToken;
    public String getWindowToken() {
        return windowToken;
    }
    public void setWindowToken(String windowToken) {
        this.windowToken = windowToken;
    }
    
    public void validatePasswordEquality(ComponentSystemEvent event){
        
        FacesContext fc = FacesContext.getCurrentInstance();

        UIComponent components = event.getComponent();

        // get password
        UIInput uiInputPassword = (UIInput) components.findComponent("password");
        String passwordVal = uiInputPassword.getLocalValue() == null ? ""
                : uiInputPassword.getLocalValue().toString();

        // get confirm password
        UIInput uiInputConfirmPassword = (UIInput) components.findComponent("passwordConfirmation");
        String confirmPassword = uiInputConfirmPassword.getLocalValue() == null ? ""
                : uiInputConfirmPassword.getLocalValue().toString();
        String passwordConfirmationId = uiInputConfirmPassword.getClientId();

        // Let required="true" do its job.
        if (passwordVal.isEmpty() || confirmPassword.isEmpty()) {
            return;
        }

        if (!passwordVal.equals(confirmPassword)) {

            String errorMessage = FacesContext.getCurrentInstance().getApplication().
                    getResourceBundle(FacesContext.getCurrentInstance(),"global").
                    getString("validation.error.password.confirmation");
            
            
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,errorMessage, errorMessage);
            
            fc.addMessage(passwordConfirmationId, message);
            fc.renderResponse();

        }

    }
    
    public void validateOperationWindow() {
        try {
            email = passwordResetUseCase.validateOperationWindow(encodedUserEmail, windowToken);

        } catch (ForbidenOperationException foException){
            String errorMessage = FacesContext.getCurrentInstance().getApplication().
                    getResourceBundle(FacesContext.getCurrentInstance(),"global").
                    getString("action.error.forbiden.operation");
            
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,errorMessage, errorMessage);
            
            FacesContext.getCurrentInstance().addMessage(null, message);
            
            //TODO revisar essa forma de tentar pegar o ip do cliente.
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            logger.error("IP suspeito {"+getClientIpAddress(request)+"}", foException);


        } catch (TimeHasExpiredException teException) {

            String errorMessage = FacesContext.getCurrentInstance().getApplication().
                    getResourceBundle(FacesContext.getCurrentInstance(),"global").
                    getString("action.error.invalid.user.temp.window.token");
            
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,errorMessage, errorMessage);
            
            FacesContext.getCurrentInstance().addMessage(null, message);

            
        } catch (Exception ex) {

            logger.error("Usuário não encontrado ao tentar resetar senha.", ex);

            String errorMessage = FacesContext.getCurrentInstance().getApplication().
                    getResourceBundle(FacesContext.getCurrentInstance(),"global").
                    getString("exception.unknown");
            
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,errorMessage, errorMessage);
            
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

    }
    
    public String execute() throws Exception {
        
        passwordResetUseCase.resetPassword(email, password);

        String infoMessage = FacesContext.getCurrentInstance().getApplication().
                getResourceBundle(FacesContext.getCurrentInstance(),"global").
                getString("action.message.password.reset");

        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,infoMessage, infoMessage);

        FacesContext.getCurrentInstance().addMessage(null, message);
        
        return "login";
    }
    
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedForHeader == null) {
            return request.getRemoteAddr();
        } else {
            // As of https://en.wikipedia.org/wiki/X-Forwarded-For
            // The general format of the field is: X-Forwarded-For: client, proxy1, proxy2 ...
            // we only want the client
            return new StringTokenizer(xForwardedForHeader, ",").nextToken().trim();
        }
    }
}