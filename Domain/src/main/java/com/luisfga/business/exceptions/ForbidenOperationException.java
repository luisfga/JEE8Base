package com.luisfga.business.exceptions;

import com.luisfga.business.PasswordResetUseCase;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ForbidenOperationException extends Exception {
    public ForbidenOperationException(String email){
        Logger.getLogger(PasswordResetUseCase.class.getName()).log(Level.SEVERE, "Tentativa suspeita de resetar senha do usu\u00e1rio '{'{0}'}'", email);
    }
}
