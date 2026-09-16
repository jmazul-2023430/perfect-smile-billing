package org.perfectsmiles.system.repository;

import org.perfectsmiles.system.config.ConexionDB;
import org.perfectsmiles.system.model.Role;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



public class RoleDAO {
    
     @Override
    public void create(Role role) throws Exception {
        String sql = "CALL sp_create_role(?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, role.getRoleName());
            ps.setString(2, role.getRoleDescription());
            ps.executeUpdate();
        }
    }

    @Override
    public List<Role> readAll() throws Exception {
        List<Role> roles = new ArrayList<>();
        String sql = "CALL sp_read_role()";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Role role = new Role();
                role.setIdRole(rs.getInt("ID_ROLE"));
                role.setRoleName(rs.getString("Role Name"));
                role.setRoleDescription(rs.getString("Description"));
                role.setRoleStatus(rs.getBoolean("Status"));
                roles.add(role);
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
    public void update(Role role) throws Exception {
        String sql = "CALL sp_edit_role(?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, role.getRoleName());
            ps.setString(2, role.getRoleDescription());
            ps.setBoolean(3, role.isRoleStatus());
            ps.setInt(4, role.getIdRole());
            ps.executeUpdate();
        }
    }
    
    @Override
    public void delete(int id) throws Exception {
        String sql = "CALL sp_delete_role(?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
    
    


 

    


}
