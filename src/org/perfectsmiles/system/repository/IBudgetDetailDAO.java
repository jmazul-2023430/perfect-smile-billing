package org.perfectsmiles.system.repository;

import org.perfectsmiles.system.model.BudgetDetail;
import java.util.List;

public interface IBudgetDetailDAO extends IDAO<BudgetDetail> {

    List<BudgetDetail> readByBudget(int idBudget) throws Exception;
}
