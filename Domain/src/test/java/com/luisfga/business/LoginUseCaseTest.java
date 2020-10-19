package com.luisfga.business;

import com.luisfga.business.entities.AppUser;
import com.luisfga.shiro.ApplicationShiroJdbcRealm;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.security.auth.login.LoginException;
import static junit.framework.TestCase.fail;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.session.HttpServletSession;
import static org.mockito.Mockito.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LoginUseCaseTest extends AbstractShiroEnabledComposedBaseTest{
    
    @EJB TestSupportBean testSupportBean;
    
    @EJB LoginUseCase loginUseCase;
    
    @Before
    public void setUp() {
        testSupportBean.loadUser();
    }
    
    @After
    public void tearDown(){
        testSupportBean.clearData();
    }
    
    @After
    public void tearDownSubject() {
        //3. Unbind the subject from the current thread:
        clearSubject();
    }
    
    @Test
    public void testLogin() throws Exception {
        
        //The steps 1 and 2, as demonstrated on Shiro's docs, works so a call to "SecurityUtils.getSubject();" can return a Subject
        
        //https://shiro.apache.org/testing.html
        //1.  Create a mock authenticated Subject instance for the test to run:
        Subject subjectUnderTest = mock(Subject.class);
        
        //2. Bind the subject to the current thread:
        setSubject(subjectUnderTest);

        //perform test logic here.  Any call to
        //SecurityUtils.getSubject() directly (or nested in the
        //call stack) will work properly.
        
        //well, there's a call to session for storing bean of logged AppUser, so... mock it
        when(subjectUnderTest.getSession()).thenReturn(mock(HttpServletSession.class));
        
        loginUseCase.login(testSupportBean.getEmail(), testSupportBean.getPassword());
    }
    
    @Test
    public void testLogin_ThrowingPendingEmailConfirmationException() throws Exception {
        
        //needed by SecurityManager (you shall not touch this!)
        Subject subjectUnderTest = mock(Subject.class);
        setSubject(subjectUnderTest);

        //simulate login throwing PendingEmailConfirmationException
        doThrow(ApplicationShiroJdbcRealm.PendingEmailConfirmationException.class).when(subjectUnderTest).login(any(UsernamePasswordToken.class));

        try {
            loginUseCase.login(testSupportBean.getEmail(), testSupportBean.getPassword());
        } catch (EJBException ex) {
            if(ex.getCause() instanceof ApplicationShiroJdbcRealm.PendingEmailConfirmationException){
                return;
            } else {
                fail("Should throw EJBException (with cause:AuthenticationException) but was with another one");
            }
        } catch (Exception ex) {
            fail("Should throw EJBException (with cause:PendingEmailConfirmationException)");
        } 
        
        fail("Should throw EJBException (with cause:PendingEmailConfirmationException)");
        
    }
    
    @Test
    public void testLogin_ThrowingLoginException() throws Exception {
        
        //needed by SecurityManager (you shall not touch this!)
        Subject subjectUnderTest = mock(Subject.class);
        setSubject(subjectUnderTest);

        //simulate login throwing UnknownAccountException
        doThrow(UnknownAccountException.class).when(subjectUnderTest).login(any(UsernamePasswordToken.class));

        try {
            loginUseCase.login(testSupportBean.getEmail(), testSupportBean.getPassword());
        } catch (LoginException ex) {
            //no op
        } catch (Exception ex) {
            fail("Should throw LoginException");
        }
        
        //simulate login throwing IncorrectCredentialsException
        doThrow(IncorrectCredentialsException.class).when(subjectUnderTest).login(any(UsernamePasswordToken.class));

        try {
            loginUseCase.login(testSupportBean.getEmail(), testSupportBean.getPassword());
        } catch (LoginException ex) {
            //no op
        } catch (Exception ex) {
            fail("Should throw LoginException");
        }
        
        //simulate login throwing LockedAccountException
        doThrow(LockedAccountException.class).when(subjectUnderTest).login(any(UsernamePasswordToken.class));

        try {
            loginUseCase.login(testSupportBean.getEmail(), testSupportBean.getPassword());
        } catch (LoginException ex) {
            //no op
        } catch (Exception ex) {
            fail("Should throw LoginException");
        }
        
        //simulate login throwing ExcessiveAttemptsException
        doThrow(ExcessiveAttemptsException.class).when(subjectUnderTest).login(any(UsernamePasswordToken.class));

        try {
            loginUseCase.login(testSupportBean.getEmail(), testSupportBean.getPassword());
        } catch (LoginException ex) {
            //no op
        } catch (Exception ex) {
            fail("Should throw LoginException");
        }
    }
    
    @Test
    public void testLogin_ThrowingAuthenticationException() throws Exception {
        
        //needed by SecurityManager (you shall not touch this!)
        Subject subjectUnderTest = mock(Subject.class);
        setSubject(subjectUnderTest);

        //simulate login throwing PendingEmailConfirmationException
        doThrow(AuthenticationException.class).when(subjectUnderTest).login(any(UsernamePasswordToken.class));

        try {
            loginUseCase.login(testSupportBean.getEmail(), testSupportBean.getPassword());
        } catch (EJBException ex) {
            if(ex.getCause() instanceof AuthenticationException){
                return;
            } else {
                fail("Should throw EJBException (with cause:AuthenticationException) but was with another one");
            }
        } catch (Exception ex) {
            fail("Should throw EJBException (with cause:AuthenticationException)");
        } 
        
        fail("Should throw EJBException (with cause:AuthenticationException)");
        
    }

    /**
     * Test of loadUser method, of class LoginUseCase.
     */
    @Test
    public void testLoadUser() throws Exception {
        
        AppUser appUser = loginUseCase.loadUser(testSupportBean.getEmail());
        
        assertNotNull(appUser);
        
    }
    
}
