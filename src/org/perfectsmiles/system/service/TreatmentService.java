package org.perfectsmiles.system.service;

import org.perfectsmiles.system.model.Treatment;
import org.perfectsmiles.system.repository.DAO.TreatmentDAO;
import org.perfectsmiles.system.repository.ITreatmentDAO;
import org.perfectsmiles.system.utils.Validations;

import java.math.BigDecimal;
import java.util.List;

public class TreatmentService {

    private final ITreatmentDAO treatmentDAO;

    public TreatmentService() {
        this.treatmentDAO = new TreatmentDAO();
    }

    public TreatmentService(ITreatmentDAO treatmentDAO) {
        this.treatmentDAO = treatmentDAO;
    }

    public boolean createTreatment(Treatment treatment) throws Exception {
        String error;

        if ((error = Validations.getRequiredFieldError(treatment.getTreatmentName(), "Nombre del tratamiento")) != null) {
            throw new IllegalArgumentException(error);
        }
        if ((error = Validations.getRequiredFieldError(treatment.getInternalCode(), "Codigo interno")) != null) {
            throw new IllegalArgumentException(error);
        }

        if (treatment.getIdUser() <= 0) {
            throw new IllegalArgumentException("Debe indicarse el usuario que crea el tratamiento.");
        }

        if (treatment.getStandardCost() == null || treatment.getStandardCost().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El costo estandar debe ser mayor o igual a cero.");
        }

        if (treatmentDAO.existsByInternalCode(treatment.getInternalCode())) {
            throw new IllegalArgumentException("Ya existe un tratamiento con ese codigo interno.");
        }

        treatment.setTreatmentStatus(true);
        return treatmentDAO.create(treatment);
    }

    public List<Treatment> getAllTreatments() throws Exception {
        return treatmentDAO.readAll();
    }

    public List<Treatment> getActiveTreatments() throws Exception {
        return treatmentDAO.readActiveTreatments();
    }

    public List<Treatment> getTreatmentsByUser(int idUser) throws Exception {
        return treatmentDAO.readByUser(idUser);
    }

    public Treatment getTreatmentById(int id) throws Exception {
        return treatmentDAO.readById(id);
    }

    public List<Treatment> searchTreatments(String query) throws Exception {
        if (Validations.isNullOrEmpty(query)) {
            return treatmentDAO.readActiveTreatments();
        }
        return treatmentDAO.searchByNameOrCode(query);
    }

    public boolean updateTreatment(Treatment treatment) throws Exception {
        String error;
        if ((error = Validations.getRequiredFieldError(treatment.getTreatmentName(), "Nombre del tratamiento")) != null) {
            throw new IllegalArgumentException(error);
        }
        if ((error = Validations.getRequiredFieldError(treatment.getInternalCode(), "Codigo interno")) != null) {
            throw new IllegalArgumentException(error);
        }

        if (treatment.getStandardCost() == null || treatment.getStandardCost().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El costo estandar debe ser mayor o igual a cero.");
        }

        return treatmentDAO.update(treatment);
    }

    public boolean deactivateTreatment(int idTreatment) throws Exception {
        return treatmentDAO.delete(idTreatment);
    }
}
