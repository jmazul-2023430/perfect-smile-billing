package org.perfectsmiles.system.repository;

import org.perfectsmiles.system.model.Budget;
import java.util.List;

public interface IBudgetDAO extends IDAO<Budget> {

    List<Budget> readActiveBudgets() throws Exception;
    
    List<Budget> readByPatient(int idPatient) throws Exception;

    List<Budget> readByUser(int idUser) throws Exception;

    boolean changeStatus(int idBudget, boolean newStatus) throws Exception;
}
