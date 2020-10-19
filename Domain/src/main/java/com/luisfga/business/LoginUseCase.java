package com.luisfga.business;

import com.luisfga.business.entities.AppUser;
import com.luisfga.business.exceptions.PendingEmailConfirmationException;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.security.auth.login.LoginException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

@Stateless //TODO atenção para os testes. Ver se pode ser Stateless mesmo ou se tem que ser Statefull
public class LoginUseCase extends UseCase{
    
    public void login(String email, String password) throws EJBException, LoginException, PendingEmailConfirmationException, AuthenticationException {

        UsernamePasswordToken authToken = new UsernamePasswordToken(email, password);
        authToken.setRememberMe(false);
        
        Subject currentUser = SecurityUtils.getSubject();
        
        try {
            currentUser.login(authToken);
            
        } catch ( UnknownAccountException | IncorrectCredentialsException | LockedAccountException | ExcessiveAttemptsException ice ) {
            throw new LoginException();
        }

        AppUser appUser = loadUser(authToken.getUsername());
        currentUser.getSession().setAttribute("userBean", appUser);
    }
    
    public AppUser loadUser(String email){
        //tenta recuperar usuário pelo email
        Query findByEmail = em.createNamedQuery("AppUser.findByEmail");
        findByEmail.setParameter("email", email);
        AppUser appUser = (AppUser) findByEmail.getSingleResult();
        
        return appUser;
    }

}
