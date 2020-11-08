package com.luisfga.business.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "app_role")
@NamedQueries({
        @NamedQuery(name = "AppRole.findRolesForNewUser", query = "SELECT ar FROM AppRole ar WHERE ar.roleName IN ('Normal User')"),
        @NamedQuery(name = "AppRole.findAllRoles", query = "SELECT ar FROM AppRole ar"),
        @NamedQuery(name = "AppRole.findByName", query = "SELECT ar FROM AppRole ar WHERE ar.roleName = :roleName")
})
public class AppRole implements Serializable {
    
    @Id
    @Column(name = "role_name")
    private String roleName;
    
    @ManyToMany(mappedBy = "roles")
    private List<AppUser> users;
    
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "roles_permissions", 
            joinColumns = @JoinColumn(name = "role_name"), 
            inverseJoinColumns = @JoinColumn(name = "permission_name")
    )
    private List<AppPermission> permissions;

    private Boolean deletable = Boolean.TRUE;
    
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<AppUser> getUsers() {
        return users;
    }

    public void setUsers(List<AppUser> users) {
        this.users = users;
    }

    public List<AppPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<AppPermission> permissions) {
        this.permissions = permissions;
    }

    public Boolean getDeletable() {
        return deletable;
    }

    public void setDeletable(Boolean deletable) {
        this.deletable = deletable;
    }

}