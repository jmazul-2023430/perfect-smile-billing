package org.perfectsmiles.system.repository;

import org.perfectsmiles.system.config.ConexionDB;
import org.perfectsmiles.system.model.Treatment;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TreatmentDAO {
     @Override
    public void create(Treatment treatment) throws Exception {
        String sql = "CALL sp_create_treatment(?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, treatment.getIdUser());
            ps.setString(2, treatment.getInternalCode());
            ps.setString(3, treatment.getTreatmentName());
            ps.setBigDecimal(4, treatment.getStandardCost());
            ps.setBoolean(5, treatment.isTreatmentStatus());
            ps.setString(6, treatment.getTreatmentDescription());
            ps.executeUpdate();
        }
    }
    
     @Override
    public List<Treatment> readAll() throws Exception {
        List<Treatment> treatments = new ArrayList<>();
        String sql = "CALL sp_read_treatment()";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Treatment t = new Treatment();
                t.setIdTreatment(rs.getInt("ID_TREATMENT"));
                t.setIdUser(rs.getInt("ID_USER"));
                t.setInternalCode(rs.getString("Internal Code"));
                t.setTreatmentName(rs.getString("Treatment Name"));
                t.setStandardCost(rs.getBigDecimal("Standard Cost"));
                t.setTreatmentStatus(rs.getBoolean("Status"));
                t.setTreatmentDescription(rs.getString("Description"));
                treatments.add(t);
            }
        }
        return treatments;
    }
    
    @Override
    public Treatment readById(int id) throws Exception {
        Treatment treatment = null;
        String sql = "SELECT * FROM treatment_tb WHERE id_treatment = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    treatment = new Treatment();
                    treatment.setIdTreatment(rs.getInt("id_treatment"));
                    treatment.setIdUser(rs.getInt("id_user"));
                    treatment.setInternalCode(rs.getString("internal_code"));
                    treatment.setTreatmentName(rs.getString("treatment_name"));
                    treatment.setStandardCost(rs.getBigDecimal("standard_cost"));
                    treatment.setTreatmentStatus(rs.getBoolean("treatment_status"));
                    treatment.setTreatmentDescription(rs.getString("treatment_description"));
                }
            }
        }
        return treatment;
    }
    
      @Override
    public void update(Treatment treatment) throws Exception {
        String sql = "CALL sp_edit_treatment(?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, treatment.getIdUser());
            ps.setString(2, treatment.getInternalCode());
            ps.setString(3, treatment.getTreatmentName());
            ps.setBigDecimal(4, treatment.getStandardCost());
            ps.setBoolean(5, treatment.isTreatmentStatus());
            ps.setString(6, treatment.getTreatmentDescription());
            ps.setInt(7, treatment.getIdTreatment());
            ps.executeUpdate();
        }
    }
    
    
    @Override
    public void delete(int id) throws Exception {
        String sql = "CALL sp_delete_treatment(?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
    
    
}
