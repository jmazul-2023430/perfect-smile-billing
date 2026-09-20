package org.perfectsmiles.system.repository.DAO;

import org.perfectsmiles.system.config.ConexionDB;
import org.perfectsmiles.system.model.RolePermission;
import org.perfectsmiles.system.repository.IRolePermissionDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RolePermissionDAO implements IRolePermissionDAO {

    private RolePermission mapResultSet(ResultSet rs) throws SQLException {
        RolePermission rp = new RolePermission();
        rp.setIdRolePermission(rs.getInt("ID_ROLE_PERMISSION"));
        rp.setIdRole(rs.getInt("ID_ROLE"));
        rp.setIdPermission(rs.getInt("ID_PERMISSION"));
        rp.setAssignmentStatus(rs.getBoolean("Status"));
        return rp;
    }

    private RolePermission mapResultSetRaw(ResultSet rs) throws SQLException {
        RolePermission rp = new RolePermission();
        rp.setIdRolePermission(rs.getInt("id_role_permission"));
        rp.setIdRole(rs.getInt("id_role"));
        rp.setIdPermission(rs.getInt("id_permission"));
        rp.setAssignmentStatus(rs.getBoolean("assignment_status"));
        return rp;
    }

    @Override
    public boolean create(RolePermission rp) throws Exception {
        String sql = "{call sp_create_role_permission(?, ?, ?)}";
        try (Connection conn = ConexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, rp.getIdRole());
            cs.setInt(2, rp.getIdPermission());
            cs.setBoolean(3, rp.isAssignmentStatus());
            return cs.executeUpdate() > 0;
        }
    }

    @Override
    public List<RolePermission> readAll() throws Exception {
        List<RolePermission> list = new ArrayList<>();
        String sql = "{call sp_read_role_permission()}";
        try (Connection conn = ConexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSet(rs));
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
                if (rs.next()) rp = mapResultSetRaw(rs);
            }
        }
        return rp;
    }

    @Override
    public boolean update(RolePermission rp) throws Exception {
        String sql = "{call sp_edit_role_permission(?, ?, ?, ?)}";
        try (Connection conn = ConexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, rp.getIdRole());
            cs.setInt(2, rp.getIdPermission());
            cs.setBoolean(3, rp.isAssignmentStatus());
            cs.setInt(4, rp.getIdRolePermission());
            return cs.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws Exception {
        String sql = "{call sp_delete_role_permission(?)}";
        try (Connection conn = ConexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, id);
            return cs.executeUpdate() > 0;
        }
    }

    @Override
    public List<RolePermission> readByRole(int idRole) throws Exception {
        List<RolePermission> list = new ArrayList<>();
        String sql = "SELECT * FROM role_permission_tb WHERE id_role = ? AND assignment_status = true";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idRole);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapResultSetRaw(rs));
            }
        }
        return list;
    }

    @Override
    public List<RolePermission> readByPermission(int idPermission) throws Exception {
        List<RolePermission> list = new ArrayList<>();
        String sql = "SELECT * FROM role_permission_tb WHERE id_permission = ? AND assignment_status = true";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPermission);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapResultSetRaw(rs));
            }
        }
        return list;
    }

    @Override
    public boolean exists(int idRole, int idPermission) throws Exception {
        String sql = "SELECT 1 FROM role_permission_tb WHERE id_role = ? AND id_permission = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idRole);
            ps.setInt(2, idPermission);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}