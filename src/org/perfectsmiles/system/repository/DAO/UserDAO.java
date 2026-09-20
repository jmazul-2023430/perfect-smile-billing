package org.perfectsmiles.system.repository.DAO;

import org.perfectsmiles.system.config.ConexionDB;
import org.perfectsmiles.system.model.User;
import org.perfectsmiles.system.repository.IUserDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements IUserDAO {

    private User mapResultSetSP(ResultSet rs) throws SQLException {
        User u = new User();
        u.setIdUser(rs.getInt("ID_USER"));
        u.setIdRole(rs.getInt("ID_ROLE"));
        u.setUserName(rs.getString("Username"));
        u.setCompleteName(rs.getString("Full Name"));
        u.setEmail(rs.getString("Email"));
        u.setPhone(rs.getString("Phone"));
        Timestamp ts = rs.getTimestamp("Last Access");
        if (ts != null) {
            u.setLastAccess(ts.toLocalDateTime());
        }
        u.setActive(rs.getBoolean("Status"));
        return u;
    }

    // Mapper para SELECT directo
    private User mapResultSetRaw(ResultSet rs) throws SQLException {
        User u = new User();
        u.setIdUser(rs.getInt("id_user"));
        u.setIdRole(rs.getInt("id_role"));
        u.setUserName(rs.getString("username"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setCompleteName(rs.getString("full_name"));
        u.setEmail(rs.getString("email"));
        u.setPhone(rs.getString("phone"));
        Timestamp ts = rs.getTimestamp("last_access");
        if (ts != null) {
            u.setLastAccess(ts.toLocalDateTime());
        }
        u.setActive(rs.getBoolean("user_status"));
        return u;
    }

    @Override
    public boolean create(User user) throws Exception {
        String sql = "{call sp_create_user(?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionDB.getConnection(); CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, user.getIdRole());
            cs.setString(2, user.getUserName());
            cs.setString(3, user.getPasswordHash());
            cs.setString(4, user.getCompleteName());
            cs.setString(5, user.getEmail());
            cs.setString(6, user.getPhone());
            cs.setTimestamp(7, user.getLastAccess() != null ? Timestamp.valueOf(user.getLastAccess()) : null);
            cs.setBoolean(8, user.isActive());
            return cs.executeUpdate() > 0;
        }
    }

    @Override
    public List<User> readAll() throws Exception {
        List<User> users = new ArrayList<>();
        String sql = "{call sp_read_user()}";
        try (Connection conn = ConexionDB.getConnection(); CallableStatement cs = conn.prepareCall(sql); ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                users.add(mapResultSetSP(rs));
            }
        }
        return users;
    }

    @Override
    public User readById(int id) throws Exception {
        User user = null;
        String sql = "SELECT * FROM user_tb WHERE id_user = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = mapResultSetRaw(rs);
                }
            }
        }
        return user;
    }

    @Override
    public boolean update(User user) throws Exception {
        String sql = "{call sp_edit_user(?, ?, ?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionDB.getConnection(); CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, user.getIdRole());
            cs.setString(2, user.getUserName());
            cs.setString(3, user.getCompleteName());
            cs.setString(4, user.getEmail());
            cs.setString(5, user.getPhone());
            cs.setBoolean(6, user.isActive());
            cs.setInt(7, user.getIdUser());
            return cs.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws Exception {
        String sql = "{call sp_delete_user(?)}";
        try (Connection conn = ConexionDB.getConnection(); CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, id);
            return cs.executeUpdate() > 0;
        }
    }

    @Override
    public User readByUsername(String username) throws Exception {
        User user = null;
        String sql = "SELECT * FROM user_tb WHERE username = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = mapResultSetRaw(rs);
                }
            }
        }
        return user;
    }

    @Override
    public User login(String username, String passwordHash) throws Exception {
        User user = null;
        String sql = "{call sp_login_user(?, ?)}";
        try (Connection conn = ConexionDB.getConnection(); CallableStatement cs = conn.prepareCall(sql)) {
            cs.setString(1, username);
            cs.setString(2, passwordHash);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    user = mapResultSetSP(rs);
                }
            }
        }
        return user;
    }

    @Override
    public boolean changePassword(int idUser, String newPasswordHash) throws Exception {
        String sql = "{call sp_change_user_password(?, ?)}";
        try (Connection conn = ConexionDB.getConnection(); CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, idUser);
            cs.setString(2, newPasswordHash);
            return cs.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateLastAccess(int idUser) throws Exception {
        String sql = "{call sp_update_last_access(?)}";
        try (Connection conn = ConexionDB.getConnection(); CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, idUser);
            return cs.executeUpdate() > 0;
        }
    }

    @Override
    public boolean existsByUsername(String username) throws Exception {
        String sql = "SELECT 1 FROM user_tb WHERE username = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public boolean existsByEmail(String email) throws Exception {
        String sql = "SELECT 1 FROM user_tb WHERE email = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
