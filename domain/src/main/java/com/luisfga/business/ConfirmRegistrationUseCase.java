package com.luisfga.business;

import com.luisfga.business.entities.AppUser;
import com.luisfga.business.exceptions.CorruptedLinkageException;
import java.util.Base64;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

@Stateless
public class ConfirmRegistrationUseCase extends UseCase{

    public void confirmRegistration(String encodedEmail) 
            throws CorruptedLinkageException, IllegalArgumentException{
        
        if (encodedEmail == null || encodedEmail.isEmpty()) {
            throw new CorruptedLinkageException();
        }
        
        try {
            byte[] decodedEmailBytes = Base64.getDecoder().decode(encodedEmail);
            String email = new String(decodedEmailBytes);
            
            Query findByEmail = em.createNamedQuery("AppUser.findByEmail");
            findByEmail.setParameter("email", email);
            AppUser appUser = (AppUser) findByEmail.getSingleResult();
            
            appUser.setStatus("ok");//seta status para OK, i.e. CONFIRMADO
            em.persist(appUser);
            
        } catch (NoResultException nrException) {
            throw new CorruptedLinkageException();
            
        } catch (IllegalArgumentException iae) {
            throw iae;
        }
    }
    
}
