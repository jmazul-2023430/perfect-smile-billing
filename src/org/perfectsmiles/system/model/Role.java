package org.perfectsmiles.system.model;

public class Role {

    private int idRole;
    private String roleName;
    private String roleDescription;
    private boolean roleStatus;

    public Role() {
    }

    public Role(int idRole, String roleName, String roleDescription, boolean roleStatus) {
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

    @Override
    public String toString() {
        return roleName; // Útil para ComboBox
    }
}
