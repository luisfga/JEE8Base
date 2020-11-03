package com.luisfga.struts;

import com.luisfga.business.PasswordResetUseCase;
import com.luisfga.business.exceptions.ForbidenOperationException;
import com.luisfga.business.exceptions.TimeHasExpiredException;
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

public class PasswordResetActionTest extends AbstractComposedBaseTest{
    
    @Test
    public void testPasswordResetInput_ForbidenOperationException() throws Exception{
        ActionMapping mapping = getActionMapping("/passwordResetInput");
        assertNotNull(mapping);
        
        //get action by path
        ActionProxy actionProxy = getActionProxy("/passwordResetInput");
        assertNotNull(actionProxy);

        PasswordResetAction action = (PasswordResetAction) actionProxy.getAction();
        assertNotNull("The action is null but should not be.", action);
        
        //mock business tier
        PasswordResetUseCase useCase = mock(PasswordResetUseCase.class);
        action.passwordResetUseCase = useCase;

        //simulate handling exception thrown by business logic
        doThrow(ForbidenOperationException.class).when(useCase).validateOperationWindow(any(), any());
        
        String result = actionProxy.execute();
        assertEquals("The execute method did not return " + ActionSupport.ERROR + " but should have.", ActionSupport.ERROR, result);
        
        //check action messages
        assertEquals("Erro nas mensagens de erro da action",action.getActionErrors().size(), 1);
        assertEquals("Erro na mensagens da action",action.getActionMessages().size(), 0);
        
        List<String> actionErrors = (List) action.getActionErrors();
        assertEquals("Mensagem de erro está diferente", actionErrors.get(0), action.getText("action.error.forbiden.operation"));
    }
    
    @Test
    public void testPasswordResetInput_TimeHasExpiredException() throws Exception{
        ActionMapping mapping = getActionMapping("/passwordResetInput");
        assertNotNull(mapping);
        
        //get action by path
        ActionProxy actionProxy = getActionProxy("/passwordResetInput");
        assertNotNull(actionProxy);

        PasswordResetAction action = (PasswordResetAction) actionProxy.getAction();
        assertNotNull("The action is null but should not be.", action);
        
        //mock business tier
        PasswordResetUseCase useCase = mock(PasswordResetUseCase.class);
        action.passwordResetUseCase = useCase;

        //simulate handling exception thrown by business logic
        doThrow(TimeHasExpiredException.class).when(useCase).validateOperationWindow(any(), any());
        
        String result = actionProxy.execute();
        assertEquals("The execute method did not return " + ActionSupport.ERROR + " but should have.", ActionSupport.ERROR, result);
        
        //check action messages
        assertEquals("Erro nas mensagens de erro da action",action.getActionErrors().size(), 1);
        assertEquals("Erro na mensagens da action",action.getActionMessages().size(), 0);
        
        List<String> actionErrors = (List) action.getActionErrors();
        assertEquals("Mensagem de erro está diferente", actionErrors.get(0), action.getText("action.error.invalid.user.temp.window.token"));
    }
    
    @Test
    public void testPasswordResetInput_Exception() throws Exception{
        ActionMapping mapping = getActionMapping("/passwordResetInput");
        assertNotNull(mapping);
        
        //get action by path
        ActionProxy actionProxy = getActionProxy("/passwordResetInput");
        assertNotNull(actionProxy);

        PasswordResetAction action = (PasswordResetAction) actionProxy.getAction();
        assertNotNull("The action is null but should not be.", action);
        
        //mock business tier
        PasswordResetUseCase useCase = mock(PasswordResetUseCase.class);
        action.passwordResetUseCase = useCase;

        //simulate handling exception thrown by business logic
        doThrow(Exception.class).when(useCase).validateOperationWindow(any(), any());
        
        String result = actionProxy.execute();
        assertEquals("The execute method did not return " + ActionSupport.ERROR + " but should have.", ActionSupport.ERROR, result);
        
        //check action messages
        assertEquals("Erro nas mensagens de erro da action",action.getActionErrors().size(), 1);
        assertEquals("Erro na mensagens da action",action.getActionMessages().size(), 0);
        
        List<String> actionErrors = (List) action.getActionErrors();
        assertEquals("Mensagem de erro está diferente", actionErrors.get(0), action.getText("exception.unknown"));
    }
    
    @Test
    public void testPasswordResetInput() throws Exception{
        ActionMapping mapping = getActionMapping("/passwordResetInput");
        assertNotNull(mapping);
        
        //get action by path
        ActionProxy actionProxy = getActionProxy("/passwordResetInput");
        assertNotNull(actionProxy);

        PasswordResetAction action = (PasswordResetAction) actionProxy.getAction();
        assertNotNull("The action is null but should not be.", action);
        
        //mock business tier
        PasswordResetUseCase useCase = mock(PasswordResetUseCase.class);
        action.passwordResetUseCase = useCase;

        String result = actionProxy.execute();
        assertEquals("The execute method did not return " + ActionSupport.INPUT + " but should have.", ActionSupport.INPUT, result);
        
        //check action messages
        assertEquals("Erro nas mensagens de erro da action",action.getActionErrors().size(), 0);
        assertEquals("Erro na mensagens da action",action.getActionMessages().size(), 0);
        
    }
    
    @Test
    public void testExecute() throws Exception{
        request.setParameter("email", "test@test.test");
        request.setParameter("password", "123");
        request.setParameter("passwordConfirmation", "123");
        
        ActionMapping mapping = getActionMapping("/passwordReset");
        assertNotNull(mapping);
        
        //get action by path
        ActionProxy actionProxy = getActionProxy("/passwordReset");
        assertNotNull(actionProxy);

        PasswordResetAction action = (PasswordResetAction) actionProxy.getAction();
        assertNotNull("The action is null but should not be.", action);
        
        //mock business tier
        PasswordResetUseCase useCase = mock(PasswordResetUseCase.class);
        action.passwordResetUseCase = useCase;

        String result = actionProxy.execute();
        
        //check if error was handled
        assertEquals("The execute method did not return " + ActionSupport.SUCCESS + " but should have.", ActionSupport.SUCCESS, result);
        
        //check action messages
        assertEquals("Erro nas mensagens de erro da action",action.getActionErrors().size(), 0);
        assertEquals("Erro na mensagens da action",action.getActionMessages().size(), 1);
        
        List<String> actionMessages = (List) action.getActionMessages();
        assertEquals("Mensagem de erro está diferente", actionMessages.get(0), action.getText("action.message.password.reset"));
    }
}
