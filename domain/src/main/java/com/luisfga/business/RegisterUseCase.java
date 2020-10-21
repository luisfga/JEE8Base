package com.luisfga.business;

import com.luisfga.business.entities.AppRole;
import com.luisfga.business.entities.AppUser;
import com.luisfga.business.exceptions.EmailAlreadyTakenException;
import com.luisfga.business.exceptions.EmailConfirmationSendingException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.apache.shiro.authc.credential.DefaultPasswordService;

@Stateless
public class RegisterUseCase extends UseCase {
    
    @EJB private MailHelper mailHelper;
    
    public void registerNewAppUser(String email, String password, String userName, LocalDate birthday) 
            throws EmailAlreadyTakenException {
        
        try {
            //verifica se o email informado está disponível
            Query checkIfExists = em.createNamedQuery("AppUser.checkIfExists");
            checkIfExists.setParameter("email", email);
            checkIfExists.getSingleResult();
            
            //se não lançou NoResultException é porque já existe tal email cadastrado
            throw new EmailAlreadyTakenException();
            
        } catch (NoResultException nrException) {
        }
        
        //configura novo usuário
        AppUser newAppUser = new AppUser();
        newAppUser.setEmail(email);
        newAppUser.setUserName(userName);
        newAppUser.setBirthday(birthday);
        newAppUser.setStatus("new");
        newAppUser.setJoinTime(OffsetDateTime.now());

        DefaultPasswordService defaultPasswordService = new DefaultPasswordService();
        String encryptedPassword = defaultPasswordService.encryptPassword(password);
        newAppUser.setPassword(encryptedPassword);
        
        //configura ROLE
        newAppUser.setRoles(getRolesForNewUser());

        //salva novo usuário
        em.persist(newAppUser);

    }
    
    public void enviarEmailConfirmacaoNovoUsuario(String contextPath, String email) throws EmailConfirmationSendingException{
        try {
            mailHelper.enviarEmailConfirmacaoNovoUsuario(contextPath, email);
            
        } catch (MessagingException | UnsupportedEncodingException ex) {
            throw new EmailConfirmationSendingException();
        }
    }
    
    private Set<AppRole> getRolesForNewUser(){
        try {
            Query findRegisteredUserRole = em.createNamedQuery("AppRole.findRegisteredUserRole");
            //Assumimos que um novo usuário não tem nada e sua lista de Roles está vazia.
            Set<AppRole> roles = new HashSet<>();
            roles.add((AppRole) findRegisteredUserRole.getSingleResult());
            return roles;
            
        } catch (NoResultException nrException) {
            //no op
        }
        return null;
    }
    
}