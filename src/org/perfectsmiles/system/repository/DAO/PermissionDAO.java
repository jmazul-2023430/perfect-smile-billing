package org.perfectsmiles.system.repository.DAO;

import org.perfectsmiles.system.config.ConexionDB;
import org.perfectsmiles.system.model.Permission;
import org.perfectsmiles.system.repository.IPermissionDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PermissionDAO implements IPermissionDAO {

    private Permission mapResultSet(ResultSet rs) throws SQLException {
        Permission p = new Permission();
        p.setIdPermission(rs.getInt("ID_PERMISSION"));
        p.setPermissionName(rs.getString("Permission Name"));
        p.setPermissionDescription(rs.getString("Description"));
        p.setModuleName(rs.getString("Module"));
        p.setPermissionStatus(rs.getBoolean("Status"));
        return p;
    }

    private Permission mapResultSetRaw(ResultSet rs) throws SQLException {
        Permission p = new Permission();
        p.setIdPermission(rs.getInt("id_permission"));
        p.setPermissionName(rs.getString("permission_name"));
        p.setPermissionDescription(rs.getString("permission_description"));
        p.setModuleName(rs.getString("module_name"));
        p.setPermissionStatus(rs.getBoolean("permission_status"));
        return p;
    }

    @Override
    public boolean create(Permission permission) throws Exception {
        String sql = "{call sp_create_permission(?, ?, ?, ?)}";
        try (Connection conn = ConexionDB.getConnection(); CallableStatement cs = conn.prepareCall(sql)) {
            cs.setString(1, permission.getPermissionName());
            cs.setString(2, permission.getPermissionDescription());
            cs.setString(3, permission.getModuleName());
            cs.setBoolean(4, permission.isPermissionStatus());
            return cs.executeUpdate() > 0;
        }
    }

    @Override
    public List<Permission> readAll() throws Exception {
        List<Permission> list = new ArrayList<>();
        String sql = "{call sp_read_permission()}";
        try (Connection conn = ConexionDB.getConnection(); CallableStatement cs = conn.prepareCall(sql); ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        }
        return list;
    }

    @Override
    public Permission readById(int id) throws Exception {
        Permission permission = null;
        String sql = "SELECT * FROM permission_tb WHERE id_permission = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    permission = mapResultSetRaw(rs);
                }
            }
        }
        return permission;
    }

    @Override
    public boolean update(Permission permission) throws Exception {
        String sql = "{call sp_edit_permission(?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionDB.getConnection(); CallableStatement cs = conn.prepareCall(sql)) {
            cs.setString(1, permission.getPermissionName());
            cs.setString(2, permission.getPermissionDescription());
            cs.setString(3, permission.getModuleName());
            cs.setBoolean(4, permission.isPermissionStatus());
            cs.setInt(5, permission.getIdPermission());
            return cs.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws Exception {
        String sql = "{call sp_delete_permission(?)}";
        try (Connection conn = ConexionDB.getConnection(); CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, id);
            return cs.executeUpdate() > 0;
        }
    }

    @Override
    public Permission readByName(String permissionName) throws Exception {
        Permission permission = null;
        String sql = "SELECT * FROM permission_tb WHERE permission_name = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, permissionName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    permission = mapResultSetRaw(rs);
                }
            }
        }
        return permission;
    }

    @Override
    public List<Permission> readByModule(String moduleName) throws Exception {
        List<Permission> list = new ArrayList<>();
        String sql = "SELECT * FROM permission_tb WHERE module_name = ? AND permission_status = true";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, moduleName);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetRaw(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<Permission> readActivePermissions() throws Exception {
        List<Permission> list = new ArrayList<>();
        String sql = "SELECT * FROM permission_tb WHERE permission_status = true";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetRaw(rs));
            }
        }
        return list;
    }
}
