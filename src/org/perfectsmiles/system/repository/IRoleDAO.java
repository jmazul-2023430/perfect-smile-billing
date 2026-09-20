package org.perfectsmiles.system.repository;

import org.perfectsmiles.system.model.Role;
import java.util.List;

public interface IRoleDAO extends IDAO<Role> {

    Role readByName(String roleName) throws Exception;

    List<Role> readActiveRoles() throws Exception;
}
