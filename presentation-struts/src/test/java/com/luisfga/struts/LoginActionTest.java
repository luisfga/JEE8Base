package com.luisfga.struts;

import com.luisfga.business.LoginUseCase;
import com.luisfga.business.exceptions.EmailConfirmationSendingException;
import com.luisfga.business.exceptions.LoginException;
import com.luisfga.business.exceptions.PendingEmailConfirmationShiroAuthenticationException;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ActionSupport;
import java.util.List;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.junit.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public class LoginActionTest extends AbstractComposedBaseTest{
    
    @Test
    public void testExecute_LoginInput() throws Exception{
        
        ActionMapping mapping = getActionMapping("/loginInput");
        assertNotNull(mapping);
        
        //get action by path
        ActionProxy actionProxy = getActionProxy("/loginInput");
        assertNotNull(actionProxy);

        LoginAction action = (LoginAction) actionProxy.getAction();
        assertNotNull("The action is null but should not be.", action);
        
        String result = actionProxy.execute();
        assertEquals("The execute method did not return " + ActionSupport.INPUT + " but should have.", ActionSupport.INPUT, result);
        
    }
    
   @Test
    public void testExecute_LoginException() throws Exception{
        
        request.setParameter("email", "test@test.test");
        request.setParameter("password", "123");
        
        ActionMapping mapping = getActionMapping("/login");
        assertNotNull(mapping);
        
        //get action by path
        ActionProxy actionProxy = getActionProxy("/login");
        assertNotNull(actionProxy);

        LoginAction action = (LoginAction) actionProxy.getAction();
        assertNotNull("The action is null but should not be.", action);
        
        //mock business tier
        LoginUseCase useCase = mock(LoginUseCase.class);
        action.loginUseCase = useCase;

        //simulate handling exception thrown by business logic
        doThrow(LoginException.class).when(useCase).login(any(), any());
        
        String result = actionProxy.execute();
        
        //check if error was handled
        assertEquals("The execute method did not return " + ActionSupport.ERROR + " but should have.", ActionSupport.ERROR, result);
        
        //check action messages
        assertEquals("Erro nas mensagens de erro da action",action.getActionErrors().size(), 1);
        assertEquals("Erro na mensagens da action",action.getActionMessages().size(), 0);
        
        List<String> actionErrors = (List) action.getActionErrors();
        assertEquals("Mensagem de erro está diferente", actionErrors.get(0), action.getText("action.error.authentication.exception"));
    }
    
   @Test
    public void testExecute_PendingEmailConfirmationException() throws Exception{
        
        request.setParameter("email", "test@test.test");
        request.setParameter("password", "123");
        
        ActionMapping mapping = getActionMapping("/login");
        assertNotNull(mapping);
        
        //get action by path
        ActionProxy actionProxy = getActionProxy("/login");
        assertNotNull(actionProxy);

        LoginAction action = (LoginAction) actionProxy.getAction();
        assertNotNull("The action is null but should not be.", action);
        
        //mock business tier
        LoginUseCase useCase = mock(LoginUseCase.class);
        action.loginUseCase = useCase;

        //simulate handling exception thrown by business logic
        doThrow(PendingEmailConfirmationShiroAuthenticationException.class).when(useCase).login(any(), any());
        
        String result = actionProxy.execute();
        
        //check if error was handled
        assertEquals("The execute method did not return " + ActionSupport.ERROR + " but should have.", ActionSupport.ERROR, result);
        
        //check action messages
        assertEquals("Erro nas mensagens de erro da action",action.getActionErrors().size(), 1);
        assertEquals("Erro na mensagens da action",action.getActionMessages().size(), 0);
        
        List<String> actionErrors = (List) action.getActionErrors();
        assertEquals("Mensagem de erro está diferente", actionErrors.get(0), action.getText("action.error.pending.email.confirmation"));
    }
    
    
   @Test
    public void testExecute_EmailConfirmationSendingException_after_PendingEmailConfirmationException() throws Exception{
        
        request.setParameter("email", "test@test.test");
        request.setParameter("password", "123");
        
        ActionMapping mapping = getActionMapping("/login");
        assertNotNull(mapping);
        
        //get action by path
        ActionProxy actionProxy = getActionProxy("/login");
        assertNotNull(actionProxy);

        LoginAction action = (LoginAction) actionProxy.getAction();
        assertNotNull("The action is null but should not be.", action);
        
        //mock business tier
        LoginUseCase useCase = mock(LoginUseCase.class);
        action.loginUseCase = useCase;

        //simulate handling exception thrown by business logic
        doThrow(PendingEmailConfirmationShiroAuthenticationException.class).when(useCase).login(any(), any());
        doThrow(EmailConfirmationSendingException.class).when(useCase).enviarEmailConfirmacaoNovoUsuario(any(), any());
        
        String result = actionProxy.execute();
        
        //check if error was handled
        assertEquals("The execute method did not return " + ActionSupport.ERROR + " but should have.", ActionSupport.ERROR, result);
        
        //check action messages
        assertEquals("Erro nas mensagens de erro da action",action.getActionErrors().size(), 2);
        assertEquals("Erro na mensagens da action",action.getActionMessages().size(), 0);
        
        List<String> actionErrors = (List) action.getActionErrors();
        assertEquals("Mensagem de erro está diferente", actionErrors.get(0), action.getText("action.error.pending.email.confirmation"));
        assertEquals("Mensagem de erro está diferente", actionErrors.get(1), action.getText("exception.email.confirmation.sending"));
    }
    
   @Test
    public void testExecute() throws Exception{
        
        request.setParameter("email", "test@test.test");
        request.setParameter("password", "123");
        
        ActionMapping mapping = getActionMapping("/login");
        assertNotNull(mapping);
        
        //get action by path
        ActionProxy actionProxy = getActionProxy("/login");
        assertNotNull(actionProxy);

        LoginAction action = (LoginAction) actionProxy.getAction();
        assertNotNull("The action is null but should not be.", action);
        
        //mock business tier
        LoginUseCase useCase = mock(LoginUseCase.class);
        action.loginUseCase = useCase;

        String result = actionProxy.execute();
        assertEquals("The execute method did not return " + ActionSupport.SUCCESS + " but should have.", ActionSupport.SUCCESS, result);

    }
    
}
