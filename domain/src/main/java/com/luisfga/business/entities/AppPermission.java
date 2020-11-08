/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.luisfga.business.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "app_permission")
@NamedQueries({
        @NamedQuery(name = "AppPermission.findAllPermissions", query = "SELECT ap FROM AppPermission ap"),
        @NamedQuery(name = "AppPermission.findByName", query = "SELECT ap FROM AppPermission ap WHERE ap.permissionName = :permissionName")
})
public class AppPermission implements Serializable{
    
    @Id
    @Column(name = "permission_name")
    private String permissionName;
    
    @ManyToMany(mappedBy = "permissions")
    private List<AppRole> roles;

    private Boolean deletable = Boolean.TRUE;
    
    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public List<AppRole> getRoles() {
        return roles;
    }

    public void setRoles(List<AppRole> roles) {
        this.roles = roles;
    }

    public Boolean getDeletable() {
        return deletable;
    }

    public void setDeletable(Boolean deletable) {
        this.deletable = deletable;
    }
    
}