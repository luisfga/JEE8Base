package com.luisfga.business;

import com.luisfga.business.entities.AppUser;
import com.luisfga.business.entities.AppUserOperationWindow;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class TestSupportBean {
    
    @PersistenceContext(unitName = "applicationJpaUnit")
    EntityManager em;
    
    //Usado para o usuário "completo" que funcionará na validação
    private final String email = "default@test.email";
    private final String password = "final_p@ssword";
    private final LocalDate birthday = LocalDate.parse("1981-06-03");
    private final String statusNew = "new";
    
    private final String windowToken = "123";
    private final OffsetDateTime windowInitTime = OffsetDateTime.now();
    
    //Usado para criar um usuário SEM janela de operação
    private final String emailWithoutOpWindow = "email@without.window";
    
    //Usado para um usuário com a janela de operação EXPIRADA (testar o limite de tempo)
    private final String emailWithExpiredOpWindow = "email@expired.window";
    private final String expiredOpWindowToken = "whatever";
    
    public void loadUser(){
        //insert user with operation window
        AppUser appUser = new AppUser();
        appUser.setEmail(email);
        appUser.setPassword(password);
        appUser.setBirthday(birthday);
        appUser.setStatus(statusNew);
        em.persist(appUser);
    }
    
    public void loadUserOpWindow(){
        Query findByEmail = em.createNamedQuery("AppUser.findByEmail");
        findByEmail.setParameter("email", email);
        AppUser appUser = (AppUser) findByEmail.getSingleResult();//sem try, se der erro apenas falha o teste.
        
        AppUserOperationWindow appUserOperationWindow = new AppUserOperationWindow();
        appUserOperationWindow.setAppUser(appUser);
        appUserOperationWindow.setInitTime(windowInitTime);
        appUserOperationWindow.setWindowToken(windowToken);
        em.persist(appUserOperationWindow);

    }
    
    public void loadUserWithoutOpWindow(){
        //insert only a user, without operation window
        AppUser appUserWithoutOpWindow = new AppUser();
        appUserWithoutOpWindow.setEmail(emailWithoutOpWindow);
        em.persist(appUserWithoutOpWindow);
    }
    
    public void loadUserWithExpiredWindow(){
        //insert user with operation window
        AppUser appUserWithExpiredWindow = new AppUser();
        appUserWithExpiredWindow.setEmail(emailWithExpiredOpWindow);
        em.persist(appUserWithExpiredWindow);
    }
    
    public void loadExpiredOpWindow(){
        Query findByEmail = em.createNamedQuery("AppUser.findByEmail");
        findByEmail.setParameter("email", emailWithExpiredOpWindow);
        AppUser appUserWithExpiredWindow = (AppUser) findByEmail.getSingleResult();//sem try, se der erro apenas falha o teste.
        
        AppUserOperationWindow appUserExpiredOpWindow = new AppUserOperationWindow();
        appUserExpiredOpWindow.setAppUser(appUserWithExpiredWindow);
        appUserExpiredOpWindow.setInitTime(OffsetDateTime.parse("2007-12-03T10:15:30+01:00"));
        appUserExpiredOpWindow.setWindowToken(expiredOpWindowToken);
        em.persist(appUserExpiredOpWindow);
        

    }
    
    public void clearData(){
        AppUser appUser = em.find(AppUser.class, email);
        em.remove(appUser);
        
        AppUser appUserWithoutOpWindow = em.find(AppUser.class, emailWithoutOpWindow);
        em.remove(appUserWithoutOpWindow);
        
        AppUser appUserWithExpiredWindow = em.find(AppUser.class, emailWithExpiredOpWindow);
        em.remove(appUserWithExpiredWindow);
        
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public String getWindowToken() {
        return windowToken;
    }

    public OffsetDateTime getWindowInitTime() {
        return windowInitTime;
    }

    public String getEmailWithoutOpWindow() {
        return emailWithoutOpWindow;
    }

    public String getEmailWithExpiredOpWindow() {
        return emailWithExpiredOpWindow;
    }

    public String getExpiredOpWindowToken() {
        return expiredOpWindowToken;
    }
    
    
}
