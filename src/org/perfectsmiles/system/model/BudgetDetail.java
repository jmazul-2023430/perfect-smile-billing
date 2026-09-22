package org.perfectsmiles.system.model;

import java.math.BigDecimal;

public class BudgetDetail {

    private int idBudgetDetail;
    private int idBudget;
    private int idTreatment;
    private BigDecimal unitPrice;
    private int itemQuantity;
    private BigDecimal subtotal;

    public BudgetDetail() {
    }

    public BudgetDetail(int idBudgetDetail, int idBudget, int idTreatment,
            BigDecimal unitPrice, int itemQuantity, BigDecimal subtotal) {
        this.idBudgetDetail = idBudgetDetail;
        this.idBudget = idBudget;
        this.idTreatment = idTreatment;
        this.unitPrice = unitPrice;
        this.itemQuantity = itemQuantity;
        this.subtotal = subtotal;
    }

    public int getIdBudgetDetail() {
        return idBudgetDetail;
    }

    public void setIdBudgetDetail(int idBudgetDetail) {
        this.idBudgetDetail = idBudgetDetail;
    }

    public int getIdBudget() {
        return idBudget;
    }

    public void setIdBudget(int idBudget) {
        this.idBudget = idBudget;
    }

    public int getIdTreatment() {
        return idTreatment;
    }

    public void setIdTreatment(int idTreatment) {
        this.idTreatment = idTreatment;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    @Override
    public String toString() {
        return "BudgetDetail{id=" + idBudgetDetail + ", budget=" + idBudget
                + ", treatment=" + idTreatment + ", qty=" + itemQuantity
                + ", subtotal=" + subtotal + "}";
    }
}
