package com.luisfga.struts.secure;

import com.luisfga.business.entities.AppRole;
import com.luisfga.business.entities.AppUser;
import com.luisfga.business.secure.DashboardUseCase;
import com.opensymphony.xwork2.ActionSupport;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import org.apache.struts2.convention.annotation.Result;

@Result(name = "success", location = "dashboard.jsp")
public class DashboardAction extends ActionSupport {

    private static final long serialVersionUID = 1L;

    @EJB
    private DashboardUseCase dashboardUseCase;
    
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

    @Override
    public String execute() throws Exception {

        //TODO carregar o que tiver de ser carregado
        users = dashboardUseCase.getAllUsers();
        
        roles = dashboardUseCase.getAllRoles();
        
        return SUCCESS;
    }
}
