package com.luisfga.jsf;

import com.luisfga.business.LoginUseCase;
import com.luisfga.business.exceptions.EmailConfirmationSendingException;
import com.luisfga.business.exceptions.LoginException;
import com.luisfga.business.exceptions.PendingEmailConfirmationException;
import java.lang.reflect.Method;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.naming.Context;
import javax.servlet.ServletContext;
import org.apache.myfaces.test.mock.MockExternalContext;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class LoginTest extends AbstractComposedBaseTest{

    Login bean;
    
    @Before
    public void before(){
        bean = new Login();
        bean.loginUseCase = mock(LoginUseCase.class);
    }
    
    @Test
    public void testExecute_LoginException() throws Exception{

        doThrow(LoginException.class).when(bean.loginUseCase).login(any(), any());

        String result = bean.execute();
        
        assertEquals("Retorno diferente do esperado", "login", result);
        
        //conferir mensagens
        assertEquals("Qtd. incorreta de mensagens", 1, FacesContext.getCurrentInstance().getMessageList().size());
        
        String faceMsg = FacesContext.getCurrentInstance().getMessageList().get(0).getSummary();
        String expectedMsg = ResourceBundle.getBundle("global").getString("action.error.authentication.exception");
        assertEquals("Mensagem incorreta", expectedMsg, faceMsg);
    }
    
    @Test
    public void testExecute_PendingEmailConfirmationException() throws Exception{
        
        doThrow(PendingEmailConfirmationException.class).when(bean.loginUseCase).login(any(), any());

        MockExternalContext extCtx = mock(MockExternalContext.class);
        facesContext.setExternalContext(extCtx);
        
        ServletContext ctx = mock(ServletContext.class);
        doReturn(ctx).when(facesContext.getExternalContext()).getContext();

        doReturn("").when(ctx).getContextPath();

//        doNothing().when(bean.loginUseCase).enviarEmailConfirmacaoNovoUsuario(any(), any());
        
        String result = bean.execute();
        
        assertEquals("Retorno diferente do esperado", "login", result);
        
        //conferir mensagens
        assertEquals("Qtd. incorreta de mensagens", 1, FacesContext.getCurrentInstance().getMessageList().size());
        
        String faceMsg = FacesContext.getCurrentInstance().getMessageList().get(0).getSummary();
        String expectedMsg = ResourceBundle.getBundle("global").getString("action.error.pending.email.confirmation");
        assertEquals("Mensagem incorreta", expectedMsg, faceMsg);
    }
    
    @Test
    public void testExecute_EmailConfirmationSendingException() throws Exception{
        
        doThrow(PendingEmailConfirmationException.class).when(bean.loginUseCase).login(any(), any());

        MockExternalContext extCtx = mock(MockExternalContext.class);
        facesContext.setExternalContext(extCtx);
        
        ServletContext ctx = mock(ServletContext.class);
        doReturn(ctx).when(facesContext.getExternalContext()).getContext();

        doReturn("").when(ctx).getContextPath();

        doThrow(EmailConfirmationSendingException.class).when(bean.loginUseCase).enviarEmailConfirmacaoNovoUsuario(any(), any());
        
        String result = bean.execute();
        
        assertEquals("Retorno diferente do esperado", "login", result);
        
        //conferir mensagens
        assertEquals("Qtd. incorreta de mensagens", 2, FacesContext.getCurrentInstance().getMessageList().size());
        
        String faceMsg = FacesContext.getCurrentInstance().getMessageList().get(0).getSummary();
        String expectedMsg = ResourceBundle.getBundle("global").getString("action.error.pending.email.confirmation");
        assertEquals("Mensagem incorreta", expectedMsg, faceMsg);
        
        String faceMsg2 = FacesContext.getCurrentInstance().getMessageList().get(1).getSummary();
        String expectedMsg2 = ResourceBundle.getBundle("global").getString("exception.email.confirmation.sending");
        assertEquals("Mensagem incorreta", expectedMsg2, faceMsg2);
    }
    
    @Test
    public void testExecute() throws Exception{
        
        String result = bean.execute();
        
        assertEquals("Retorno diferente do esperado", "/secure/dashboard?faces-redirect=true", result);
        
        //conferir mensagens
        assertEquals("Qtd. incorreta de mensagens", 0, FacesContext.getCurrentInstance().getMessageList().size());
        
    }
}
