package org.perfectsmiles.system.service;

import org.perfectsmiles.system.model.User;
import org.perfectsmiles.system.repository.DAO.UserDAO;
import org.perfectsmiles.system.repository.IUserDAO;
import org.perfectsmiles.system.utils.PasswordEncryptor;
import org.perfectsmiles.system.utils.Session;
import org.perfectsmiles.system.utils.Validations;

public class AuthService {

    private final IUserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    public AuthService(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User login(String username, String plainPassword) throws Exception {
        if (Validations.isNullOrEmpty(username) || Validations.isNullOrEmpty(plainPassword)) {
            throw new IllegalArgumentException("Usuario y contraseña son obligatorios.");
        }

        String hash = PasswordEncryptor.encrypt(plainPassword);
        User user = userDAO.login(username, hash);

        if (user != null) {
            userDAO.updateLastAccess(user.getIdUser());
            Session.setCurrentUser(user);
        }
        return user;
    }

    public void logout() {
        Session.clear();
    }
}
