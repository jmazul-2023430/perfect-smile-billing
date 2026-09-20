package org.perfectsmiles.system.repository;

import org.perfectsmiles.system.model.RolePermission;
import java.util.List;

public interface IRolePermissionDAO extends IDAO<RolePermission> {

    List<RolePermission> readByRole(int idRole) throws Exception;

    List<RolePermission> readByPermission(int idPermission) throws Exception;

    boolean exists(int idRole, int idPermission) throws Exception;
}
