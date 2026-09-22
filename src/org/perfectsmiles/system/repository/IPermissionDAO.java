package org.perfectsmiles.system.repository;

import org.perfectsmiles.system.model.Permission;
import java.util.List;

public interface IPermissionDAO extends IDAO<Permission> {


    Permission readByName(String permissionName) throws Exception;

    List<Permission> readByModule(String moduleName) throws Exception;

    List<Permission> readActivePermissions() throws Exception;
}
