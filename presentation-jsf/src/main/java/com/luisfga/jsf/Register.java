package com.luisfga.jsf;

import com.luisfga.business.RegisterUseCase;
import com.luisfga.business.exceptions.EmailAlreadyTakenException;
import com.luisfga.business.exceptions.EmailConfirmationSendingException;

import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Named;
import javax.servlet.ServletContext;

@Named
@RequestScoped
public class Register {

    private static final long serialVersionUID = 1L;
    
    @EJB RegisterUseCase registerUseCase;

    private String token;
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    private LocalDate birthday;
    public LocalDate getBirthday() {
        return birthday;
    }
    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
    
    private String userName;
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
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
    
    private String passwordConfirmation;
    public String getPasswordConfirmation() { return passwordConfirmation; }
    public void setPasswordConfirmation(String passwordConfirmation) { this.passwordConfirmation = passwordConfirmation; }
    
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
                    getResourceBundle(FacesContext.getCurrentInstance(),"msg").
                    getString("validation.error.password.confirmation");
            
            
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,errorMessage, errorMessage);
            
            fc.addMessage(passwordConfirmationId, message);
            fc.renderResponse();

        }

    }
    
    public String execute() {

        try {
            registerUseCase.registerNewAppUser(email, password, userName, birthday);
            
            ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            registerUseCase.enviarEmailConfirmacaoNovoUsuario(ctx.getContextPath(), email);
            
        } catch (EmailAlreadyTakenException ex) {

            String errorMessage = FacesContext.getCurrentInstance().getApplication().
                    getResourceBundle(FacesContext.getCurrentInstance(),"msg").
                    getString("validation.error.email.already.taken").replace("{0}", email);
            
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,errorMessage, errorMessage);
            
            FacesContext.getCurrentInstance().addMessage(null, message);
            
            //mensagem com um link que não será "escapado" no html
            String infoMessage = FacesContext.getCurrentInstance().getApplication().
                    getResourceBundle(FacesContext.getCurrentInstance(),"msg").
                    getString("validation.error.account.recovery.link");
            
            FacesMessage faceLinkMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,infoMessage, infoMessage);
            
            FacesContext.getCurrentInstance().addMessage(null, faceLinkMessage);
            
            return "";
            
        }  catch (EmailConfirmationSendingException ex) {
            Logger.getLogger(Register.class.getName()).log(Level.SEVERE, "Erro ao tentar enviar email de confirmação para o usuário {"+email+"}", ex);
//            addActionError(getText("exception.email.confirmation.sending"));
            String errorMessage = FacesContext.getCurrentInstance().getApplication().
                    getResourceBundle(FacesContext.getCurrentInstance(),"msg").
                    getString("exception.email.confirmation.sending");
            
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,errorMessage, errorMessage);
            
            FacesContext.getCurrentInstance().addMessage(null, message);

            return "";
        }
        
//        addActionMessage(getText("action.message.account.created"));
        String successMessage = FacesContext.getCurrentInstance().getApplication().
                getResourceBundle(FacesContext.getCurrentInstance(),"msg").
                getString("action.message.account.created");

        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,successMessage, successMessage);

        FacesContext.getCurrentInstance().addMessage(null, message);
        
        return "index";

    }
}