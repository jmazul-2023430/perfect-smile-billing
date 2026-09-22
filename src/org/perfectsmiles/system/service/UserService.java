package org.perfectsmiles.system.service;

import org.perfectsmiles.system.model.User;
import org.perfectsmiles.system.repository.DAO.UserDAO;
import org.perfectsmiles.system.repository.IUserDAO;
import org.perfectsmiles.system.utils.PasswordEncryptor;
import org.perfectsmiles.system.utils.Validations;

import java.time.LocalDateTime;
import java.util.List;

public class UserService {

    private final IUserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public UserService(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void registerUser(User user, String plainPassword, String confirmPassword) throws Exception {
        String error;

        if ((error = Validations.getRequiredFieldError(user.getUserName(), "Usuario")) != null) {
            throw new IllegalArgumentException(error);
        }
        if ((error = Validations.getRequiredFieldError(user.getCompleteName(), "Nombre completo")) != null) {
            throw new IllegalArgumentException(error);
        }
        if ((error = Validations.getEmailError(user.getEmail())) != null) {
            throw new IllegalArgumentException(error);
        }
        if ((error = Validations.getPasswordError(plainPassword)) != null) {
            throw new IllegalArgumentException(error);
        }
        if ((error = Validations.getConfirmPasswordError(plainPassword, confirmPassword)) != null) {
            throw new IllegalArgumentException(error);
        }

        if (userDAO.existsByUsername(user.getUserName())) {
            throw new IllegalArgumentException("El nombre de usuario ya esta registrado.");
        }

        if (userDAO.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("El correo electronico ya esta registrado.");
        }

        user.setPasswordHash(PasswordEncryptor.encrypt(plainPassword));
        user.setLastAccess(LocalDateTime.now());
        user.setActive(true);

        if (!userDAO.create(user)) {
            throw new RuntimeException("No se pudo registrar el usuario.");
        }
    }

    public User login(String username, String plainPassword) throws Exception {
        if (Validations.isNullOrEmpty(username) || Validations.isNullOrEmpty(plainPassword)) {
            throw new IllegalArgumentException("Usuario y contrasena son obligatorios.");
        }

        String hash = PasswordEncryptor.encrypt(plainPassword);
        User user = userDAO.login(username, hash);

        if (user != null) {
            userDAO.updateLastAccess(user.getIdUser());
        }
        return user;
    }

    public void changePassword(int idUser, String newPassword, String confirmPassword) throws Exception {
        String error;
        if ((error = Validations.getPasswordError(newPassword)) != null) {
            throw new IllegalArgumentException(error);
        }
        if ((error = Validations.getConfirmPasswordError(newPassword, confirmPassword)) != null) {
            throw new IllegalArgumentException(error);
        }

        String newHash = PasswordEncryptor.encrypt(newPassword);
        if (!userDAO.changePassword(idUser, newHash)) {
            throw new RuntimeException("No se pudo cambiar la contrasena.");
        }
    }

    public List<User> listAll() throws Exception {
        return userDAO.readAll();
    }

    public void updateUser(User user) throws Exception {
        String error;
        if ((error = Validations.getRequiredFieldError(user.getUserName(), "Usuario")) != null) {
            throw new IllegalArgumentException(error);
        }
        if ((error = Validations.getEmailError(user.getEmail())) != null) {
            throw new IllegalArgumentException(error);
        }

        userDAO.update(user);
    }

    public boolean deactivateUser(int idUser) throws Exception {
        return userDAO.delete(idUser);
    }
}
