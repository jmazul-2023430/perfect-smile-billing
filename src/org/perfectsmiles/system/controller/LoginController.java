package org.perfectsmiles.system.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.perfectsmiles.system.model.Permission;
import org.perfectsmiles.system.model.User;
import org.perfectsmiles.system.service.AuthService;
import org.perfectsmiles.system.service.AuthorizationService;
import org.perfectsmiles.system.utils.AlertInformation;
import org.perfectsmiles.system.utils.Session;
import org.perfectsmiles.system.utils.Validations;
import org.perfectsmiles.system.utils.ViewFactory;

import java.util.List;

public class LoginController {

    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField pwdPassword;
    @FXML
    private Label lblMensajeError;

    private final AuthService authService = new AuthService();
    private final AuthorizationService authorizationService = new AuthorizationService();
    private final ViewFactory viewFactory = new ViewFactory();

    @FXML
    private void initialize() {
        if (lblMensajeError != null) {
            lblMensajeError.setText("");
        }
        if (txtUsuario != null) {
            txtUsuario.requestFocus();
        }
    }

    @FXML
    private void handleLogin() {
        String username = txtUsuario.getText();
        String password = pwdPassword.getText();

        if (lblMensajeError != null) {
            lblMensajeError.setText("");
        }

        if (Validations.isNullOrEmpty(username)) {
            showError("El usuario es obligatorio.");
            return;
        }
        if (Validations.isNullOrEmpty(password)) {
            showError("La contrasena es obligatoria.");
            return;
        }

        try {
            User user = authService.login(username, password);

            if (user == null) {
                showError("Usuario o contrasena incorrectos.");
                return;
            }

            List<Permission> permisos = authorizationService.loadPermissionsForRole(user.getIdRole());
            Session.setCurrentPermissions(permisos);

            AlertInformation.showInfo("Bienvenido, " + user.getCompleteName());

            redirectByRole(user.getIdRole());

        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        } catch (Exception e) {
            showError("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void redirectByRole(int idRole) {
        viewFactory.loadScene("tariff");
    }

    private void showError(String mensaje) {
        if (lblMensajeError != null) {
            lblMensajeError.setText(mensaje);
        }
        AlertInformation.showError(mensaje);
    }
}
