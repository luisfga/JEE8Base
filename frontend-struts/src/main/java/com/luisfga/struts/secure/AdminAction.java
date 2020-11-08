package com.luisfga.struts.secure;

import com.luisfga.business.entities.AppPermission;
import com.luisfga.business.entities.AppRole;
import com.luisfga.business.entities.AppUser;
import com.luisfga.business.secure.AdminUseCase;
import com.opensymphony.xwork2.ActionSupport;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import org.apache.struts2.convention.annotation.Result;

@Result(name = "success", location = "dashboard-admin.jsp")
public class AdminAction extends ActionSupport {

    private static final long serialVersionUID = 1L;

    @EJB
    private AdminUseCase adminUseCase;
    
    private String selectedRole;
    public String getSelectedRole() {
        return selectedRole;
    }
    public void setSelectedRole(String selectedRole) {
        this.selectedRole = selectedRole;
    }
    
    
    private List<AppUser> users;
    public List<AppUser> getUsers() {
        return users;
    }
    public void setUsers(List<AppUser> users) {
        this.users = users;
    }
    
    private List<AppRole> roles;
    public List<AppRole> getRoles() {
        return roles;
    }
    public void setRoles(List<AppRole> roles) {
        this.roles = roles;
    }
    
    private List<AppPermission> permissions;

    public List<AppPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<AppPermission> permissions) {
        this.permissions = permissions;
    }
        
    //Inputs
    private String permission;
    public String getPermission() {
        return permission;
    }
    public void setPermission(String permission) {
        this.permission = permission;
    }
    
    @Override
    public String execute() throws Exception {

        roles = adminUseCase.getAllRoles();
        
        permissions = adminUseCase.getAllPermissions();

        //TODO carregar o que tiver de ser carregado
        users = adminUseCase.getAllUsers();
        
        return SUCCESS;
    }

}
