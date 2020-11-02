package com.luisfga.struts;

import com.luisfga.business.PasswordResetUseCase;
import com.luisfga.business.exceptions.ForbidenOperationException;
import com.luisfga.business.exceptions.InvalidDataException;
import com.luisfga.business.exceptions.TimeHasExpiredException;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.ExpressionValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import java.util.StringTokenizer;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

/**
 * OBS: esta classe tem uma utilização incomum do atributo token. É utilizado também junto com o banco de dados
 * para validar token recebido de link externo e passar para externalOpenWindowToken.
 * @author luigi
 */
public class PasswordResetAction extends ActionSupport {
    
    private final Logger logger = LogManager.getLogger();
    
    @EJB PasswordResetUseCase passwordResetUseCase;

    private String password;
    public String getPassword() {
        return password;
    }
    @RequiredStringValidator(key = "validation.error.required")
    public void setPassword(String password) {
        this.password = password;
    }
    
    private String passwordConfirmation;
    public String getPasswordConfirmation() { return passwordConfirmation; }
    @RequiredStringValidator(key = "validation.error.required")
    @ExpressionValidator(expression = "password==passwordConfirmation", key = "validation.error.password.confirmation")
    public void setPasswordConfirmation(String passwordConfirmation) { this.passwordConfirmation = passwordConfirmation; }

    private String email;
    public String getEmail() {
        return email;
    }
    @RequiredStringValidator(key = "validation.error.required")
    @EmailValidator(key = "validation.error.invalid.email")
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
    
    
    private String token;
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    
    @Action(value = "passwordResetInput", 
            results = {
                @Result(name="error", location="index.jsp"),
                @Result(name="input", location="passwordReset.jsp")
            })
    @Override
    public String input() {
        try {
            email = passwordResetUseCase.validateOperationWindow(encodedUserEmail, token);

        } catch (InvalidDataException ide) {
            addActionError(getText("action.error.invalid.informations"));
            return ERROR;
                    
        } catch (ForbidenOperationException foException){
            addActionError(getText("action.error.forbiden.operation"));
            HttpServletRequest request = ServletActionContext.getRequest();
            //TODO revisar essa forma de tentar pegar o ip do cliente.
            logger.error("IP suspeito {"+getClientIpAddress(request)+"}", foException);
            return ERROR;

        } catch (TimeHasExpiredException teException) {
            addActionError(getText("action.error.invalid.user.temp.window.token"));
            return ERROR;
            
        } catch (Exception ex) {
            addActionError(getText("exception.unknown"));
            logger.error("Usuário não encontrado ao tentar resetar senha.", ex);
            return ERROR;
        }
        return INPUT;
    }
    
    @Action(value = "passwordReset", 
            results = {
                @Result(name="error", location="index.jsp"),
                @Result(name="success", location="login.jsp")
            })
    @Override
    public String execute() throws Exception {
        
        passwordResetUseCase.resetPassword(email, password);

        addActionMessage(getText("action.message.password.reset"));
        
        return SUCCESS;
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