package org.perfectsmiles.system.repository.DAO;

import org.perfectsmiles.system.config.ConexionDB;
import org.perfectsmiles.system.model.Treatment;
import org.perfectsmiles.system.repository.ITreatmentDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TreatmentDAO implements ITreatmentDAO {

    private Treatment mapResultSetSP(ResultSet rs) throws SQLException {
        Treatment t = new Treatment();
        t.setIdTreatment(rs.getInt("ID_TREATMENT"));
        t.setIdUser(rs.getInt("ID_USER"));
        t.setInternalCode(rs.getString("Internal Code"));
        t.setTreatmentName(rs.getString("Treatment Name"));
        t.setStandardCost(rs.getBigDecimal("Standard Cost"));
        t.setTreatmentStatus(rs.getBoolean("Status"));
        t.setTreatmentDescription(rs.getString("Description"));
        return t;
    }

    private Treatment mapResultSetRaw(ResultSet rs) throws SQLException {
        Treatment t = new Treatment();
        t.setIdTreatment(rs.getInt("id_treatment"));
        t.setIdUser(rs.getInt("id_user"));
        t.setInternalCode(rs.getString("internal_code"));
        t.setTreatmentName(rs.getString("treatment_name"));
        t.setStandardCost(rs.getBigDecimal("standard_cost"));
        t.setTreatmentStatus(rs.getBoolean("treatment_status"));
        t.setTreatmentDescription(rs.getString("treatment_description"));
        return t;
    }

    @Override
    public boolean create(Treatment treatment) throws Exception {
        String sql = "{call sp_create_treatment(?, ?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionDB.getConnection(); CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, treatment.getIdUser());
            cs.setString(2, treatment.getInternalCode());
            cs.setString(3, treatment.getTreatmentName());
            cs.setBigDecimal(4, treatment.getStandardCost());
            cs.setBoolean(5, treatment.isTreatmentStatus());
            cs.setString(6, treatment.getTreatmentDescription());
            return cs.executeUpdate() > 0;
        }
    }

    @Override
    public List<Treatment> readAll() throws Exception {
        List<Treatment> list = new ArrayList<>();
        String sql = "{call sp_read_treatment()}";
        try (Connection conn = ConexionDB.getConnection(); CallableStatement cs = conn.prepareCall(sql); ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetSP(rs));
            }
        }
        return list;
    }

    @Override
    public Treatment readById(int id) throws Exception {
        Treatment treatment = null;
        String sql = "SELECT * FROM treatment_tb WHERE id_treatment = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    treatment = mapResultSetRaw(rs);
                }
            }
        }
        return treatment;
    }

    @Override
    public boolean update(Treatment treatment) throws Exception {
        String sql = "{call sp_edit_treatment(?, ?, ?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionDB.getConnection(); CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, treatment.getIdUser());
            cs.setString(2, treatment.getInternalCode());
            cs.setString(3, treatment.getTreatmentName());
            cs.setBigDecimal(4, treatment.getStandardCost());
            cs.setBoolean(5, treatment.isTreatmentStatus());
            cs.setString(6, treatment.getTreatmentDescription());
            cs.setInt(7, treatment.getIdTreatment());
            return cs.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws Exception {
        String sql = "{call sp_delete_treatment(?)}";
        try (Connection conn = ConexionDB.getConnection(); CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, id);
            return cs.executeUpdate() > 0;
        }
    }

    @Override
    public List<Treatment> readActiveTreatments() throws Exception {
        List<Treatment> list = new ArrayList<>();
        String sql = "SELECT * FROM treatment_tb WHERE treatment_status = true";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetRaw(rs));
            }
        }
        return list;
    }

    @Override
    public List<Treatment> readByUser(int idUser) throws Exception {
        List<Treatment> list = new ArrayList<>();
        String sql = "SELECT * FROM treatment_tb WHERE id_user = ? AND treatment_status = true";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUser);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetRaw(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<Treatment> searchByNameOrCode(String query) throws Exception {
        List<Treatment> list = new ArrayList<>();
        String sql = "SELECT * FROM treatment_tb WHERE "
                + "(LOWER(treatment_name) LIKE ? OR LOWER(internal_code) LIKE ?) "
                + "AND treatment_status = true";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            String q = "%" + query.toLowerCase() + "%";
            ps.setString(1, q);
            ps.setString(2, q);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetRaw(rs));
                }
            }
        }
        return list;
    }

    @Override
    public boolean existsByInternalCode(String internalCode) throws Exception {
        String sql = "SELECT 1 FROM treatment_tb WHERE internal_code = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, internalCode);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
