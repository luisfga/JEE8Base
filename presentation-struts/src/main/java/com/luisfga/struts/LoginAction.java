package com.luisfga.struts;

import com.luisfga.business.LoginUseCase;
import com.luisfga.business.exceptions.PendingEmailConfirmationException;
import com.luisfga.business.helper.MailHelper;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.mail.MessagingException;
import javax.security.auth.login.LoginException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.SessionAware;

public class LoginAction extends ActionSupport implements SessionAware {

    @EJB LoginUseCase loginUseCase;
    
    @EJB private MailHelper mailHelper;

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
                @Result(name="input", location="login.jsp"),
                @Result(name="success", location="secure/dashboard", type = "redirectAction")
            }
    )
    @Override
    public String execute() {
        
        try {

            loginUseCase.login(email, password);
            
        } catch (LoginException le) { 
            addActionError(getText("action.error.authentication.exception"));
            return INPUT;  
            
        } catch (PendingEmailConfirmationException pecException) {
            
            addActionError(getText("action.error.pending.email.confirmation"));
            
            try {
                String contextPath = ServletActionContext.getServletContext().getContextPath();
                mailHelper.enviarEmailConfirmacaoNovoUsuario(contextPath,loginUseCase.loadUser(email));

            } catch (MessagingException | UnsupportedEncodingException ex) { //generaliza possíveis exceções em uma só
                Logger.getLogger(LoginAction.class.getName()).log(Level.SEVERE, "Erro ao tentar enviar email de confirmação para o usuário {"+email+"}", ex);
                addActionError(getText("exception.email.confirmation.sending"));
            }
            
            return INPUT;            
            

            
        } catch (AuthenticationException authException) {
            addActionError(getText("action.error.authentication.exception"));
            return INPUT;  
        }
        
        return SUCCESS;
    }
}
