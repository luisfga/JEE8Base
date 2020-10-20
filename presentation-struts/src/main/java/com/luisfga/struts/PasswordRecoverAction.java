package com.luisfga.struts;

import com.luisfga.business.PasswordRecoverUseCase;
import com.luisfga.business.entities.AppUser;
import com.luisfga.business.exceptions.EmailConfirmationSendingException;
import com.luisfga.business.exceptions.WrongInfoException;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.conversion.annotations.Conversion;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;
import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

@Conversion(
    conversions = {
         // key must be the name of a property for which converter should be used
         @TypeConversion(key = "birthday", converter = "com.luisfga.struts.converter.LocalDateStrutsTypeConverter")
    }
)
public class PasswordRecoverAction extends ActionSupport{

    @EJB PasswordRecoverUseCase passwordRecoverUseCase;
    
    private String email;
    public String getEmail() {
        return email;
    }
    @RequiredStringValidator(key = "validation.error.required")
    @EmailValidator(key = "validation.error.invalid.email")
    public void setEmail(String email) {
        this.email = email;
    }
    
    private LocalDate birthday;
    public LocalDate getBirthday() {
        return birthday;
    }
    @RequiredFieldValidator(key = "validation.error.required")
    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
    
    private String token;
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    
    @Action(value = "passwordRecoverInput", results={@Result(name="input", location="passwordRecover.jsp")})
    @Override
    public String input() {
        return INPUT;
    }
    
    @Action(value = "passwordRecover", 
            results = {
                @Result(name="input", location="passwordRecover.jsp"),
                @Result(name="success", location="index.jsp")
            })
    @Override
    public String execute() {
        
        try {
            AppUser appUser = passwordRecoverUseCase.prepareRecovery(email, birthday, token);
        
            String contextPath = ServletActionContext.getServletContext().getContextPath();
            passwordRecoverUseCase.enviarEmailResetSenha(contextPath, appUser, token);
        } catch (WrongInfoException wbException) {
            addActionError(getText("action.error.invalid.informations"));
            return INPUT;
            
        } catch (EmailConfirmationSendingException ex) {
            Logger.getLogger(PasswordRecoverAction.class.getName()).log(Level.SEVERE, "Erro ao tentar enviar email para reset de senha para o usuário {"+email+"}", ex);
            addActionError(getText("exception.email.confirmation.sending"));
            return INPUT;            
        }
        
        //enviar email para reset de senha
        addActionMessage(getText("action.message.reset.password.email.sent"));

        return SUCCESS;
    }
    
}