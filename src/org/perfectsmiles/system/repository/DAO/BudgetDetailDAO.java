package org.perfectsmiles.system.repository.DAO;

import org.perfectsmiles.system.config.ConexionDB;
import org.perfectsmiles.system.model.BudgetDetail;
import org.perfectsmiles.system.repository.IBudgetDetailDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BudgetDetailDAO implements IBudgetDetailDAO {

    // ============================================================
    // MAPPERS
    // ============================================================
    private BudgetDetail mapResultSetSP(ResultSet rs) throws SQLException {
        BudgetDetail d = new BudgetDetail();
        d.setIdBudgetDetail(rs.getInt("ID_BUDGET_DETAIL"));
        d.setIdBudget(rs.getInt("ID_BUDGET"));
        d.setIdTreatment(rs.getInt("ID_TREATMENT"));
        d.setUnitPrice(rs.getBigDecimal("Unit Price"));
        d.setItemQuantity(rs.getInt("Quantity"));
        d.setSubtotal(rs.getBigDecimal("Subtotal"));
        return d;
    }

    private BudgetDetail mapResultSetRaw(ResultSet rs) throws SQLException {
        BudgetDetail d = new BudgetDetail();
        d.setIdBudgetDetail(rs.getInt("id_budget_detail"));
        d.setIdBudget(rs.getInt("id_budget"));
        d.setIdTreatment(rs.getInt("id_treatment"));
        d.setUnitPrice(rs.getBigDecimal("unit_price"));
        d.setItemQuantity(rs.getInt("item_quantity"));
        d.setSubtotal(rs.getBigDecimal("subtotal"));
        return d;
    }

    // ============================================================
    // 1. CREATE
    // ============================================================
    @Override
    public boolean create(BudgetDetail detail) throws Exception {
        String sql = "{call sp_create_budget_detail(?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionDB.getConnection(); CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, detail.getIdBudget());
            cs.setInt(2, detail.getIdTreatment());
            cs.setBigDecimal(3, detail.getUnitPrice());
            cs.setInt(4, detail.getItemQuantity());
            cs.setBigDecimal(5, detail.getSubtotal());
            return cs.executeUpdate() > 0;
        }
    }

    // ============================================================
    // 2. READ ALL
    // ============================================================
    @Override
    public List<BudgetDetail> readAll() throws Exception {
        List<BudgetDetail> list = new ArrayList<>();
        String sql = "{call sp_read_budget_detail()}";
        try (Connection conn = ConexionDB.getConnection(); CallableStatement cs = conn.prepareCall(sql); ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetSP(rs));
            }
        }
        return list;
    }

    // ============================================================
    // 3. READ BY ID
    // ============================================================
    @Override
    public BudgetDetail readById(int id) throws Exception {
        BudgetDetail detail = null;
        String sql = "SELECT * FROM budget_detail_tb WHERE id_budget_detail = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    detail = mapResultSetRaw(rs);
                }
            }
        }
        return detail;
    }

    // ============================================================
    // 4. UPDATE
    // ============================================================
    @Override
    public boolean update(BudgetDetail detail) throws Exception {
        String sql = "{call sp_edit_budget_detail(?, ?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionDB.getConnection(); CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, detail.getIdBudget());
            cs.setInt(2, detail.getIdTreatment());
            cs.setBigDecimal(3, detail.getUnitPrice());
            cs.setInt(4, detail.getItemQuantity());
            cs.setBigDecimal(5, detail.getSubtotal());
            cs.setInt(6, detail.getIdBudgetDetail());
            return cs.executeUpdate() > 0;
        }
    }

    // ============================================================
    // 5. DELETE (físico en BD)
    // ============================================================
    @Override
    public boolean delete(int id) throws Exception {
        String sql = "{call sp_delete_budget_detail(?)}";
        try (Connection conn = ConexionDB.getConnection(); CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, id);
            return cs.executeUpdate() > 0;
        }
    }

    // ============================================================
    // MÉTODOS EXTRA
    // ============================================================
    @Override
    public List<BudgetDetail> readByBudget(int idBudget) throws Exception {
        List<BudgetDetail> list = new ArrayList<>();
        String sql = "SELECT * FROM budget_detail_tb WHERE id_budget = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idBudget);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetRaw(rs));
                }
            }
        }
        return list;
    }
}
