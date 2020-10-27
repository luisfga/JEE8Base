package com.luisfga.struts;

import com.luisfga.business.PasswordRecoverUseCase;
import com.luisfga.business.exceptions.EmailConfirmationSendingException;
import com.luisfga.business.exceptions.WrongInfoException;
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

public class PasswordRecoverActionTest extends AbstractComposedBaseTest{
    
    @Test
    public void testPasswordRecoverInput() throws Exception{
        
        ActionMapping mapping = getActionMapping("/passwordRecoverInput");
        assertNotNull(mapping);
        
        //get action by path
        ActionProxy actionProxy = getActionProxy("/passwordRecoverInput");
        assertNotNull(actionProxy);

        PasswordRecoverAction action = (PasswordRecoverAction) actionProxy.getAction();
        assertNotNull("The action is null but should not be.", action);
        
        String result = actionProxy.execute();
        assertEquals("The execute method did not return " + ActionSupport.INPUT + " but should have.", ActionSupport.INPUT, result);
        
    }
    
    @Test
    public void testExecute_WrongInfoException() throws Exception{
        request.setParameter("email", "test@test.test");
        request.setParameter("birthday", "2020-10-01");
        request.setParameter("token", "123");
        
        ActionMapping mapping = getActionMapping("/passwordRecover");
        assertNotNull(mapping);
        
        //get action by path
        ActionProxy actionProxy = getActionProxy("/passwordRecover");
        assertNotNull(actionProxy);

        PasswordRecoverAction action = (PasswordRecoverAction) actionProxy.getAction();
        assertNotNull("The action is null but should not be.", action);
        
        //mock business tier
        PasswordRecoverUseCase useCase = mock(PasswordRecoverUseCase.class);
        action.passwordRecoverUseCase = useCase;

        //simulate handling exception thrown by business logic
        doThrow(WrongInfoException.class).when(useCase).prepareRecovery(any(), any(), any());
        
        String result = actionProxy.execute();
        
        //check if error was handled
        assertEquals("The execute method did not return " + ActionSupport.ERROR + " but should have.", ActionSupport.ERROR, result);
        
        //check action messages
        assertEquals("Erro nas mensagens de erro da action",action.getActionErrors().size(), 1);
        assertEquals("Erro na mensagens da action",action.getActionMessages().size(), 0);
        
        List<String> actionErrors = (List) action.getActionErrors();
        assertEquals("Mensagem de erro está diferente", actionErrors.get(0), action.getText("action.error.invalid.informations"));
    }
    
    @Test
    public void testExecute_EmailConfirmationSendingException() throws Exception{
        request.setParameter("email", "test@test.test");
        request.setParameter("birthday", "2020-10-01");
        request.setParameter("token", "123");
        
        ActionMapping mapping = getActionMapping("/passwordRecover");
        assertNotNull(mapping);
        
        //get action by path
        ActionProxy actionProxy = getActionProxy("/passwordRecover");
        assertNotNull(actionProxy);

        PasswordRecoverAction action = (PasswordRecoverAction) actionProxy.getAction();
        assertNotNull("The action is null but should not be.", action);
        
        //mock business tier
        PasswordRecoverUseCase useCase = mock(PasswordRecoverUseCase.class);
        action.passwordRecoverUseCase = useCase;

        //simulate handling exception thrown by business logic
        doThrow(EmailConfirmationSendingException.class).when(useCase).enviarEmailResetSenha(any(), any(), any());
        
        String result = actionProxy.execute();
        
        //check if error was handled
        assertEquals("The execute method did not return " + ActionSupport.ERROR + " but should have.", ActionSupport.ERROR, result);
        
        //check action messages
        assertEquals("Erro nas mensagens de erro da action",action.getActionErrors().size(), 1);
        assertEquals("Erro na mensagens da action",action.getActionMessages().size(), 0);
        
        List<String> actionErrors = (List) action.getActionErrors();
        assertEquals("Mensagem de erro está diferente", actionErrors.get(0), action.getText("exception.email.confirmation.sending"));
    }
    
    @Test
    public void testExecute() throws Exception{
        request.setParameter("email", "test@test.test");
        request.setParameter("birthday", "2020-10-01");
        request.setParameter("token", "123");
        
        ActionMapping mapping = getActionMapping("/passwordRecover");
        assertNotNull(mapping);
        
        //get action by path
        ActionProxy actionProxy = getActionProxy("/passwordRecover");
        assertNotNull(actionProxy);

        PasswordRecoverAction action = (PasswordRecoverAction) actionProxy.getAction();
        assertNotNull("The action is null but should not be.", action);
        
        //mock business tier
        PasswordRecoverUseCase useCase = mock(PasswordRecoverUseCase.class);
        action.passwordRecoverUseCase = useCase;

        String result = actionProxy.execute();
        
        //check if error was handled
        assertEquals("The execute method did not return " + ActionSupport.SUCCESS + " but should have.", ActionSupport.SUCCESS, result);
        
        //check action messages
        assertEquals("Erro nas mensagens de erro da action",action.getActionErrors().size(), 0);
        assertEquals("Erro na mensagens da action",action.getActionMessages().size(), 1);
        
        List<String> actionMessages = (List) action.getActionMessages();
        assertEquals("Mensagem de erro está diferente", actionMessages.get(0), action.getText("action.message.reset.password.email.sent"));
    }
    
}
