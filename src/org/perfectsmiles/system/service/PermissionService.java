package org.perfectsmiles.system.service;

import org.perfectsmiles.system.model.Permission;
import org.perfectsmiles.system.repository.DAO.PermissionDAO;
import org.perfectsmiles.system.repository.IPermissionDAO;
import org.perfectsmiles.system.utils.Validations;

import java.util.List;

public class PermissionService {

    private final IPermissionDAO permissionDAO;

    public PermissionService() {
        this.permissionDAO = new PermissionDAO();
    }

    public PermissionService(IPermissionDAO permissionDAO) {
        this.permissionDAO = permissionDAO;
    }

    // ============================================================
    // CREATE
    // ============================================================
    public boolean createPermission(Permission permission) throws Exception {
        if (Validations.isNullOrEmpty(permission.getPermissionName())) {
            throw new IllegalArgumentException("El nombre del permiso es obligatorio.");
        }
        if (permission.getPermissionName().length() > 50) {
            throw new IllegalArgumentException("El nombre del permiso no puede exceder 50 caracteres.");
        }

        // Verificar que no exista otro permiso con el mismo nombre
        Permission existing = permissionDAO.readByName(permission.getPermissionName());
        if (existing != null) {
            throw new IllegalArgumentException("Ya existe un permiso con ese nombre.");
        }

        return permissionDAO.create(permission);
    }

    // ============================================================
    // READ
    // ============================================================
    public List<Permission> getAllPermissions() throws Exception {
        return permissionDAO.readAll();
    }

    public List<Permission> getActivePermissions() throws Exception {
        return permissionDAO.readActivePermissions();
    }

    public List<Permission> getPermissionsByModule(String module) throws Exception {
        return permissionDAO.readByModule(module);
    }

    public Permission getPermissionById(int id) throws Exception {
        return permissionDAO.readById(id);
    }

    public Permission getPermissionByName(String name) throws Exception {
        return permissionDAO.readByName(name);
    }

    // ============================================================
    // UPDATE
    // ============================================================
    public boolean updatePermission(Permission permission) throws Exception {
        if (Validations.isNullOrEmpty(permission.getPermissionName())) {
            throw new IllegalArgumentException("El nombre del permiso es obligatorio.");
        }
        return permissionDAO.update(permission);
    }

    // ============================================================
    // DELETE (soft)
    // ============================================================
    public boolean deactivatePermission(int id) throws Exception {
        return permissionDAO.delete(id);
    }
}
