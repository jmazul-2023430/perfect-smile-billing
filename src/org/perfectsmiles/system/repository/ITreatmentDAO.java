package org.perfectsmiles.system.repository;

import org.perfectsmiles.system.model.Treatment;
import java.util.List;

public interface ITreatmentDAO extends IDAO<Treatment> {

    List<Treatment> readActiveTreatments() throws Exception;

    List<Treatment> readByUser(int idUser) throws Exception;

    List<Treatment> searchByNameOrCode(String query) throws Exception;

    boolean existsByInternalCode(String internalCode) throws Exception;
}
