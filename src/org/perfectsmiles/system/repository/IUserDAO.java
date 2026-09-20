package org.perfectsmiles.system.repository;

import org.perfectsmiles.system.model.User;
import java.util.List;

public interface IUserDAO extends IDAO<User> {

    /**
     * Busca un usuario por su username (para login)
     */
    User readByUsername(String username) throws Exception;

    /**
     * Login: valida usuario + hash directamente en la BD
     */
    User login(String username, String passwordHash) throws Exception;

    /**
     * Cambia SOLO la contraseña
     */
    boolean changePassword(int idUser, String newPasswordHash) throws Exception;

    /**
     * Actualiza el último acceso (tras login exitoso)
     */
    boolean updateLastAccess(int idUser) throws Exception;

    /**
     * Verifica si ya existe un username
     */
    boolean existsByUsername(String username) throws Exception;

    /**
     * Verifica si ya existe un email
     */
    boolean existsByEmail(String email) throws Exception;
}
