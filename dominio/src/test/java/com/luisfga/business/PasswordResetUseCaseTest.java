package com.luisfga.business;

import com.luisfga.business.entities.AppUser;
import com.luisfga.business.exceptions.ForbidenOperationException;
import com.luisfga.business.exceptions.InvalidDataException;
import com.luisfga.business.exceptions.TimeHasExpiredException;
import java.util.Base64;
import javax.ejb.EJB;
import javax.persistence.Query;
import static junit.framework.TestCase.assertTrue;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PasswordResetUseCaseTest extends AbstractComposedBaseTest{
    
    @EJB TestSupportBean testSupportEJB;
    
    @EJB PasswordResetUseCase passwordResetUseCase;
    
    @Before
    public void loadData() throws Exception {
        
        //usuário e sua janela de operação, que ao final devem funcionar
        testSupportEJB.loadUser();
        testSupportEJB.loadUserOpWindow();
        
        //usuário sem janela de operação
        testSupportEJB.loadUserWithoutOpWindow();
        
        //usuário com janela de operação expirada
        testSupportEJB.loadUserWithExpiredWindow();
        testSupportEJB.loadExpiredOpWindow();
    }
    
    @After
    public void clearData(){
        testSupportEJB.clearData();
    }
  
    @Test
    public void testNullEmailShallThrowInvalidDataException(){
        try {
            passwordResetUseCase.validateOperationWindow(null, "xpto");
        } catch (InvalidDataException ex) {
            return;
        } catch (Exception ex) {
        }
        fail("Null Email Shall Throw InvalidDataException");
    }

    @Test
    public void testEmptyEmailShallThrowInvalidDataException(){
        try {
            passwordResetUseCase.validateOperationWindow("", "xpto");
        } catch (InvalidDataException ex) {
            return;
        } catch (Exception ex) {
        }
        fail("Empty Email Shall Throw InvalidDataException");
    }
    
    @Test
    public void testNullTokenShallThrowInvalidDataException(){
        try {
            passwordResetUseCase.validateOperationWindow("xpto", null);
        } catch (InvalidDataException ex) {
            return;
        } catch (Exception ex) {
        }
        fail("Null Token Shall Throw InvalidDataException");
    }
    
    @Test
    public void testEmptyTokenShallThrowInvalidDataException(){
        try {
            passwordResetUseCase.validateOperationWindow("xpto", "");
        } catch (InvalidDataException ex) {
            return;
        } catch (Exception ex) {
        }
        fail("Empty Token Shall Throw InvalidDataException");
    }
    
    @Test
    public void testUnencodedEmailAddressShallThrowInvalidDataException() {
        try {
            passwordResetUseCase.validateOperationWindow(testSupportEJB.getEmail(), "xpto");
        } catch (InvalidDataException ex) {
            return;
        } catch (Exception ex) {
        }
        fail("Unencoded Email Address Shall Throw InvalidDataException");
    }
    
    @Test
    public void testWrongEmailAddressShallThrowInvalidDataException() {
        try {
            byte[] encodedEmail = Base64.getEncoder().encode("another@random.email".getBytes());
            passwordResetUseCase.validateOperationWindow(new String(encodedEmail), "xpto");
        } catch (InvalidDataException ex) {
            return;
        } catch (Exception ex) {
        }
        fail("Wrong Email Address Shall Throw InvalidDataException");
    }
    
    @Test
    public void testExpiredOpWindowShallThrowTimeHasExpiredException(){
        try {
            byte[] encodedEmail = Base64.getEncoder().encode(testSupportEJB.getEmailWithExpiredOpWindow().getBytes());
            passwordResetUseCase.validateOperationWindow(new String(encodedEmail), testSupportEJB.getExpiredOpWindowToken());
        } catch (TimeHasExpiredException ex) {
            return;
        } catch (Exception ex) {
        }
        fail("Expired OpWindow Shall Throw TimeHasExpiredException");
    }
    
    @Test
    public void testNullWindowShallThrowForbidenException() {
        try {
            byte[] encodedEmail = Base64.getEncoder().encode(testSupportEJB.getEmailWithoutOpWindow().getBytes());
            passwordResetUseCase.validateOperationWindow(new String(encodedEmail), "xpto");
        } catch (ForbidenOperationException ex) {
            return;
        } catch (Exception ex) {
        }
        fail("Null Window Shall Throw ForbidenOperationException");
    }
    
    @Test
    public void testWrongTokenShallThrowForbidenOperationException(){
        try {
            byte[] encodedEmail = Base64.getEncoder().encode(testSupportEJB.getEmail().getBytes());
            passwordResetUseCase.validateOperationWindow(new String(encodedEmail), "some_wrong_token");
        } catch (ForbidenOperationException ex) {
            return;
        } catch (Exception ex) {
        }
        fail("Wrong Token Shall Throw ForbidenOperationException");
    }
  
    @Test
    public void testValidateOperationWindow() throws Exception {

        byte[] encodedEmail = Base64.getEncoder().encode(testSupportEJB.getEmail().getBytes());
        String result = passwordResetUseCase.validateOperationWindow(new String(encodedEmail), testSupportEJB.getWindowToken());
        
        assertNotNull(result);
    }
    

    /**
     * Test of resetPassword method, of class PasswordResetUseCase.
     */
    @Test
    public void testResetPassword() throws Exception {
        
        passwordResetUseCase.resetPassword(testSupportEJB.getEmail(), "new_password");
        
        //conferir se está salvo com o novo password
        Query findByEmail = em.createNamedQuery("AppUser.findByEmail");
        findByEmail.setParameter("email", testSupportEJB.getEmail());
        AppUser user = (AppUser) findByEmail.getSingleResult();//sem try, se der erro apenas falha o teste.
        
        //Apache Shiro acoplado. Fazer o quê? \o/
        DefaultPasswordService defaultPasswordService = new DefaultPasswordService();
        assertTrue(defaultPasswordService.passwordsMatch("new_password", user.getPassword()));
        
    }
    
}
