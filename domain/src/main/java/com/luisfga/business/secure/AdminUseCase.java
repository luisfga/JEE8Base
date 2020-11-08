/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.luisfga.business.secure;

import com.luisfga.business.UseCase;
import com.luisfga.business.entities.AppPermission;
import com.luisfga.business.entities.AppRole;
import com.luisfga.business.entities.AppUser;
import com.luisfga.business.exceptions.AlreadyExistsException;
import com.luisfga.business.exceptions.UndeletableException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;

/**
 *
 * @author Luis
 */
@Stateless
@RequiresAuthentication
public class AdminUseCase extends UseCase{
    
    private final Logger logger = LogManager.getLogger();

    @RequiresPermissions("admin:create")    
    public void saveRole(String roleName) throws AlreadyExistsException{
        Query findRoleByName = em.createNamedQuery("AppRole.findByName");
        findRoleByName.setParameter("roleName", roleName);
        
        AppRole appRole;
        
        try{
            appRole = (AppRole) findRoleByName.getSingleResult();
            
            throw new AlreadyExistsException();
            
        } catch (NoResultException ex) {
            appRole = new AppRole();
            appRole.setRoleName(roleName);
            
            em.persist(appRole);
            logger.info("Role salva: " + roleName);
        }
        
    }
    
    @RequiresPermissions("admin:delete")
    public void deleteRole(String roleName) throws UndeletableException {
        Query findByName = em.createNamedQuery("AppRole.findByName");
        findByName.setParameter("roleName", roleName);

        AppRole appRole = (AppRole) findByName.getSingleResult();
        
        if(appRole.getDeletable()){
            em.remove(appRole);
        } else {
            throw new UndeletableException();
        }
    }
    
    @RequiresPermissions("admin:create")
    public void savePermission(String permissionName) throws AlreadyExistsException{
        Query findByName = em.createNamedQuery("AppPermission.findByName");
        findByName.setParameter("permissionName", permissionName);
        
        AppPermission appPermission;
        
        try {
            appPermission = (AppPermission) findByName.getSingleResult();
            
            throw new AlreadyExistsException();
            
        } catch (NoResultException ex) {
            appPermission = new AppPermission();
            appPermission.setPermissionName(permissionName);
            
            em.persist(appPermission);
            logger.info("Permissão salva: " + permissionName);
        }
    }
    @RequiresPermissions("admin:delete")
    public void deletePermission(String permissionName) throws UndeletableException {
        Query findByName = em.createNamedQuery("AppPermission.findByName");
        findByName.setParameter("permissionName", permissionName);

        AppPermission appPermission = (AppPermission) findByName.getSingleResult();
        
        if(appPermission.getDeletable()){
            em.remove(appPermission);
        } else {
            throw new UndeletableException();
        }
    }
    
    @RequiresPermissions("admin:read")
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
    
    @RequiresPermissions("admin:read")
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
    
    @RequiresPermissions("admin:read")
    public List<AppPermission> getAllPermissions(){
        List<AppPermission> permissions;
        
        Query findAllPermissions = em.createNamedQuery("AppPermission.findAllPermissions");
        permissions = findAllPermissions.getResultList();
        logger.debug("Permissions.lenght = " + permissions.size());
        permissions.forEach(role -> {
            logger.debug("Permission: " + role.getPermissionName());
        });
        
        return permissions;
    }
    
    @RequiresPermissions("admin:read")
    public List<AppPermission> getPermissionsByRole(String roleName){
        List<AppPermission> permissions;
        
        Query findRoleByName = em.createNamedQuery("AppRole.findByName");
        findRoleByName.setParameter("roleName", roleName);
        AppRole appRole = (AppRole) findRoleByName.getSingleResult();
        return appRole.getPermissions();
    }
}
