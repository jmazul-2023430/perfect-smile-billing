package org.perfectsmiles.system.service;

import org.perfectsmiles.system.model.Patient;
import org.perfectsmiles.system.repository.DAO.PatientDAO;
import org.perfectsmiles.system.repository.IPatientDAO;
import org.perfectsmiles.system.utils.Validations;

import java.util.List;

public class PatientService {

    private final IPatientDAO patientDAO;

    public PatientService() {
        this.patientDAO = new PatientDAO();
    }

    public PatientService(IPatientDAO patientDAO) {
        this.patientDAO = patientDAO;
    }

    public boolean createPatient(Patient patient) throws Exception {
        String error;

        if ((error = Validations.getRequiredFieldError(patient.getFirstName(), "Nombre")) != null) {
            throw new IllegalArgumentException(error);
        }
        if ((error = Validations.getRequiredFieldError(patient.getLastName(), "Apellido")) != null) {
            throw new IllegalArgumentException(error);
        }
        if ((error = Validations.getRequiredFieldError(patient.getDpi(), "DPI")) != null) {
            throw new IllegalArgumentException(error);
        }

        // DPI único
        if (patientDAO.existsByDpi(patient.getDpi())) {
            throw new IllegalArgumentException("Ya existe un paciente con ese DPI.");
        }

        // Email solo si no está vacío
        if (!Validations.isNullOrEmpty(patient.getEmail())) {
            if ((error = Validations.getEmailError(patient.getEmail())) != null) {
                throw new IllegalArgumentException(error);
            }
        }

        patient.setPatientStatus(true);
        return patientDAO.create(patient);
    }

    public List<Patient> getAllPatients() throws Exception {
        return patientDAO.readAll();
    }

    public List<Patient> getActivePatients() throws Exception {
        return patientDAO.readActivePatients();
    }

    public Patient getPatientById(int id) throws Exception {
        return patientDAO.readById(id);
    }

    public Patient getPatientByDpi(String dpi) throws Exception {
        return patientDAO.readByDpi(dpi);
    }

    public List<Patient> searchPatients(String query) throws Exception {
        if (Validations.isNullOrEmpty(query)) {
            return patientDAO.readActivePatients();
        }
        return patientDAO.searchByName(query);
    }

    public boolean updatePatient(Patient patient) throws Exception {
        String error;
        if ((error = Validations.getRequiredFieldError(patient.getFirstName(), "Nombre")) != null) {
            throw new IllegalArgumentException(error);
        }
        if ((error = Validations.getRequiredFieldError(patient.getLastName(), "Apellido")) != null) {
            throw new IllegalArgumentException(error);
        }

        if (!Validations.isNullOrEmpty(patient.getEmail())) {
            if ((error = Validations.getEmailError(patient.getEmail())) != null) {
                throw new IllegalArgumentException(error);
            }
        }

        return patientDAO.update(patient);
    }

    public boolean deactivatePatient(int idPatient) throws Exception {
        return patientDAO.delete(idPatient);
    }
}
