package com.luisfga.struts.config;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.Result;
import com.opensymphony.xwork2.UnknownHandler;
import com.opensymphony.xwork2.XWorkException;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.struts2.ServletActionContext;

public class ApplicationUnknownHandler implements UnknownHandler{

    private void redirectToContextRoot(){
        try {
            ServletActionContext.getResponse().sendRedirect(ServletActionContext.getRequest().getContextPath());
        } catch (IOException ex) {
            Logger.getLogger(ApplicationUnknownHandler.class.getName()).log(Level.SEVERE, null, ex);
            throw new XWorkException(ex);
        }
    }
    
    @Override
    public ActionConfig handleUnknownAction(String namespace, String actionName) throws XWorkException {
        redirectToContextRoot();
        return null;
    }

    @Override
    public Result handleUnknownResult(ActionContext actionContext, String actionName, ActionConfig actionConfig, String resultCode) throws XWorkException {
        redirectToContextRoot();
        return null;
    }

    @Override
    public Object handleUnknownActionMethod(Object action, String methodName) {
        redirectToContextRoot();
        return null;
    }
    
}