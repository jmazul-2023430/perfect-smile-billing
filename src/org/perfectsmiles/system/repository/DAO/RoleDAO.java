package org.perfectsmiles.system.repository.DAO;

import org.perfectsmiles.system.config.ConexionDB;
import org.perfectsmiles.system.model.Role;
import org.perfectsmiles.system.repository.IRoleDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleDAO implements IRoleDAO {

    private Role mapResultSet(ResultSet rs) throws SQLException {
        Role role = new Role();
        role.setIdRole(rs.getInt("ID_ROLE"));
        role.setRoleName(rs.getString("Role Name"));
        role.setRoleDescription(rs.getString("Description"));
        role.setRoleStatus(rs.getBoolean("Status"));
        return role;
    }

    @Override
    public boolean create(Role role) throws Exception {
        String sql = "{call sp_create_role(?, ?)}";
        try (Connection conn = ConexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setString(1, role.getRoleName());
            cs.setString(2, role.getRoleDescription());
            return cs.executeUpdate() > 0;
        }
    }

    @Override
    public List<Role> readAll() throws Exception {
        List<Role> roles = new ArrayList<>();
        String sql = "{call sp_read_role()}";
        try (Connection conn = ConexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                roles.add(mapResultSet(rs));
            }
        }
        return roles;
    }

    @Override
    public Role readById(int id) throws Exception {
        Role role = null;
        String sql = "SELECT * FROM role_tb WHERE id_role = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    role = new Role();
                    role.setIdRole(rs.getInt("id_role"));
                    role.setRoleName(rs.getString("role_name"));
                    role.setRoleDescription(rs.getString("role_description"));
                    role.setRoleStatus(rs.getBoolean("role_status"));
                }
            }
        }
        return role;
    }

    @Override
    public boolean update(Role role) throws Exception {
        String sql = "{call sp_edit_role(?, ?, ?, ?)}";
        try (Connection conn = ConexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setString(1, role.getRoleName());
            cs.setString(2, role.getRoleDescription());
            cs.setBoolean(3, role.isRoleStatus());
            cs.setInt(4, role.getIdRole());
            return cs.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws Exception {
        String sql = "{call sp_delete_role(?)}";
        try (Connection conn = ConexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, id);
            return cs.executeUpdate() > 0;
        }
    }

    @Override
    public Role readByName(String roleName) throws Exception {
        Role role = null;
        String sql = "SELECT * FROM role_tb WHERE role_name = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, roleName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    role = new Role();
                    role.setIdRole(rs.getInt("id_role"));
                    role.setRoleName(rs.getString("role_name"));
                    role.setRoleDescription(rs.getString("role_description"));
                    role.setRoleStatus(rs.getBoolean("role_status"));
                }
            }
        }
        return role;
    }

    @Override
    public List<Role> readActiveRoles() throws Exception {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT * FROM role_tb WHERE role_status = true";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Role role = new Role();
                role.setIdRole(rs.getInt("id_role"));
                role.setRoleName(rs.getString("role_name"));
                role.setRoleDescription(rs.getString("role_description"));
                role.setRoleStatus(rs.getBoolean("role_status"));
                roles.add(role);
            }
        }
        return roles;
    }
}