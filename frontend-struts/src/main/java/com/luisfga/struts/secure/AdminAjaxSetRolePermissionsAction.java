/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.luisfga.struts.secure;

import com.luisfga.business.entities.AppPermission;
import com.luisfga.business.entities.AppRole;
import com.luisfga.business.exceptions.AlreadyExistsException;
import com.luisfga.business.exceptions.UndeletableException;
import com.luisfga.business.secure.AdminUseCase;
import com.opensymphony.xwork2.ActionSupport;
import java.util.List;
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
public class AdminAjaxSetRolePermissionsAction extends ActionSupport{
    
    private final Logger logger = LogManager.getLogger();
    
    @EJB AdminUseCase adminUseCase;
    
    private Boolean success;
    public Boolean getSuccess() {
        return success;
    }
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    private String message;
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    private String selectedRole;
    public String getSelectedRole() {
        return selectedRole;
    }
    public void setSelectedRole(String selectedRole) {
        this.selectedRole = selectedRole;
    }
    
    private List<AppPermission> selectedRolePermissions;
    public List<AppPermission> getSelectedRolePermissions() {
        return selectedRolePermissions;
    }
    public void setSelectedRolePermissions(List<AppPermission> selectedRolePermissions) {
        this.selectedRolePermissions = selectedRolePermissions;
    }
    
    private List<String> rolePermissions;
    public List<String> getRolePermissions(){
        return rolePermissions;
    }
    public void setRolePermissions(List<String> rolePermissions){
        this.rolePermissions = rolePermissions;
    }
    
    @Action(value = "adminSaveRolePermissions", results = {@Result(type = "json")}, interceptorRefs = @InterceptorRef("json"))
    public String addSaveRolePermissions(){

        adminUseCase.associateRolePermissions(selectedRole, rolePermissions);
        success = true;
        message = getText("action.message.successfully.saved");

        return SUCCESS;
    }
    
    @Action(value = "adminLoadRolePermissions", results = {@Result(type = "json")}, interceptorRefs = @InterceptorRef("json"))
    public String loadRolePermissions(){
        
        selectedRolePermissions = adminUseCase.getPermissionsByRole(selectedRole);
        logger.info("selectedRolePermissions.size() = " + selectedRolePermissions.size());
        return SUCCESS;
    }
}
