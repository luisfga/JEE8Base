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
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class ApplicationShiroJdbcRealm extends JdbcRealm {

    public ApplicationShiroJdbcRealm() {
        super.setName("ApplicationDataSourceRealm");
    }
    
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        permissionsLookupEnabled = true;
        
        userRolesQuery = "select role_name from user_roles where email = ?";
        
        permissionsQuery = "select permission_name from roles_permissions where role_name = ?";
        
        return super.doGetAuthorizationInfo(principals);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws LockedAccountException, AuthenticationException {
        
        UsernamePasswordToken uToken = (UsernamePasswordToken) token;

        try {
            Connection conn = dataSource.getConnection();
            
            PreparedStatement ps = conn.prepareStatement("SELECT email,password,status FROM app_user WHERE email = ?");
            ps.setString(1, uToken.getUsername());
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()) {
                String hashedPassword = rs.getString(2);
                DefaultPasswordService defaultPasswordService = new DefaultPasswordService();

                String password = new String(uToken.getPassword());
                if(!defaultPasswordService.passwordsMatch(password, hashedPassword)){
                    throw new IncorrectCredentialsException();
                }
                
                String status = rs.getString(3);
                if ("locked".equals(status)) {
                    throw new LockedAccountException();
                } else if ("new".equals(status)) {
                    throw new PendingEmailConfirmationShiroAuthenticationException();
                }
                
                return new SimpleAuthenticationInfo(rs.getString(1), hashedPassword, getName());
                
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