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
public class AdminAjaxRoleAction extends ActionSupport{
    
    private final Logger logger = LogManager.getLogger();
    
    @EJB AdminUseCase adminUseCase;
    
    private Boolean success;
    public Boolean getSuccess() {
        return success;
    }
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    private String role;
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    
    private String message;
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    
    @Action(value = "adminAddRole", results = {@Result(type = "json")}, interceptorRefs = @InterceptorRef("json"))
    public String addRole(){
        if(role != null && !role.isEmpty()){
            try {
                adminUseCase.saveRole(role);
                success = true;
                message = getText("action.message.successfully.saved");
            } catch (AlreadyExistsException ex) {
                message = getText("action.error.already.exists");
                success = false;
            }
        }
        return SUCCESS;
    }
    
    @Action(value = "adminDeleteRole", results = {@Result(type = "json")}, interceptorRefs = @InterceptorRef("json"))
    public String deleteRole(){
        logger.info("Deletando role? " + role);
        if(role != null && !role.isEmpty()){
            
            try {
                adminUseCase.deleteRole(role);
                success = true;
                message = getText("action.message.successfully.deleted");
                logger.info("Role deletada = " + role);
                
            } catch (UndeletableException ex) {
                success = false;
                message = getText("action.error.undeletable");
            }
        }
        return SUCCESS;
    }
}
