package org.perfectsmiles.system.model;

public class Permission {

    private int idPermission;
    private String permissionName;
    private String permissionDescription;
    private String moduleName;
    private boolean permissionStatus;

    public Permission() {
    }

    public Permission(int idPermission, String permissionName, String permissionDescription,
            String moduleName, boolean permissionStatus) {
        this.idPermission = idPermission;
        this.permissionName = permissionName;
        this.permissionDescription = permissionDescription;
        this.moduleName = moduleName;
        this.permissionStatus = permissionStatus;
    }

    public int getIdPermission() {
        return idPermission;
    }

    public void setIdPermission(int idPermission) {
        this.idPermission = idPermission;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getPermissionDescription() {
        return permissionDescription;
    }

    public void setPermissionDescription(String permissionDescription) {
        this.permissionDescription = permissionDescription;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public boolean isPermissionStatus() {
        return permissionStatus;
    }

    public void setPermissionStatus(boolean permissionStatus) {
        this.permissionStatus = permissionStatus;
    }

    @Override
    public String toString() {
        return "Permission{id=" + idPermission + ", name='" + permissionName + "', module='" + moduleName + "'}";
    }
}
