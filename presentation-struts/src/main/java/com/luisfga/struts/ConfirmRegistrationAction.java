package com.luisfga.struts;

import com.luisfga.business.ConfirmRegistrationUseCase;
import com.luisfga.business.exceptions.CorruptedLinkageException;
import com.opensymphony.xwork2.ActionSupport;
import javax.ejb.EJB;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

public class ConfirmRegistrationAction extends ActionSupport{
    
    private String encodedUserEmail;
    public String getEncodedUserEmail() {
        return encodedUserEmail;
    }
    public void setEncodedUserEmail(String encodedUserEmail) {
        this.encodedUserEmail = encodedUserEmail;
    }

    @EJB private ConfirmRegistrationUseCase confirmRegistrationUseCase;
    
    @Action(value = "confirmRegistration", 
            results = {
                @Result(name="error", location="index.jsp"),
                @Result(name="success", location="login.jsp")
            })
    @Override
    public String execute() {
        
        try {
            confirmRegistrationUseCase.confirmRegistration(encodedUserEmail);
            
        } catch (CorruptedLinkageException clException) {
            addActionError(getText("action.error.email.is.empty"));
            return ERROR;
        }
        
        addActionMessage(getText("action.message.confirmation.completed"));
        
        return SUCCESS;
    }
    
}