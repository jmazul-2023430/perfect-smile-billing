package org.perfectsmiles.system.service;

import org.perfectsmiles.system.model.BudgetDetail;
import org.perfectsmiles.system.repository.DAO.BudgetDetailDAO;
import org.perfectsmiles.system.repository.IBudgetDetailDAO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class BudgetDetailService {

    private final IBudgetDetailDAO detailDAO;

    public BudgetDetailService() {
        this.detailDAO = new BudgetDetailDAO();
    }

    public boolean addDetail(BudgetDetail detail) throws Exception {
        if (detail.getIdBudget() <= 0) {
            throw new IllegalArgumentException("El detalle debe pertenecer a un presupuesto.");
        }
        if (detail.getIdTreatment() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar un tratamiento.");
        }
        if (detail.getUnitPrice() == null || detail.getUnitPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio unitario no puede ser negativo.");
        }
        if (detail.getItemQuantity() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }

        // Calcular subtotal
        BigDecimal subtotal = detail.getUnitPrice()
                .multiply(BigDecimal.valueOf(detail.getItemQuantity()))
                .setScale(2, RoundingMode.HALF_UP);
        detail.setSubtotal(subtotal);

        return detailDAO.create(detail);
    }

    public List<BudgetDetail> getDetailsByBudget(int idBudget) throws Exception {
        return detailDAO.readByBudget(idBudget);
    }

    public BudgetDetail getDetailById(int id) throws Exception {
        return detailDAO.readById(id);
    }

    public boolean updateDetail(BudgetDetail detail) throws Exception {
        BigDecimal subtotal = detail.getUnitPrice()
                .multiply(BigDecimal.valueOf(detail.getItemQuantity()))
                .setScale(2, RoundingMode.HALF_UP);
        detail.setSubtotal(subtotal);
        return detailDAO.update(detail);
    }

    public boolean deleteDetail(int id) throws Exception {
        return detailDAO.delete(id);
    }
}