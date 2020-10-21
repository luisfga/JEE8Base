package com.luisfga.business;

import com.luisfga.business.entities.AppUser;
import com.luisfga.business.entities.AppUserOperationWindow;
import com.luisfga.business.exceptions.EmailConfirmationSendingException;
import com.luisfga.business.exceptions.WrongInfoException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.persistence.NoResultException;
import javax.persistence.Query;

@Stateless
public class PasswordRecoverUseCase extends UseCase{
    
    @EJB private MailHelper mailHelper;

    public void prepareRecovery(String email, LocalDate birthday, String token) 
            throws WrongInfoException {
        
        try {
            Query findByEmail = em.createNamedQuery("AppUser.findByEmail");
            findByEmail.setParameter("email", email);

            AppUser appUser = (AppUser) findByEmail.getSingleResult();
            
            if (!appUser.getBirthday().equals(birthday)) {
                throw new WrongInfoException();
            }

            //reaproveitamento de janela de operação. Caso já exista uma, apenas atualiza a existente ao invés de excluir e criar uma nova.
            AppUserOperationWindow operationWindow = em.find(AppUserOperationWindow.class, appUser.getEmail());
            appUser.setOperationWindow(operationWindow);
            if (appUser.getOperationWindow() == null) {
                operationWindow = new AppUserOperationWindow();
                operationWindow.setAppUser(appUser);
            }
            
            operationWindow.setWindowToken(token);
            operationWindow.setInitTime(OffsetDateTime.now());
            em.persist(operationWindow);
            
        } catch (NoResultException nrException) {
            throw new WrongInfoException();
            
        }
    }
    
    public void enviarEmailResetSenha(String contextName, String email, String windowToken) 
            throws EmailConfirmationSendingException{
        
        try {
            
            mailHelper.enviarEmailResetSenha(contextName, email, windowToken);
        } catch (MessagingException | UnsupportedEncodingException ex) {
            throw new EmailConfirmationSendingException();
        }
    }
    
}