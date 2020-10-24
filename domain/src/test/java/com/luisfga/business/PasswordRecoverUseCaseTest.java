package com.luisfga.business;

import com.luisfga.business.entities.AppUserOperationWindow;
import com.luisfga.business.exceptions.WrongInfoException;
import java.time.LocalDate;
import javax.ejb.EJB;
import static junit.framework.TestCase.fail;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PasswordRecoverUseCaseTest extends AbstractComposedBaseTest{
    
    @EJB TestSupportBean testSupportBean;
    
    @EJB PasswordRecoverUseCase passwordRecoverUseCase;
    
    @Before
    @Override
    public void setUp() {
        testSupportBean.loadUser();
        testSupportBean.loadUserOpWindow();
    }
    
    @After
    @Override
    public void tearDown() {
        testSupportBean.clearData();
    }

    @Test
    public void testPrepareRecovery_NonExistingEmailShallThrowWrongInfoException(){
        
        try {
            passwordRecoverUseCase.prepareRecovery("any@wrong.email", LocalDate.MIN, "some_wrong_token");
        } catch (WrongInfoException ex) { 
            return;
        }
        fail("Should have thrown WrongInfoException");
    }
    
    @Test
    public void testPrepareRecovery_WrongBirthdatShallThrowWrongInfoException(){
        try {
            passwordRecoverUseCase.prepareRecovery(testSupportBean.getEmail(), LocalDate.parse("2000-01-01"), "random_token");
        } catch (WrongInfoException ex) {
            return;
        }
        fail("Should have thrown WrongInfoException");
    }
    
    @Test
    public void testPrepareRecovery_ShallReuseExistingWindowUpdatingIt(){
        
        try {
            AppUserOperationWindow opWindow = em.find(AppUserOperationWindow.class, testSupportBean.getEmail());
            assertEquals(opWindow.getInitTime(),testSupportBean.getWindowInitTime());
            assertEquals(opWindow.getWindowToken(), testSupportBean.getWindowToken());
            
            passwordRecoverUseCase.prepareRecovery(
                    testSupportBean.getEmail(), 
                    testSupportBean.getBirthday(), 
                    "new_window_token");
            
            //check if data was updated
            AppUserOperationWindow reloadedOpWindow = em.find(AppUserOperationWindow.class, testSupportBean.getEmail());
            Assert.assertNotEquals(reloadedOpWindow.getInitTime(),testSupportBean.getWindowInitTime());
            Assert.assertNotEquals(reloadedOpWindow.getWindowToken(), testSupportBean.getWindowToken());
            
        } catch (Exception ex) {
            fail("Should not have thrown Exception");
        }
    }
    
}
