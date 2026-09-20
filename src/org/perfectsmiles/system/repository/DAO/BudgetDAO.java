package org.perfectsmiles.system.repository.DAO;

import org.perfectsmiles.system.config.ConexionDB;
import org.perfectsmiles.system.model.Budget;
import org.perfectsmiles.system.repository.IBudgetDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BudgetDAO implements IBudgetDAO {

    private Budget mapResultSetSP(ResultSet rs) throws SQLException {
        Budget b = new Budget();
        b.setIdBudget(rs.getInt("ID_BUDGET"));
        b.setIdPatient(rs.getInt("ID_PATIENT"));
        b.setIdUser(rs.getInt("ID_USER"));
        b.setDescriptionBudget(rs.getString("Description"));
        b.setIssueDate(rs.getDate("Issue Date").toLocalDate());
        b.setSubtotal(rs.getBigDecimal("Subtotal"));
        b.setTax(rs.getBigDecimal("Tax"));
        b.setTotal(rs.getBigDecimal("Total"));
        b.setBudgetStatus(rs.getBoolean("Status"));
        return b;
    }

    private Budget mapResultSetRaw(ResultSet rs) throws SQLException {
        Budget b = new Budget();
        b.setIdBudget(rs.getInt("id_budget"));
        b.setIdPatient(rs.getInt("id_patient"));
        b.setIdUser(rs.getInt("id_user"));
        b.setDescriptionBudget(rs.getString("description_budget"));
        b.setIssueDate(rs.getDate("issue_date").toLocalDate());
        b.setSubtotal(rs.getBigDecimal("subtotal"));
        b.setTax(rs.getBigDecimal("tax"));
        b.setTotal(rs.getBigDecimal("total"));
        b.setBudgetStatus(rs.getBoolean("budget_status"));
        return b;
    }

    @Override
    public boolean create(Budget budget) throws Exception {
        String sql = "{call sp_create_budget(?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionDB.getConnection(); CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, budget.getIdPatient());
            cs.setInt(2, budget.getIdUser());
            cs.setString(3, budget.getDescriptionBudget());
            cs.setDate(4, Date.valueOf(budget.getIssueDate()));
            cs.setBigDecimal(5, budget.getSubtotal());
            cs.setBigDecimal(6, budget.getTax());
            cs.setBigDecimal(7, budget.getTotal());
            cs.setBoolean(8, budget.isBudgetStatus());
            return cs.executeUpdate() > 0;
        }
    }
    
    @Override
    public List<Budget> readAll() throws Exception {
        List<Budget> list = new ArrayList<>();
        String sql = "{call sp_read_budget()}";
        try (Connection conn = ConexionDB.getConnection(); CallableStatement cs = conn.prepareCall(sql); ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetSP(rs));
            }
        }
        return list;
    }

    @Override
    public Budget readById(int id) throws Exception {
        Budget budget = null;
        String sql = "SELECT * FROM budget_tb WHERE id_budget = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    budget = mapResultSetRaw(rs);
                }
            }
        }
        return budget;
    }

    @Override
    public boolean update(Budget budget) throws Exception {
        String sql = "{call sp_edit_budget(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionDB.getConnection(); CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, budget.getIdPatient());
            cs.setInt(2, budget.getIdUser());
            cs.setString(3, budget.getDescriptionBudget());
            cs.setDate(4, Date.valueOf(budget.getIssueDate()));
            cs.setBigDecimal(5, budget.getSubtotal());
            cs.setBigDecimal(6, budget.getTax());
            cs.setBigDecimal(7, budget.getTotal());
            cs.setBoolean(8, budget.isBudgetStatus());
            cs.setInt(9, budget.getIdBudget());
            return cs.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws Exception {
        String sql = "{call sp_delete_budget(?)}";
        try (Connection conn = ConexionDB.getConnection(); CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, id);
            return cs.executeUpdate() > 0;
        }
    }

    @Override
    public List<Budget> readActiveBudgets() throws Exception {
        List<Budget> list = new ArrayList<>();
        String sql = "SELECT * FROM budget_tb WHERE budget_status = true";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetRaw(rs));
            }
        }
        return list;
    }

    @Override
    public List<Budget> readByPatient(int idPatient) throws Exception {
        List<Budget> list = new ArrayList<>();
        String sql = "SELECT * FROM budget_tb WHERE id_patient = ? AND budget_status = true";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPatient);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetRaw(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<Budget> readByUser(int idUser) throws Exception {
        List<Budget> list = new ArrayList<>();
        String sql = "SELECT * FROM budget_tb WHERE id_user = ? AND budget_status = true";
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
    public boolean changeStatus(int idBudget, boolean newStatus) throws Exception {
        String sql = "UPDATE budget_tb SET budget_status = ? WHERE id_budget = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, newStatus);
            ps.setInt(2, idBudget);
            return ps.executeUpdate() > 0;
        }
    }
}
