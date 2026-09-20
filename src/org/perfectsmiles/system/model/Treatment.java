package org.perfectsmiles.system.model;

import java.math.BigDecimal;

public class Treatment {

    private int idTreatment;
    private int idUser;
    private String internalCode;
    private String treatmentName;
    private BigDecimal standardCost;
    private boolean treatmentStatus;
    private String treatmentDescription;

    public Treatment() {
    }

    public Treatment(int idTreatment, int idUser, String internalCode, String treatmentName,
            BigDecimal standardCost, boolean treatmentStatus, String treatmentDescription) {
        this.idTreatment = idTreatment;
        this.idUser = idUser;
        this.internalCode = internalCode;
        this.treatmentName = treatmentName;
        this.standardCost = standardCost;
        this.treatmentStatus = treatmentStatus;
        this.treatmentDescription = treatmentDescription;
    }

    public int getIdTreatment() {
        return idTreatment;
    }

    public void setIdTreatment(int idTreatment) {
        this.idTreatment = idTreatment;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getInternalCode() {
        return internalCode;
    }

    public void setInternalCode(String internalCode) {
        this.internalCode = internalCode;
    }

    public String getTreatmentName() {
        return treatmentName;
    }

    public void setTreatmentName(String treatmentName) {
        this.treatmentName = treatmentName;
    }

    public BigDecimal getStandardCost() {
        return standardCost;
    }

    public void setStandardCost(BigDecimal standardCost) {
        this.standardCost = standardCost;
    }

    public boolean isTreatmentStatus() {
        return treatmentStatus;
    }

    public void setTreatmentStatus(boolean treatmentStatus) {
        this.treatmentStatus = treatmentStatus;
    }

    public String getTreatmentDescription() {
        return treatmentDescription;
    }

    public void setTreatmentDescription(String treatmentDescription) {
        this.treatmentDescription = treatmentDescription;
    }

    @Override
    public String toString() {
        return "Treatment{id=" + idTreatment + ", code='" + internalCode
                + "', name='" + treatmentName + "', cost=" + standardCost + "}";
    }
}
