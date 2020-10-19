package com.luisfga.jsf;

import com.luisfga.business.ConfirmRegistrationUseCase;
import com.luisfga.business.exceptions.CorruptedLinkageException;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named
@RequestScoped
public class ConfirmRegistration {
    
    private String encodedUserEmail;
    public String getEncodedUserEmail() {
        return encodedUserEmail;
    }
    public void setEncodedUserEmail(String encodedUserEmail) {
        this.encodedUserEmail = encodedUserEmail;
    }
    
    
    @EJB ConfirmRegistrationUseCase confirmRegistrationUseCase;
    
    public String execute(){
        
        System.out.println("Encoded User Email = " + encodedUserEmail);
        
        try {
            confirmRegistrationUseCase.confirmRegistration(encodedUserEmail);
            
        } catch (CorruptedLinkageException clException) {
            String errorMessage = FacesContext.getCurrentInstance().getApplication().
                    getResourceBundle(FacesContext.getCurrentInstance(),"msg").
                    getString("action.error.email.is.empty");
            
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,errorMessage, errorMessage);
            
            FacesContext.getCurrentInstance().addMessage(null, message);
            
            return "login";
        }
        

        String successMessage = FacesContext.getCurrentInstance().getApplication().
                getResourceBundle(FacesContext.getCurrentInstance(),"msg").
                getString("action.message.confirmation.completed");

        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,successMessage, successMessage);

        FacesContext.getCurrentInstance().addMessage(null, message);
        
        return "login";

    }
    
}