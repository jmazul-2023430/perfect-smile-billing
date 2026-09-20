package org.perfectsmiles.system.repository;

import org.perfectsmiles.system.model.Patient;
import java.util.List;

public interface IPatientDAO extends IDAO<Patient> {

    Patient readByDpi(String dpi) throws Exception;

    List<Patient> readActivePatients() throws Exception;

    List<Patient> searchByName(String query) throws Exception;

    boolean existsByDpi(String dpi) throws Exception;
}
