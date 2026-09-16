package org.perfectsmiles.system.repository;


import org.perfectsmiles.system.config.ConexionDB;
import org.perfectsmiles.system.model.RolePermission;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



public class RolePermissionDAO {
    
     @Override
    public void create(RolePermission rolePermission) throws Exception {
        String sql = "CALL sp_create_role_permission(?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, rolePermission.getIdRole());
            ps.setInt(2, rolePermission.getIdPermission());
            ps.setBoolean(3, rolePermission.isAssignmentStatus());
            ps.executeUpdate();
        }
    }
    
    
    @Override
    public List<RolePermission> readAll() throws Exception {
        List<RolePermission> list = new ArrayList<>();
        String sql = "CALL sp_read_role_permission()";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                RolePermission rp = new RolePermission();
                rp.setIdRolePermission(rs.getInt("ID_ROLE_PERMISSION"));
                rp.setIdRole(rs.getInt("ID_ROLE"));
                rp.setIdPermission(rs.getInt("ID_PERMISSION"));
                rp.setAssignmentStatus(rs.getBoolean("Status"));
                list.add(rp);
            }
        }
        return list;
    }
    
    
    @Override
    public RolePermission readById(int id) throws Exception {
        RolePermission rp = null;
        String sql = "SELECT * FROM role_permission_tb WHERE id_role_permission = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    rp = new RolePermission();
                    rp.setIdRolePermission(rs.getInt("id_role_permission"));
                    rp.setIdRole(rs.getInt("id_role"));
                    rp.setIdPermission(rs.getInt("id_permission"));
                    rp.setAssignmentStatus(rs.getBoolean("assignment_status"));
                }
            }
        }
        return rp;
    }
    
    @Override
    public void update(RolePermission rolePermission) throws Exception {
        String sql = "CALL sp_edit_role_permission(?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, rolePermission.getIdRole());
            ps.setInt(2, rolePermission.getIdPermission());
            ps.setBoolean(3, rolePermission.isAssignmentStatus());
            ps.setInt(4, rolePermission.getIdRolePermission());
            ps.executeUpdate();
        }
    }
    
    
     @Override
    public void delete(int id) throws Exception {
        String sql = "CALL sp_delete_role_permission(?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
    
}
