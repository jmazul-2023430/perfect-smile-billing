package org.perfectsmiles.system.service;

import org.perfectsmiles.system.config.Enviroment;
import org.perfectsmiles.system.model.Budget;
import org.perfectsmiles.system.model.BudgetDetail;
import org.perfectsmiles.system.repository.DAO.BudgetDAO;
import org.perfectsmiles.system.repository.DAO.BudgetDetailDAO;
import org.perfectsmiles.system.repository.IBudgetDAO;
import org.perfectsmiles.system.repository.IBudgetDetailDAO;
import org.perfectsmiles.system.utils.Validations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

public class BudgetService {

    private final IBudgetDAO budgetDAO;
    private final IBudgetDetailDAO detailDAO;

    public BudgetService() {
        this.budgetDAO = new BudgetDAO();
        this.detailDAO = new BudgetDetailDAO();
    }

    public boolean createBudget(Budget budget, List<BudgetDetail> details) throws Exception {
        if (budget.getIdPatient() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar un paciente.");
        }
        if (budget.getIdUser() <= 0) {
            throw new IllegalArgumentException("Debe indicarse el usuario que crea el presupuesto.");
        }
        if (details == null || details.isEmpty()) {
            throw new IllegalArgumentException("Debe agregar al menos un tratamiento.");
        }

        BigDecimal subtotalGeneral = BigDecimal.ZERO;
        for (BudgetDetail d : details) {
            if (d.getUnitPrice() == null || d.getUnitPrice().compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("El precio unitario no puede ser negativo.");
            }
            if (d.getItemQuantity() <= 0) {
                throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
            }
            BigDecimal lineSubtotal = d.getUnitPrice()
                    .multiply(BigDecimal.valueOf(d.getItemQuantity()))
                    .setScale(2, RoundingMode.HALF_UP);
            d.setSubtotal(lineSubtotal);
            subtotalGeneral = subtotalGeneral.add(lineSubtotal);
        }

        BigDecimal tax = subtotalGeneral.multiply(Enviroment.TAX_RATE)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotalGeneral.add(tax).setScale(2, RoundingMode.HALF_UP);

        budget.setSubtotal(subtotalGeneral);
        budget.setTax(tax);
        budget.setTotal(total);
        budget.setIssueDate(LocalDate.now());
        budget.setBudgetStatus(true);

        if (!budgetDAO.create(budget)) {
            throw new RuntimeException("No se pudo crear el presupuesto.");
        }

        int idBudget = findLastInsertedBudgetId(budget);

        for (BudgetDetail d : details) {
            d.setIdBudget(idBudget);
            if (!detailDAO.create(d)) {
                throw new RuntimeException("Error al insertar un detalle.");
            }
        }
        return true;
    }

    public boolean updateBudgetWithDetails(Budget budget, List<BudgetDetail> details) throws Exception {
        if (budget.getIdPatient() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar un paciente.");
        }
        if (details == null || details.isEmpty()) {
            throw new IllegalArgumentException("Debe agregar al menos un tratamiento.");
        }

        BigDecimal subtotalGeneral = BigDecimal.ZERO;
        for (BudgetDetail d : details) {
            if (d.getUnitPrice() == null || d.getUnitPrice().compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("El precio unitario no puede ser negativo.");
            }
            if (d.getItemQuantity() <= 0) {
                throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
            }
            BigDecimal lineSubtotal = d.getUnitPrice()
                    .multiply(BigDecimal.valueOf(d.getItemQuantity()))
                    .setScale(2, RoundingMode.HALF_UP);
            d.setSubtotal(lineSubtotal);
            subtotalGeneral = subtotalGeneral.add(lineSubtotal);
        }

        BigDecimal tax = subtotalGeneral.multiply(Enviroment.TAX_RATE)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotalGeneral.add(tax).setScale(2, RoundingMode.HALF_UP);

        budget.setSubtotal(subtotalGeneral);
        budget.setTax(tax);
        budget.setTotal(total);

        if (!budgetDAO.update(budget)) {
            throw new RuntimeException("No se pudo actualizar el presupuesto.");
        }

        List<BudgetDetail> oldDetails = detailDAO.readByBudget(budget.getIdBudget());
        for (BudgetDetail old : oldDetails) {
            detailDAO.delete(old.getIdBudgetDetail());
        }

        for (BudgetDetail d : details) {
            d.setIdBudgetDetail(0);
            d.setIdBudget(budget.getIdBudget());
            if (!detailDAO.create(d)) {
                throw new RuntimeException("Error al insertar un detalle.");
            }
        }
        return true;
    }

    private int findLastInsertedBudgetId(Budget budget) throws Exception {
        List<Budget> all = budgetDAO.readAll();
        int maxId = -1;
        for (Budget b : all) {
            if (b.getIdPatient() == budget.getIdPatient()
                    && b.getIdUser() == budget.getIdUser()
                    && maxId < b.getIdBudget()) {
                maxId = b.getIdBudget();
            }
        }
        if (maxId < 0) {
            throw new RuntimeException("No se pudo recuperar el ID del presupuesto.");
        }
        return maxId;
    }

    public List<Budget> getAllBudgets() throws Exception {
        return budgetDAO.readAll();
    }

    public List<Budget> getActiveBudgets() throws Exception {
        return budgetDAO.readActiveBudgets();
    }

    public List<Budget> getBudgetsByPatient(int idPatient) throws Exception {
        return budgetDAO.readByPatient(idPatient);
    }

    public List<Budget> getBudgetsByUser(int idUser) throws Exception {
        return budgetDAO.readByUser(idUser);
    }

    public Budget getBudgetById(int id) throws Exception {
        return budgetDAO.readById(id);
    }

    public List<BudgetDetail> getDetailsByBudget(int idBudget) throws Exception {
        return detailDAO.readByBudget(idBudget);
    }

    public boolean updateBudget(Budget budget) throws Exception {
        if (Validations.isNullOrEmpty(budget.getDescriptionBudget())) {
            budget.setDescriptionBudget("Presupuesto sin descripcion");
        }
        return budgetDAO.update(budget);
    }

    public boolean approveBudget(int idBudget) throws Exception {
        return budgetDAO.changeStatus(idBudget, true);
    }

    public boolean rejectBudget(int idBudget) throws Exception {
        return budgetDAO.changeStatus(idBudget, false);
    }

    public boolean deactivateBudget(int idBudget) throws Exception {
        return budgetDAO.delete(idBudget);
    }

    public BigDecimal[] calculateTotals(List<BudgetDetail> details) {
        BigDecimal subtotal = BigDecimal.ZERO;
        for (BudgetDetail d : details) {
            if (d.getUnitPrice() != null && d.getItemQuantity() > 0) {
                subtotal = subtotal.add(
                        d.getUnitPrice().multiply(BigDecimal.valueOf(d.getItemQuantity()))
                );
            }
        }
        subtotal = subtotal.setScale(2, RoundingMode.HALF_UP);
        BigDecimal tax = subtotal.multiply(Enviroment.TAX_RATE)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotal.add(tax).setScale(2, RoundingMode.HALF_UP);
        return new BigDecimal[]{subtotal, tax, total};
    }
}
