/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.luisfga.struts;

import com.luisfga.business.RegisterUseCase;
import com.luisfga.business.exceptions.EmailAlreadyTakenException;
import com.luisfga.business.exceptions.EmailConfirmationSendingException;
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

/**
 *
 * @author Luis
 */
public class RegisterActionTest extends AbstractComposedBaseTest{
    
    @Test
    public void testRegisterInput() throws Exception{
        
        ActionMapping mapping = getActionMapping("/registerInput");
        assertNotNull(mapping);
        
        //get action by path
        ActionProxy actionProxy = getActionProxy("/registerInput");
        assertNotNull(actionProxy);

        RegisterAction action = (RegisterAction) actionProxy.getAction();
        assertNotNull("The action is null but should not be.", action);
        
        String result = actionProxy.execute();
        assertEquals("The execute method did not return " + ActionSupport.INPUT + " but should have.", ActionSupport.INPUT, result);
        
    }
    
    @Test
    public void testExecute_EmailAlreadyTakenException() throws Exception{
        String email = "test@test.test";
        request.setParameter("email", email);
        request.setParameter("password", "123");
        request.setParameter("passwordConfirmation", "123");
        request.setParameter("birthday", "2020-10-01");
        request.setParameter("userName", "test");
        
        ActionMapping mapping = getActionMapping("/register");
        assertNotNull(mapping);
        
        //get action by path
        ActionProxy actionProxy = getActionProxy("/register");
        assertNotNull(actionProxy);

        RegisterAction action = (RegisterAction) actionProxy.getAction();
        assertNotNull("The action is null but should not be.", action);
        
        //mock business tier
        RegisterUseCase useCase = mock(RegisterUseCase.class);
        action.registerUseCase = useCase;

        //simulate handling exception thrown by business logic
        doThrow(EmailAlreadyTakenException.class).when(useCase).registerNewAppUser(any(), any(), any(), any());
        
        String result = actionProxy.execute();
        
        //check if error was handled
        assertEquals("The execute method did not return " + ActionSupport.ERROR + " but should have.", ActionSupport.ERROR, result);
        
        //check action messages
        assertEquals("Erro nas mensagens de erro da action",action.getActionErrors().size(), 2);
        assertEquals("Erro na mensagens da action",action.getActionMessages().size(), 0);
        
        List<String> actionErrors = (List) action.getActionErrors();
        assertEquals("Mensagem de erro está diferente", actionErrors.get(0), action.getText("validation.error.email.already.taken", new String[]{email}));
        assertEquals("Mensagem de erro está diferente", actionErrors.get(1), action.getText("validation.error.account.recovery.link"));
    }
    
    @Test
    public void testExecute_EmailConfirmationSendingException() throws Exception{
        String email = "test@test.test";
        request.setParameter("email", email);
        request.setParameter("password", "123");
        request.setParameter("passwordConfirmation", "123");
        request.setParameter("birthday", "2020-10-01");
        request.setParameter("userName", "test");
        
        ActionMapping mapping = getActionMapping("/register");
        assertNotNull(mapping);
        
        //get action by path
        ActionProxy actionProxy = getActionProxy("/register");
        assertNotNull(actionProxy);

        RegisterAction action = (RegisterAction) actionProxy.getAction();
        assertNotNull("The action is null but should not be.", action);
        
        //mock business tier
        RegisterUseCase useCase = mock(RegisterUseCase.class);
        action.registerUseCase = useCase;

        //simulate handling exception thrown by business logic
        doThrow(EmailConfirmationSendingException.class).when(useCase).enviarEmailConfirmacaoNovoUsuario(any(), any());
        
        String result = actionProxy.execute();
        
        //check if error was handled
        assertEquals("The execute method did not return " + ActionSupport.ERROR + " but should have.", ActionSupport.ERROR, result);
        
        //check action messages
        assertEquals("Erro nas mensagens de erro da action",action.getActionErrors().size(), 1);
        assertEquals("Erro na mensagens da action",action.getActionMessages().size(), 1);
        
        List<String> actionErrors = (List) action.getActionErrors();
        List<String> actionMessages = (List) action.getActionMessages();
        assertEquals("Mensagem de erro está diferente", actionErrors.get(0), action.getText("exception.email.confirmation.sending"));
        assertEquals("Mensagem está diferente", actionMessages.get(0), action.getText("action.message.account.created"));
                
    }
    
    @Test
    public void testExecute() throws Exception{
        String email = "test@test.test";
        request.setParameter("email", email);
        request.setParameter("password", "123");
        request.setParameter("passwordConfirmation", "123");
        request.setParameter("birthday", "2020-10-01");
        request.setParameter("userName", "test");
        
        ActionMapping mapping = getActionMapping("/register");
        assertNotNull(mapping);
        
        //get action by path
        ActionProxy actionProxy = getActionProxy("/register");
        assertNotNull(actionProxy);

        RegisterAction action = (RegisterAction) actionProxy.getAction();
        assertNotNull("The action is null but should not be.", action);
        
        //mock business tier
        RegisterUseCase useCase = mock(RegisterUseCase.class);
        action.registerUseCase = useCase;
        
        String result = actionProxy.execute();
        
        //check if error was handled
        assertEquals("The execute method did not return " + ActionSupport.SUCCESS + " but should have.", ActionSupport.SUCCESS, result);
        
        //check action messages
        assertEquals("Erro nas mensagens de erro da action",action.getActionErrors().size(), 0);
        assertEquals("Erro na mensagens da action",action.getActionMessages().size(), 1);
        
        List<String> actionMessages = (List) action.getActionMessages();
        assertEquals("Mensagem está diferente", actionMessages.get(0), action.getText("action.message.account.created"));
                
    }
}
