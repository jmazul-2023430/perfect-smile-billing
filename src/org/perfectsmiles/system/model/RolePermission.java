package org.perfectsmiles.system.model;

public class RolePermission {

    private int idRolePermission;
    private int idRole;
    private int idPermission;
    private boolean assignmentStatus;

    public RolePermission() {
    }

    public RolePermission(int idRolePermission, int idRole, int idPermission, boolean assignmentStatus) {
        this.idRolePermission = idRolePermission;
        this.idRole = idRole;
        this.idPermission = idPermission;
        this.assignmentStatus = assignmentStatus;
    }

    public int getIdRolePermission() {
        return idRolePermission;
    }

    public void setIdRolePermission(int idRolePermission) {
        this.idRolePermission = idRolePermission;
    }

    public int getIdRole() {
        return idRole;
    }

    public void setIdRole(int idRole) {
        this.idRole = idRole;
    }

    public int getIdPermission() {
        return idPermission;
    }

    public void setIdPermission(int idPermission) {
        this.idPermission = idPermission;
    }

    public boolean isAssignmentStatus() {
        return assignmentStatus;
    }

    public void setAssignmentStatus(boolean assignmentStatus) {
        this.assignmentStatus = assignmentStatus;
    }

    @Override
    public String toString() {
        return "RolePermission{id=" + idRolePermission + ", role=" + idRole
                + ", permission=" + idPermission + ", status=" + assignmentStatus + "}";
    }
}
