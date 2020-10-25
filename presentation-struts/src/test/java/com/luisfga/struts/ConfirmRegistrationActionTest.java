package com.luisfga.struts;

import com.luisfga.business.ConfirmRegistrationUseCase;
import com.luisfga.business.exceptions.CorruptedLinkageException;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ActionSupport;
import java.util.List;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class ConfirmRegistrationActionTest extends AbstractComposedBaseTest {

    @Test
    public void testExecute_OnLinkageExceptionShallReturnERROR() throws Exception{

        ActionMapping mapping = getActionMapping("/confirmRegistration");
        assertNotNull(mapping);
        
        //get action by path
        ActionProxy actionProxy = getActionProxy("/confirmRegistration");
        assertNotNull(actionProxy);

        ConfirmRegistrationAction action = (ConfirmRegistrationAction) actionProxy.getAction();
        assertNotNull("The action is null but should not be.", action);
        
        //mock business tier
        ConfirmRegistrationUseCase useCase = mock(ConfirmRegistrationUseCase.class);
        action.confirmRegistrationUseCase = useCase;

        //simulate handling exception thrown by business logic
        doThrow(CorruptedLinkageException.class).when(useCase).confirmRegistration(any());
        
        //check if error was handled
        String result = actionProxy.execute();
        assertEquals("The execute method did not return " + ActionSupport.ERROR + " but should have.", ActionSupport.ERROR, result);
        
        //check action error message
        assertEquals("Erro nas mensagens de erro da action",action.getActionErrors().size(), 1);
        assertEquals("Erro na mensagens da action",action.getActionMessages().size(), 0);
        
        List<String> actionErrors = (List) action.getActionErrors();
        assertEquals("Mensagem de erro está diferente", actionErrors.get(0), action.getText("action.error.linkage.exception"));
    }

    @Test
    public void testExecute() throws Exception{

        ActionMapping mapping = getActionMapping("/confirmRegistration");
        assertNotNull(mapping);
        
        //get action by path
        ActionProxy actionProxy = getActionProxy("/confirmRegistration");
        assertNotNull(actionProxy);

        ConfirmRegistrationAction action = (ConfirmRegistrationAction) actionProxy.getAction();
        assertNotNull("The action is null but should not be.", action);

        //mock business tier
        ConfirmRegistrationUseCase confirmRegistrationUseCase = mock(ConfirmRegistrationUseCase.class);
        action.confirmRegistrationUseCase = confirmRegistrationUseCase;
        
        //simulate handling exception thrown by business logic
        doNothing().when(confirmRegistrationUseCase).confirmRegistration(any());
//        doThrow(NullPointerException.class).when(confirmRegistrationUseCase).confirmRegistration(any());
        
        //check if error was handled
        String result = actionProxy.execute();
        assertEquals("The execute method did not return " + ActionSupport.SUCCESS + " but should have.", ActionSupport.SUCCESS, result);
        
        //deve ter zero mensagens de erro e UMA mensagem de sucesso
        assertEquals("Erro nas mensagens de erro da action",action.getActionErrors().size(), 0);
        assertEquals("Erro na mensagens da action",action.getActionMessages().size(), 1);
        
        //confere se retorna a mensagem correta
        List<String> actionMessages = (List) action.getActionMessages();
        assertEquals("Mensagem de erro está diferente", actionMessages.get(0), action.getText("action.message.confirmation.completed"));
    }
}
