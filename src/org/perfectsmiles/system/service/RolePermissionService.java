package org.perfectsmiles.system.service;

import org.perfectsmiles.system.model.RolePermission;
import org.perfectsmiles.system.repository.DAO.RolePermissionDAO;
import org.perfectsmiles.system.repository.IRolePermissionDAO;

import java.util.List;

public class RolePermissionService {

    private final IRolePermissionDAO dao;

    public RolePermissionService() {
        this.dao = new RolePermissionDAO();
    }

    public RolePermissionService(IRolePermissionDAO dao) {
        this.dao = dao;
    }

    // ============================================================
    // ASIGNAR permiso a rol
    // ============================================================
    public boolean assignPermission(RolePermission rp) throws Exception {
        if (rp.getIdRole() <= 0 || rp.getIdPermission() <= 0) {
            throw new IllegalArgumentException("Rol y permiso son obligatorios.");
        }
        if (dao.exists(rp.getIdRole(), rp.getIdPermission())) {
            throw new IllegalArgumentException("Este rol ya tiene ese permiso asignado.");
        }
        return dao.create(rp);
    }

    // ============================================================
    // REVOCAR permiso (soft delete)
    // ============================================================
    public boolean revokePermission(int idRolePermission) throws Exception {
        return dao.delete(idRolePermission);
    }

    // ============================================================
    // CONSULTAS
    // ============================================================
    public List<RolePermission> getAllAssignments() throws Exception {
        return dao.readAll();
    }

    public List<RolePermission> getByRole(int idRole) throws Exception {
        return dao.readByRole(idRole);
    }

    public List<RolePermission> getByPermission(int idPermission) throws Exception {
        return dao.readByPermission(idPermission);
    }

    public RolePermission getById(int id) throws Exception {
        return dao.readById(id);
    }

    public boolean updateAssignment(RolePermission rp) throws Exception {
        return dao.update(rp);
    }
}