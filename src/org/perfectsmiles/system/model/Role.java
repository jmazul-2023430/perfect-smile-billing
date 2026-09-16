/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.perfectsmiles.system.model;

/**
 *
 * @author informatica
 */
public class Role {
    private int idRole;
    private String roleName;
    private String roleDescription;
    private boolean roleStatus;

    public Role () {
    
    }

    public Role (int idRole, String roleName, String roleDescription, boolean roleStatus) {
        this.idRole = idRole;
        this.roleName = roleName;
        this.roleDescription = roleDescription;
        this.roleStatus = roleStatus;
    }

    public int getIdRole() {
        return idRole;
    }

    public void setIdRole(int idRole) {
        this.idRole = idRole;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }

    public boolean isRoleStatus() {
        return roleStatus;
    }

    public void setRoleStatus(boolean roleStatus) {
        this.roleStatus = roleStatus;
    }
    
    
    
}
