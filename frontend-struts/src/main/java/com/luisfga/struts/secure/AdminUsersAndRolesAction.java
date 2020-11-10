package com.luisfga.struts.secure;

import com.luisfga.business.entities.AppRole;
import com.luisfga.business.secure.AdminUseCase;
import com.opensymphony.xwork2.ActionSupport;
import java.util.List;
import javax.ejb.EJB;
import org.apache.shiro.authz.annotation.RequiresAuthentication;

@RequiresAuthentication
public class AdminUsersAndRolesAction extends ActionSupport {

    private static final long serialVersionUID = 1L;

    @EJB
    private AdminUseCase adminUseCase;
    
    private List<AppRole> roles;
    public List<AppRole> getRoles() {
        return roles;
    }
    public void setRoles(List<AppRole> roles) {
        this.roles = roles;
    }
    
    @Override
    public String execute() throws Exception {

        roles = adminUseCase.getAllRoles();
        
        return SUCCESS;
    }

}
