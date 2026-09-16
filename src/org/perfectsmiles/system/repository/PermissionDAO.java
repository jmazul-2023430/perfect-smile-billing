package org.perfectsmiles.system.repository;


import org.perfectsmiles.system.config.ConexionDB;
import org.perfectsmiles.system.model.Permission;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PermissionDAO {
    @Override
    public void create(Permission permission) throws Exception {
        String sql = "CALL sp_create_permission(?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, permission.getPermissionName());
            ps.setString(2, permission.getPermissionDescription());
            ps.setString(3, permission.getModuleName());
            ps.setBoolean(4, permission.isPermissionStatus());
            ps.executeUpdate();
        }
    }
    
     @Override
    public List<Permission> readAll() throws Exception {
        List<Permission> permissions = new ArrayList<>();
        String sql = "CALL sp_read_permission()";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Permission p = new Permission();
                p.setIdPermission(rs.getInt("ID_PERMISSION"));
                p.setPermissionName(rs.getString("Permission Name"));
                p.setPermissionDescription(rs.getString("Description"));
                p.setModuleName(rs.getString("Module"));
                p.setPermissionStatus(rs.getBoolean("Status"));
                permissions.add(p);
            }
        }
        return permissions;
    }
    
    
     @Override
    public Permission readById(int id) throws Exception {
        Permission permission = null;
        String sql = "SELECT * FROM permission_tb WHERE id_permission = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    permission = new Permission();
                    permission.setIdPermission(rs.getInt("id_permission"));
                    permission.setPermissionName(rs.getString("permission_name"));
                    permission.setPermissionDescription(rs.getString("permission_description"));
                    permission.setModuleName(rs.getString("module_name"));
                    permission.setPermissionStatus(rs.getBoolean("permission_status"));
                }
            }
        }
        return permission;
    }
    
    
    
    @Override
    public void update(Permission permission) throws Exception {
        String sql = "CALL sp_edit_permission(?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, permission.getPermissionName());
            ps.setString(2, permission.getPermissionDescription());
            ps.setString(3, permission.getModuleName());
            ps.setBoolean(4, permission.isPermissionStatus());
            ps.setInt(5, permission.getIdPermission());
            ps.executeUpdate();
        }
    }
    
    
    @Override
    public void delete(int id) throws Exception {
        String sql = "CALL sp_delete_permission(?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
    
    
    
}
