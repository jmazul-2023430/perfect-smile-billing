package org.perfectsmiles.system.repository.DAO;

import org.perfectsmiles.system.config.ConexionDB;
import org.perfectsmiles.system.model.Patient;
import org.perfectsmiles.system.repository.IPatientDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO implements IPatientDAO {

    private Patient mapResultSetSP(ResultSet rs) throws SQLException {
        Patient p = new Patient();
        p.setIdPatient(rs.getInt("ID_PATIENT"));
        p.setFirstName(rs.getString("First Name"));
        p.setLastName(rs.getString("Last Name"));
        p.setDpi(rs.getString("DPI"));
        p.setPhone(rs.getString("Phone"));
        p.setEmail(rs.getString("Email"));
        p.setAddress(rs.getString("Address"));
        p.setPatientStatus(rs.getBoolean("Status"));
        return p;
    }

    private Patient mapResultSetRaw(ResultSet rs) throws SQLException {
        Patient p = new Patient();
        p.setIdPatient(rs.getInt("id_patient"));
        p.setFirstName(rs.getString("first_name"));
        p.setLastName(rs.getString("last_name"));
        p.setDpi(rs.getString("dpi"));
        p.setPhone(rs.getString("phone"));
        p.setEmail(rs.getString("email"));
        p.setAddress(rs.getString("address"));
        p.setPatientStatus(rs.getBoolean("patient_status"));
        return p;
    }

    @Override
    public boolean create(Patient patient) throws Exception {
        String sql = "{call sp_create_patient(?, ?, ?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionDB.getConnection(); CallableStatement cs = conn.prepareCall(sql)) {
            cs.setString(1, patient.getFirstName());
            cs.setString(2, patient.getLastName());
            cs.setString(3, patient.getDpi());
            cs.setString(4, patient.getPhone());
            cs.setString(5, patient.getEmail());
            cs.setString(6, patient.getAddress());
            cs.setBoolean(7, patient.isPatientStatus());
            return cs.executeUpdate() > 0;
        }
    }

    @Override
    public List<Patient> readAll() throws Exception {
        List<Patient> patients = new ArrayList<>();
        String sql = "{call sp_read_patient()}";
        try (Connection conn = ConexionDB.getConnection(); CallableStatement cs = conn.prepareCall(sql); ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                patients.add(mapResultSetSP(rs));
            }
        }
        return patients;
    }

    @Override
    public Patient readById(int id) throws Exception {
        Patient patient = null;
        String sql = "SELECT * FROM patient_tb WHERE id_patient = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    patient = mapResultSetRaw(rs);
                }
            }
        }
        return patient;
    }

    @Override
    public boolean update(Patient patient) throws Exception {
        String sql = "{call sp_edit_patient(?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionDB.getConnection(); CallableStatement cs = conn.prepareCall(sql)) {
            cs.setString(1, patient.getFirstName());
            cs.setString(2, patient.getLastName());
            cs.setString(3, patient.getDpi());
            cs.setString(4, patient.getPhone());
            cs.setString(5, patient.getEmail());
            cs.setString(6, patient.getAddress());
            cs.setBoolean(7, patient.isPatientStatus());
            cs.setInt(8, patient.getIdPatient());
            return cs.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws Exception {
        String sql = "{call sp_delete_patient(?)}";
        try (Connection conn = ConexionDB.getConnection(); CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, id);
            return cs.executeUpdate() > 0;
        }
    }

    @Override
    public Patient readByDpi(String dpi) throws Exception {
        Patient patient = null;
        String sql = "SELECT * FROM patient_tb WHERE dpi = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dpi);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    patient = mapResultSetRaw(rs);
                }
            }
        }
        return patient;
    }

    @Override
    public List<Patient> readActivePatients() throws Exception {
        List<Patient> list = new ArrayList<>();
        String sql = "SELECT * FROM patient_tb WHERE patient_status = true";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetRaw(rs));
            }
        }
        return list;
    }

    @Override
    public List<Patient> searchByName(String query) throws Exception {
        List<Patient> list = new ArrayList<>();
        String sql = "SELECT * FROM patient_tb WHERE "
                + "(LOWER(first_name) LIKE ? OR LOWER(last_name) LIKE ?) "
                + "AND patient_status = true";
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
    public boolean existsByDpi(String dpi) throws Exception {
        String sql = "SELECT 1 FROM patient_tb WHERE dpi = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dpi);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
