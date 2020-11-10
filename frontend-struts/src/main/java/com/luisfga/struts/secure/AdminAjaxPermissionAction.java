/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.luisfga.struts.secure;

import com.luisfga.business.exceptions.AlreadyExistsException;
import com.luisfga.business.exceptions.UndeletableException;
import com.luisfga.business.secure.AdminUseCase;
import com.opensymphony.xwork2.ActionSupport;
import javax.ejb.EJB;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

/**
 *
 * @author Luis
 */
@ParentPackage("json-default")
public class AdminAjaxPermissionAction extends ActionSupport{
    
    private final Logger logger = LogManager.getLogger();
    
    @EJB AdminUseCase adminUseCase;
    
    private Boolean success;
    public Boolean getSuccess() {
        return success;
    }
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    
    private String permission;
    public String getPermission() {
        return permission;
    }
    public void setPermission(String permission) {
        this.permission = permission;
    }
    
    private String message;
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    
    @Action(value = "adminAddPermission", results = {@Result(type = "json")}, interceptorRefs = @InterceptorRef("json"))
    public String addPermission(){
        if(permission != null && !permission.isEmpty()){
            try {
                adminUseCase.savePermission(permission);
                success = true;
                message = getText("action.message.successfully.saved");
            } catch (AlreadyExistsException ex) {
                message = getText("action.error.already.exists");
                success = false;
            }
        }
        return SUCCESS;
    }
    
    @Action(value = "adminDeletePermission", results = {@Result(type = "json")}, interceptorRefs = @InterceptorRef("json"))
    public String deletePermission(){
        logger.info("Deletando permissão? " + permission);
        if(permission != null && !permission.isEmpty()){
            
            try {
                adminUseCase.deletePermission(permission);
                success = true;
                message = getText("action.message.successfully.deleted");
                logger.info("Permissão deletada = " + permission);
                
            } catch (UndeletableException ex) {
                success = false;
                message = getText("action.error.undeletable");
            }
        }
        return SUCCESS;
    }
}
