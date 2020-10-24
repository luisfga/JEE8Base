package com.luisfga.jsf;

import com.luisfga.business.PasswordRecoverUseCase;
import com.luisfga.business.exceptions.EmailConfirmationSendingException;
import com.luisfga.business.exceptions.WrongInfoException;
import java.time.LocalDate;
import java.util.UUID;
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
public class PasswordRecover {

    
    private final Logger logger = LogManager.getLogger();
    
    @EJB private PasswordRecoverUseCase passwordRecoverUseCase;
    
    private String email;
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    
    private LocalDate birthday;
    public LocalDate getBirthday() {
        return birthday;
    }
    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
    
    public String execute() {
        String windowToken = UUID.randomUUID().toString();
        try {
            passwordRecoverUseCase.prepareRecovery(email, birthday, windowToken);
            
            ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            
            passwordRecoverUseCase.enviarEmailResetSenha(ctx.getContextPath(), email, windowToken);
            
        } catch (WrongInfoException wbException) {
            String errorMessage = FacesContext.getCurrentInstance().getApplication().
                    getResourceBundle(FacesContext.getCurrentInstance(),"msg").
                    getString("action.error.invalid.informations");
            
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,errorMessage, errorMessage);
            
            FacesContext.getCurrentInstance().addMessage(null, message);

            return "index";
            
        } catch (EmailConfirmationSendingException ex) {
            logger.error("Erro ao tentar enviar email para reset de senha para o usuário {"+email+"}", ex);
            String errorMessage = FacesContext.getCurrentInstance().getApplication().
                    getResourceBundle(FacesContext.getCurrentInstance(),"msg").
                    getString("exception.email.confirmation.sending");
            
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,errorMessage, errorMessage);
            
            FacesContext.getCurrentInstance().addMessage(null, message);

            return "index";            
        }
        
        //enviar email para reset de senha
        String successMessage = FacesContext.getCurrentInstance().getApplication().
                getResourceBundle(FacesContext.getCurrentInstance(),"msg").
                getString("action.message.reset.password.email.sent");

        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,successMessage, successMessage);

        FacesContext.getCurrentInstance().addMessage(null, message);
        
        return "index";
    }
    
}