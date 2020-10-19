package com.luisfga.jsf;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class Index implements Serializable{

    public String enter(){
        Logger.getLogger(Index.class.getName()).log(Level.INFO, "Index.enter()");
        return "login.input()";
    }
    
}
