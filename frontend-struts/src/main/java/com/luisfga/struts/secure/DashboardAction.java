package com.luisfga.struts.secure;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.struts2.convention.annotation.Result;

@RequiresAuthentication
@Result(name = "success", location = "dashboard.jsp")
public class DashboardAction extends ActionSupport {

    private static final long serialVersionUID = 1L;

    @Override
    public String execute() throws Exception {

        return SUCCESS;
    }
}
