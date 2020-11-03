/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.luisfga.business.secure;

import com.luisfga.business.UseCase;
import com.luisfga.business.entities.AppRole;
import com.luisfga.business.entities.AppUser;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Luis
 */
@Stateless
public class DashboardUseCase extends UseCase{
    
    Logger logger = LogManager.getLogger();
    
    public List<AppUser> getAllUsers(){
        List<AppUser> users;
        
        Query findAllUsers = em.createNamedQuery("AppUser.findAll");
        users = findAllUsers.getResultList();
        logger.debug("Users.lenght = " + users.size());
        users.forEach(user -> {
            logger.debug("User: " + user.getUserName());
        });
        
        return users;
    }
    
    public List<AppRole> getAllRoles(){
        List<AppRole> roles;
        
        Query findAllRoles = em.createNamedQuery("AppRole.findAllRoles");
        roles = findAllRoles.getResultList();
        logger.debug("Roles.lenght = " + roles.size());
        roles.forEach(role -> {
            logger.debug("Role: " + role.getRoleName());
        });
        
        return roles;
    }
}
