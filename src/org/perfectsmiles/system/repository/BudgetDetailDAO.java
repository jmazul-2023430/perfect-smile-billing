package org.perfectsmiles.system.repository;

import org.perfectsmiles.system.config.ConexionDB;
import org.perfectsmiles.system.model.BudgetDetail;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



public class BudgetDetailDAO {
    @Override
    public void create(BudgetDetail detail) throws Exception {
        String sql = "CALL sp_create_budget_detail(?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, detail.getIdBudget());
            ps.setInt(2, detail.getIdTreatment());
            ps.setBigDecimal(3, detail.getUnitPrice());
            ps.setInt(4, detail.getItemQuantity());
            ps.setBigDecimal(5, detail.getSubtotal());
            ps.executeUpdate();
        }
    }
    
    @Override
    public List<BudgetDetail> readAll() throws Exception {
        List<BudgetDetail> details = new ArrayList<>();
        String sql = "CALL sp_read_budget_detail()";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                BudgetDetail bd = new BudgetDetail();
                bd.setIdBudgetDetail(rs.getInt("ID_BUDGET_DETAIL"));
                bd.setIdBudget(rs.getInt("ID_BUDGET"));
                bd.setIdTreatment(rs.getInt("ID_TREATMENT"));
                bd.setUnitPrice(rs.getBigDecimal("Unit Price"));
                bd.setItemQuantity(rs.getInt("Quantity"));
                bd.setSubtotal(rs.getBigDecimal("Subtotal"));
                details.add(bd);
            }
        }
        return details;
    }
    
     @Override
    public BudgetDetail readById(int id) throws Exception {
        BudgetDetail detail = null;
        String sql = "SELECT * FROM budget_detail_tb WHERE id_budget_detail = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    detail = new BudgetDetail();
                    detail.setIdBudgetDetail(rs.getInt("id_budget_detail"));
                    detail.setIdBudget(rs.getInt("id_budget"));
                    detail.setIdTreatment(rs.getInt("id_treatment"));
                    detail.setUnitPrice(rs.getBigDecimal("unit_price"));
                    detail.setItemQuantity(rs.getInt("item_quantity"));
                    detail.setSubtotal(rs.getBigDecimal("subtotal"));
                }
            }
        }
        return detail;
    }
    
     @Override
    public void update(BudgetDetail detail) throws Exception {
        String sql = "CALL sp_edit_budget_detail(?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, detail.getIdBudget());
            ps.setInt(2, detail.getIdTreatment());
            ps.setBigDecimal(3, detail.getUnitPrice());
            ps.setInt(4, detail.getItemQuantity());
            ps.setBigDecimal(5, detail.getSubtotal());
            ps.setInt(6, detail.getIdBudgetDetail());
            ps.executeUpdate();
        }
    }
    
     @Override
    public void delete(int id) throws Exception {
        String sql = "CALL sp_delete_budget_detail(?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
    
}
