package com.luisfga.struts;

import com.luisfga.business.LoginUseCase;
import com.luisfga.business.exceptions.PendingEmailConfirmationException;
import com.luisfga.business.exceptions.EmailConfirmationSendingException;
import com.luisfga.business.exceptions.LoginException;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import java.util.Map;
import javax.ejb.EJB;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.SessionAware;

public class LoginAction extends ActionSupport implements SessionAware {

    private final Logger logger = LogManager.getLogger();
    
    @EJB LoginUseCase loginUseCase;
    
    private Map<String, Object> userSession;
    @Override public void setSession(Map<String, Object> session) { userSession = session; }

    private String token;
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    private String email;
    public String getEmail() {
        return email;
    }
    @RequiredStringValidator(key = "validation.error.required")
    @EmailValidator(key = "validation.error.invalid.email")
    public void setEmail(String email) {
        this.email = email;
    }
    
    private String password;
    public String getPassword() {
        return password;
    }
    @RequiredStringValidator(key = "validation.error.required")
    public void setPassword(String password) {
        this.password = password;
    }

    @Action(value = "loginInput", results = {@Result(name="input", location="login.jsp")})
    @Override
    public String input() {
        return INPUT;
    }
    
    @Action(value = "login", 
            results = {
                @Result(name="error", location="login.jsp"),
                @Result(name="success", location="secure/dashboard", type = "redirectAction")
            }
    )
    @Override
    public String execute() {
        
        try {

            loginUseCase.login(email, password);
            
        } catch (LoginException le) { 
            addActionError(getText("action.error.authentication.exception"));
            return ERROR;  
            
        } catch (PendingEmailConfirmationException pecException) {
            
            addActionError(getText("action.error.pending.email.confirmation"));
            
            try {
                String contextPath = ServletActionContext.getServletContext().getContextPath();
                loginUseCase.enviarEmailConfirmacaoNovoUsuario(contextPath, email);

            } catch (EmailConfirmationSendingException ex) { //generaliza possíveis exceções em uma só
                logger.error("Erro ao tentar enviar email de confirmação para o usuário {"+email+"}", ex);
                addActionError(getText("exception.email.confirmation.sending"));
            }
            
            return ERROR;            
            
        }
        
        return SUCCESS;
    }
}
