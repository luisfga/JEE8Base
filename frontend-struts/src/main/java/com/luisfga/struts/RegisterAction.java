package com.luisfga.struts;

import com.luisfga.business.RegisterUseCase;
import com.luisfga.business.entities.AppUser;
import com.luisfga.business.exceptions.EmailAlreadyTakenException;
import com.luisfga.business.exceptions.EmailConfirmationSendingException;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.conversion.annotations.Conversion;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;
import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.FieldExpressionValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;

import java.time.LocalDate;
import javax.ejb.EJB;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

@Conversion(
    conversions = {
         // key must be the name of a property for which converter should be used
         @TypeConversion(key = "birthday", converter = "com.luisfga.struts.converter.LocalDateStrutsTypeConverter")
    }
)
public class RegisterAction extends ActionSupport {

    private final Logger logger = LogManager.getLogger();
    
    private static final long serialVersionUID = 1L;
    
    @EJB RegisterUseCase registerUseCase;

    private String token;
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    private LocalDate birthday;
    public LocalDate getBirthday() {
        return birthday;
    }
    @RequiredFieldValidator(key = "validation.error.required")
    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
    
    private String userName;
    public String getUserName() {
        return userName;
    }
    @RequiredStringValidator(key = "validation.error.required")
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
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
    
    private String passwordConfirmation;
    public String getPasswordConfirmation() { return passwordConfirmation; }
    @RequiredStringValidator(key = "validation.error.required")
    @FieldExpressionValidator(expression = "password==passwordConfirmation", key = "validation.error.password.confirmation")
    public void setPasswordConfirmation(String passwordConfirmation) { this.passwordConfirmation = passwordConfirmation; }
    
    @Action(value = "registerInput", results={@Result(name="input", location="register.jsp")})
    @Override
    public String input() {
        return INPUT;
    }

    @Action(value = "register", 
        results = {
            @Result(name="error", location="register.jsp"),
            @Result(name="success", location="register.jsp")
        })
    @Override
    public String execute() {

        AppUser newAppUser;
        try {
            registerUseCase.registerNewAppUser(email, password, userName, birthday);

            //mensagem de sucesso na criação da conta
            addActionMessage(getText("action.message.account.created"));
            
            String contextPath = ServletActionContext.getServletContext().getContextPath();
            registerUseCase.enviarEmailConfirmacaoNovoUsuario(contextPath, email);
            
        } catch (EmailAlreadyTakenException ex) {
            addActionError(getText("validation.error.email.already.taken", new String[]{email}));
            addActionError(getText("validation.error.account.recovery.link"));

            return ERROR;
            
        } catch (EmailConfirmationSendingException ex) {
            logger.error("Erro ao tentar enviar email de confirmação para o usuário {"+email+"}", ex);
            addActionError(getText("exception.email.confirmation.sending"));
            return ERROR;            
        }
        
        return SUCCESS;

    }
}