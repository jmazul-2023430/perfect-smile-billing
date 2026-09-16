package org.perfectsmiles.system.repository;

import org.perfectsmiles.system.config.ConexionDB;
import org.perfectsmiles.system.model.Budget;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



public class BudgetDAO {
     @Override
    public void create(Budget budget) throws Exception {
        String sql = "CALL sp_create_budget(?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, budget.getIdPatient());
            ps.setInt(2, budget.getIdUser());
            ps.setDate(3, budget.getIssueDate() != null ? Date.valueOf(budget.getIssueDate()) : null);
            ps.setBigDecimal(4, budget.getSubtotal());
            ps.setBigDecimal(5, budget.getTax());
            ps.setBigDecimal(6, budget.getTotal());
            ps.setBoolean(7, budget.isBudgetStatus());
            ps.executeUpdate();
        }
    }
    
     @Override
    public List<Budget> readAll() throws Exception {
        List<Budget> budgets = new ArrayList<>();
        String sql = "CALL sp_read_budget()";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Budget b = new Budget();
                b.setIdBudget(rs.getInt("ID_BUDGET"));
                b.setIdPatient(rs.getInt("ID_PATIENT"));
                b.setIdUser(rs.getInt("ID_USER"));
                b.setIssueDate(rs.getDate("Issue Date").toLocalDate());
                b.setSubtotal(rs.getBigDecimal("Subtotal"));
                b.setTax(rs.getBigDecimal("Tax"));
                b.setTotal(rs.getBigDecimal("Total"));
                b.setBudgetStatus(rs.getBoolean("Status"));
                budgets.add(b);
            }
        }
        return budgets;
    }
    
     @Override
    public Budget readById(int id) throws Exception {
        Budget budget = null;
        String sql = "SELECT * FROM budget_tb WHERE id_budget = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    budget = new Budget();
                    budget.setIdBudget(rs.getInt("id_budget"));
                    budget.setIdPatient(rs.getInt("id_patient"));
                    budget.setIdUser(rs.getInt("id_user"));
                    budget.setIssueDate(rs.getDate("issue_date").toLocalDate());
                    budget.setSubtotal(rs.getBigDecimal("subtotal"));
                    budget.setTax(rs.getBigDecimal("tax"));
                    budget.setTotal(rs.getBigDecimal("total"));
                    budget.setBudgetStatus(rs.getBoolean("budget_status"));
                }
            }
        }
        return budget;
    }
    
     @Override
    public void update(Budget budget) throws Exception {
        String sql = "CALL sp_edit_budget(?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, budget.getIdPatient());
            ps.setInt(2, budget.getIdUser());
            ps.setDate(3, budget.getIssueDate() != null ? Date.valueOf(budget.getIssueDate()) : null);
            ps.setBigDecimal(4, budget.getSubtotal());
            ps.setBigDecimal(5, budget.getTax());
            ps.setBigDecimal(6, budget.getTotal());
            ps.setBoolean(7, budget.isBudgetStatus());
            ps.setInt(8, budget.getIdBudget());
            ps.executeUpdate();
        }
    }
    
     @Override
    public void delete(int id) throws Exception {
        String sql = "CALL sp_delete_budget(?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
    
    
    
}
