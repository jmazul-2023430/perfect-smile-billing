package org.perfectsmiles.system.repository;

import org.perfectsmiles.system.config.ConexionDB;
import org.perfectsmiles.system.model.Patient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



public class PatientDAO {
     @Override
    public void create(Patient patient) throws Exception {
        String sql = "CALL sp_create_patient(?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, patient.getFirstName());
            ps.setString(2, patient.getLastName());
            ps.setString(3, patient.getDpi());
            ps.setString(4, patient.getPhone());
            ps.setString(5, patient.getEmail());
            ps.setString(6, patient.getAddress());
            ps.setBoolean(7, patient.isPatientStatus());
            ps.executeUpdate();
        }
    }
    
     @Override
    public List<Patient> readAll() throws Exception {
        List<Patient> patients = new ArrayList<>();
        String sql = "CALL sp_read_patient()";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Patient p = new Patient();
                p.setIdPatient(rs.getInt("ID_PATIENT"));
                p.setFirstName(rs.getString("First Name"));
                p.setLastName(rs.getString("Last Name"));
                p.setDpi(rs.getString("DPI"));
                p.setPhone(rs.getString("Phone"));
                p.setEmail(rs.getString("Email"));
                p.setAddress(rs.getString("Address"));
                p.setPatientStatus(rs.getBoolean("Status"));
                patients.add(p);
            }
        }
        return patients;
    }
    
    @Override
    public Patient readById(int id) throws Exception {
        Patient patient = null;
        String sql = "SELECT * FROM patient_tb WHERE id_patient = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    patient = new Patient();
                    patient.setIdPatient(rs.getInt("id_patient"));
                    patient.setFirstName(rs.getString("first_name"));
                    patient.setLastName(rs.getString("last_name"));
                    patient.setDpi(rs.getString("dpi"));
                    patient.setPhone(rs.getString("phone"));
                    patient.setEmail(rs.getString("email"));
                    patient.setAddress(rs.getString("address"));
                    patient.setPatientStatus(rs.getBoolean("patient_status"));
                }
            }
        }
        return patient;
    }
    
    
    @Override
    public void update(Patient patient) throws Exception {
        String sql = "CALL sp_edit_patient(?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, patient.getFirstName());
            ps.setString(2, patient.getLastName());
            ps.setString(3, patient.getDpi());
            ps.setString(4, patient.getPhone());
            ps.setString(5, patient.getEmail());
            ps.setString(6, patient.getAddress());
            ps.setBoolean(7, patient.isPatientStatus());
            ps.setInt(8, patient.getIdPatient());
            ps.executeUpdate();
        }
    }
    
    
      @Override
    public void delete(int id) throws Exception {
        String sql = "CALL sp_delete_patient(?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
    
    
    
}
