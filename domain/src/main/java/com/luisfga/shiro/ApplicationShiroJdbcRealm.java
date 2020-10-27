package com.luisfga.shiro;

import com.luisfga.business.exceptions.PendingEmailConfirmationException;
import com.luisfga.business.exceptions.PendingEmailConfirmationShiroAuthenticationException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class ApplicationShiroJdbcRealm extends JdbcRealm {

    public ApplicationShiroJdbcRealm() {
        super.setName("ApplicationDataSourceRealm");
    }
    
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws LockedAccountException, AuthenticationException {
        
        UsernamePasswordToken uToken = (UsernamePasswordToken) token;

        try {
            Connection conn = dataSource.getConnection();
            
            PreparedStatement ps = conn.prepareStatement("SELECT email,password,status FROM app_user WHERE email = ?");
            ps.setString(1, uToken.getUsername());
            ResultSet rs = ps.executeQuery();
            
            String email;
            String hashedPassword;
            String status;
            
            if(rs.next()) {
                email = rs.getString(1);
                hashedPassword = rs.getString(2);
                status = rs.getString(3);
                
                if ("locked".equals(status)) {
                    throw new LockedAccountException();
                } else if ("new".equals(status)) {
                    throw new PendingEmailConfirmationShiroAuthenticationException();
                }
                
                return new SimpleAuthenticationInfo(email, hashedPassword, getName());
                
            }
            
        } catch (SQLException ex) {
            LogManager.getLogger().error(ex.getMessage());
            throw new AuthenticationException(ex);
        }
     
        return null;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return (token instanceof UsernamePasswordToken);
    }

}