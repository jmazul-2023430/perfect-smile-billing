package org.perfectsmiles.system.service;

import org.perfectsmiles.system.model.Permission;
import org.perfectsmiles.system.model.RolePermission;
import org.perfectsmiles.system.repository.DAO.PermissionDAO;
import org.perfectsmiles.system.repository.DAO.RolePermissionDAO;
import org.perfectsmiles.system.repository.IPermissionDAO;
import org.perfectsmiles.system.repository.IRolePermissionDAO;

import java.util.ArrayList;
import java.util.List;

public class AuthorizationService {

    private final IRolePermissionDAO rolePermissionDAO;
    private final IPermissionDAO permissionDAO;

    public AuthorizationService() {
        this.rolePermissionDAO = new RolePermissionDAO();
        this.permissionDAO = new PermissionDAO();
    }

    public List<Permission> loadPermissionsForRole(int idRole) throws Exception {
        List<Permission> result = new ArrayList<>();
        List<RolePermission> assignments = rolePermissionDAO.readByRole(idRole);

        for (RolePermission rp : assignments) {
            if (rp.isAssignmentStatus()) {
                Permission permission = permissionDAO.readById(rp.getIdPermission());
                if (permission != null && permission.isPermissionStatus()) {
                    result.add(permission);
                }
            }
        }
        return result;
    }

    public boolean hasPermission(int idRole, String permissionName) throws Exception {
        List<Permission> permissions = loadPermissionsForRole(idRole);
        return permissions.stream()
                .anyMatch(p -> permissionName.equalsIgnoreCase(p.getPermissionName()));
    }

    public boolean canAccessModule(int idRole, String moduleName) throws Exception {
        List<Permission> permissions = loadPermissionsForRole(idRole);
        return permissions.stream()
                .anyMatch(p -> moduleName.equalsIgnoreCase(p.getModuleName()));
    }
}
