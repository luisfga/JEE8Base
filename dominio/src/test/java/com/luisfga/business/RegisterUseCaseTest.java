package com.luisfga.business;

import com.luisfga.business.entities.AppUser;
import java.time.LocalDate;
import javax.ejb.EJB;
import javax.persistence.Query;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.junit.Test;

public class RegisterUseCaseTest extends AbstractComposedBaseTest{
    
    @EJB
    private RegisterUseCase useCase;

    /**
     * Test of registerNewAppUser method, of class RegisterUseCase.
     */
    @Test
    public void testRegisterNewAppUser() throws Exception {
        System.out.println("registerNewAppUser");
        String email = "test@gmail.com";
        String password = "123";
        String userName = "test";
        LocalDate birthday = LocalDate.now();
        
        useCase.registerNewAppUser(email, password, userName, birthday);

        //conferir se salvou usuário
        Query findByEmail = em.createNamedQuery("AppUser.findByEmail");
        findByEmail.setParameter("email", email);
        AppUser user = (AppUser) findByEmail.getSingleResult();//sem try, se der erro apenas falha o teste.
        
//        assertEquals(user.getEmail(), email); <- não precisa né, por favor! já está na Query

        //Apache Shiro acoplado. Fazer o quê? \o/
        DefaultPasswordService defaultPasswordService = new DefaultPasswordService();
        assertTrue(defaultPasswordService.passwordsMatch(password, user.getPassword()));
        
        assertEquals(user.getUserName(), userName);
        assertEquals(user.getBirthday(), birthday);
        
        //o registro tem que setar o status do novo usuário
        assertNotNull("Status não pode ser nulo", user.getStatus());
    }
    
}
