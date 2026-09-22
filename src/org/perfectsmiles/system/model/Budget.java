package org.perfectsmiles.system.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Budget {

    private int idBudget;
    private int idPatient;
    private int idUser;
    private String descriptionBudget;
    private LocalDate issueDate;
    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal total;
    private boolean budgetStatus;

    public Budget() {
    }

    public Budget(int idBudget, int idPatient, int idUser, String descriptionBudget,
            LocalDate issueDate, BigDecimal subtotal, BigDecimal tax,
            BigDecimal total, boolean budgetStatus) {
        this.idBudget = idBudget;
        this.idPatient = idPatient;
        this.idUser = idUser;
        this.descriptionBudget = descriptionBudget;
        this.issueDate = issueDate;
        this.subtotal = subtotal;
        this.tax = tax;
        this.total = total;
        this.budgetStatus = budgetStatus;
    }

    public int getIdBudget() {
        return idBudget;
    }

    public void setIdBudget(int idBudget) {
        this.idBudget = idBudget;
    }

    public int getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(int idPatient) {
        this.idPatient = idPatient;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getDescriptionBudget() {
        return descriptionBudget;
    }

    public void setDescriptionBudget(String descriptionBudget) {
        this.descriptionBudget = descriptionBudget;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public boolean isBudgetStatus() {
        return budgetStatus;
    }

    public void setBudgetStatus(boolean budgetStatus) {
        this.budgetStatus = budgetStatus;
    }

    @Override
    public String toString() {
        return "Budget{id=" + idBudget + ", patient=" + idPatient
                + ", user=" + idUser + ", total=" + total + ", status=" + budgetStatus + "}";
    }
}
