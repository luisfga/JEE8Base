package com.luisfga.business;

import com.luisfga.business.entities.AppUser;
import com.luisfga.business.exceptions.CorruptedLinkageException;
import java.util.Base64;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import static junit.framework.TestCase.assertEquals;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ConfirmRegistrationUseCaseTest extends AbstractComposedBaseTest{
    
    @EJB TestSupportBean testSupportBean;
    
    @EJB ConfirmRegistrationUseCase confirmRegistrationUseCase;
    
    @Before
    public void setUp() {
        testSupportBean.loadUser();
    }
    
    @After
    public void tearDown() {
        testSupportBean.clearData();
    }

    @Test
    public void testEmptyEmailShallThrowCorruptedLinkageException() throws Exception {
        
        try {
            confirmRegistrationUseCase.confirmRegistration("");
        } catch (CorruptedLinkageException ex) {
            return;
        }
        fail("Empty email should have thrown CorruptedLinkageException");
    }
    
    @Test
    public void testUnencodedEmailShallThrowCorruptedLinkageException(){
        
        try {
            confirmRegistrationUseCase.confirmRegistration("some@email.com");
        } catch (CorruptedLinkageException e){
            return;
            //else, head to fail
        } catch (Exception e){
            //no op, head to fail
        }
        fail("Unencoded email should have thrown CorruptedLinkageException");
    }
    
    @Test
    public void testWrongEmailShallThrowCorruptedLinkageException() throws Exception {
        
        try {
            String encodedEmail = Base64.getEncoder().encodeToString("some@wrong.email".getBytes());
            confirmRegistrationUseCase.confirmRegistration(encodedEmail);
        } catch (CorruptedLinkageException ex) {
            return;
        }
        fail("Wrong email should have thrown CorruptedLinkageException");
    }
    
    @Test
    public void testSuccessShallUpdateUserStatusToOK(){
        
        AppUser appUser = em.find(AppUser.class, testSupportBean.getEmail());
        assertEquals("new", appUser.getStatus());
        
        String encodedEmail = Base64.getEncoder().encodeToString(testSupportBean.getEmail().getBytes());
        try {
            confirmRegistrationUseCase.confirmRegistration(encodedEmail);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
        
        AppUser reloadedAppUser = em.find(AppUser.class, testSupportBean.getEmail());
        assertEquals("ok", reloadedAppUser.getStatus());
    }
}
