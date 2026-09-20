package org.perfectsmiles.system.service;

import org.perfectsmiles.system.model.Role;
import org.perfectsmiles.system.repository.DAO.RoleDAO;
import org.perfectsmiles.system.repository.IRoleDAO;
import org.perfectsmiles.system.utils.Validations;

import java.util.List;

public class RoleService {

    private final IRoleDAO roleDAO;

    public RoleService() {
        this.roleDAO = new RoleDAO();
    }

    // Para tests con mock
    public RoleService(IRoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    // ============================================================
    // CREATE
    // ============================================================
    public boolean createRole(Role role) throws Exception {
        // Validaciones
        if (Validations.isNullOrEmpty(role.getRoleName())) {
            throw new IllegalArgumentException("El nombre del rol es obligatorio.");
        }
        if (role.getRoleName().length() > 50) {
            throw new IllegalArgumentException("El nombre del rol no puede exceder 50 caracteres.");
        }
        
        // Verificar que no exista otro rol con el mismo nombre
        Role existing = roleDAO.readByName(role.getRoleName());
        if (existing != null) {
            throw new IllegalArgumentException("Ya existe un rol con ese nombre.");
        }
        
        return roleDAO.create(role);
    }

    // ============================================================
    // READ
    // ============================================================
    public List<Role> getAllRoles() throws Exception {
        return roleDAO.readAll();
    }

    public List<Role> getActiveRoles() throws Exception {
        return roleDAO.readActiveRoles();
    }

    public Role getRoleById(int id) throws Exception {
        return roleDAO.readById(id);
    }

    public Role getRoleByName(String name) throws Exception {
        return roleDAO.readByName(name);
    }

    // ============================================================
    // UPDATE
    // ============================================================
    public boolean updateRole(Role role) throws Exception {
        if (Validations.isNullOrEmpty(role.getRoleName())) {
            throw new IllegalArgumentException("El nombre del rol es obligatorio.");
        }
        return roleDAO.update(role);
    }

    // ============================================================
    // DELETE (soft)
    // ============================================================
    public boolean deactivateRole(int id) throws Exception {
        // No permitir desactivar los 3 roles base del sistema
        if (id == 1 || id == 2 || id == 3) {
            throw new IllegalArgumentException("No se pueden desactivar los roles base del sistema (owner, administrator, dentist).");
        }
        return roleDAO.delete(id);
    }
}