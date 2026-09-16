package org.perfectsmiles.system.repository;


import org.perfectsmiles.system.config.ConexionDB;
import org.perfectsmiles.system.model.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;





public class UserDAO {
    @Override
    public void create(User user) throws Exception {
        String sql = "CALL sp_create_user(?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, user.getIdRole());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getFullName());
            ps.setString(5, user.getEmail());
            ps.setString(6, user.getPhone());
            ps.setTimestamp(7, user.getLastAccess() != null ? Timestamp.valueOf(user.getLastAccess()) : null);
            ps.setBoolean(8, user.isUserStatus());
            ps.executeUpdate();
        }
    }
    
    
     @Override
    public List<User> readAll() throws Exception {
        List<User> users = new ArrayList<>();
        String sql = "CALL sp_read_user()";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                User u = new User();
                u.setIdUser(rs.getInt("ID_USER"));
                u.setIdRole(rs.getInt("ID_ROLE"));
                u.setUsername(rs.getString("Username"));
                u.setFullName(rs.getString("Full Name"));
                u.setEmail(rs.getString("Email"));
                u.setPhone(rs.getString("Phone"));
                Timestamp ts = rs.getTimestamp("Last Access");
                if (ts != null) u.setLastAccess(ts.toLocalDateTime());
                u.setUserStatus(rs.getBoolean("Status"));
                users.add(u);
            }
        }
        return users;
    }
    
    
    @Override
    public User readById(int id) throws Exception {
        User user = null;
        String sql = "SELECT * FROM user_tb WHERE id_user = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setIdUser(rs.getInt("id_user"));
                    user.setIdRole(rs.getInt("id_role"));
                    user.setUsername(rs.getString("username"));
                    user.setPasswordHash(rs.getString("password_hash"));
                    user.setFullName(rs.getString("full_name"));
                    user.setEmail(rs.getString("email"));
                    user.setPhone(rs.getString("phone"));
                    Timestamp ts = rs.getTimestamp("last_access");
                    if (ts != null) user.setLastAccess(ts.toLocalDateTime());
                    user.setUserStatus(rs.getBoolean("user_status"));
                }
            }
        }
        return user;
    }
    
    @Override
    public void update(User user) throws Exception {
        String sql = "CALL sp_edit_user(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, user.getIdRole());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getFullName());
            ps.setString(5, user.getEmail());
            ps.setString(6, user.getPhone());
            ps.setTimestamp(7, user.getLastAccess() != null ? Timestamp.valueOf(user.getLastAccess()) : null);
            ps.setBoolean(8, user.isUserStatus());
            ps.setInt(9, user.getIdUser());
            ps.executeUpdate();
        }
    }
    
    
      @Override
    public void delete(int id) throws Exception {
        String sql = "CALL sp_delete_user(?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
    
    
    
}
