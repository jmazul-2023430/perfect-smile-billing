package org.perfectsmiles.system.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.perfectsmiles.system.model.Role;
import org.perfectsmiles.system.model.User;
import org.perfectsmiles.system.service.RoleService;
import org.perfectsmiles.system.service.UserService;
import org.perfectsmiles.system.utils.AlertInformation;
import org.perfectsmiles.system.utils.ViewFactory;

import java.util.List;

public class RegisterController {

    @FXML
    private TextField txtNombreCompleto;
    @FXML
    private TextField txtUsuario;
    @FXML
    private TextField txtCorreo;
    @FXML
    private TextField txtTelefono;
    @FXML
    private ComboBox<Role> cmbRol;
    @FXML
    private PasswordField pwdPassword;
    @FXML
    private PasswordField pwdConfirmarPassword;
    @FXML
    private Label lblMensajeError;
    @FXML
    private javafx.scene.control.Button btnCancelar;
    @FXML
    private javafx.scene.control.Button btnCrearCuenta;

    private final UserService userService = new UserService();
    private final RoleService roleService = new RoleService();
    private final ViewFactory viewFactory = new ViewFactory();

    @FXML
    private void initialize() {
        if (lblMensajeError != null) {
            lblMensajeError.setText("");
        }
        loadRoles();
    }

    private void loadRoles() {
        try {
            List<Role> roles = roleService.getActiveRoles();
            for (Role role : roles) {
                if (!"owner".equalsIgnoreCase(role.getRoleName())) {
                    cmbRol.getItems().add(role);
                }
            }
        } catch (Exception e) {
            showError("Error al cargar los roles: " + e.getMessage());
        }
    }

    @FXML
    private void handleCreateAccount() {
        if (lblMensajeError != null) {
            lblMensajeError.setText("");
        }

        try {
            User user = new User();
            user.setCompleteName(txtNombreCompleto.getText());
            user.setUserName(txtUsuario.getText());
            user.setEmail(txtCorreo.getText());
            user.setPhone(txtTelefono.getText());

            Role selectedRole = cmbRol.getValue();
            if (selectedRole == null) {
                showError("Debe seleccionar un rol.");
                return;
            }
            user.setIdRole(selectedRole.getIdRole());

            userService.registerUser(user, pwdPassword.getText(), pwdConfirmarPassword.getText());

            AlertInformation.showInfo("Usuario creado exitosamente.");
            viewFactory.loadScene("users");

        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        } catch (Exception e) {
            showError("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel() {
        viewFactory.loadScene("users");
    }

    private void showError(String mensaje) {
        if (lblMensajeError != null) {
            lblMensajeError.setText(mensaje);
        }
        AlertInformation.showError(mensaje);
    }
}
