package org.perfectsmiles.system.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.perfectsmiles.system.model.User;
import org.perfectsmiles.system.utils.AlertInformation;
import org.perfectsmiles.system.utils.Session;
import org.perfectsmiles.system.utils.ViewFactory;

public class UsersViewController {

    @FXML
    private Label lblUsuarioActual;
    @FXML
    private Label lblRolActual;
    @FXML
    private Button btnTarifario;
    @FXML
    private Button btnPresupuestos;
    @FXML
    private Button btnPacientes;
    @FXML
    private Button btnUsuarios;
    @FXML
    private Button btnCerrarSesion;
    @FXML
    private TextField txtBuscarUsuario;
    @FXML
    private Button btnNuevoUsuario;
    @FXML
    private TableView<?> tblUsuarios;
    @FXML
    private TableColumn<?, ?> colIdUsuario;
    @FXML
    private TableColumn<?, ?> colUsuario;
    @FXML
    private TableColumn<?, ?> colNombreCompleto;
    @FXML
    private TableColumn<?, ?> colRol;
    @FXML
    private TableColumn<?, ?> colEmail;
    @FXML
    private TableColumn<?, ?> colTelefono;
    @FXML
    private TableColumn<?, ?> colUltimoAcceso;
    @FXML
    private TableColumn<?, ?> colEstado;
    @FXML
    private Button btnEditarUsuario;
    @FXML
    private Button btnCambiarRol;
    @FXML
    private Button btnDesactivarUsuario;
    @FXML
    private Label lblMensajeEstado;

    private final ViewFactory viewFactory = new ViewFactory();

    @FXML
    private void initialize() {
        if (!Session.hasRole(1)) {
            AlertInformation.showError("No tiene permiso para acceder a Usuarios.");
            viewFactory.loadScene("tariff");
            return;
        }
        showUserInfo();
        applyPermissions();
    }

    private void showUserInfo() {
        User user = Session.getCurrentUser();
        if (user != null) {
            lblUsuarioActual.setText("Usuario: " + user.getCompleteName());
            lblRolActual.setText("Rol: " + getRoleName(user.getIdRole()));
        }
    }

    private String getRoleName(int idRole) {
        return switch (idRole) {
            case 1 ->
                "Owner";
            case 2 ->
                "Administrator";
            case 3 ->
                "Dentist";
            default ->
                "Desconocido";
        };
    }

    private void applyPermissions() {
        boolean isOwner = Session.hasRole(1);
        btnUsuarios.setVisible(isOwner);
        btnUsuarios.setManaged(isOwner);
    }

    @FXML
    private void goToTariff() {
        viewFactory.loadScene("tariff");
    }

    @FXML
    private void goToBudgets() {
        viewFactory.loadScene("budgets");
    }

    @FXML
    private void goToPatients() {
        viewFactory.loadScene("patients");
    }

    @FXML
    private void goToUsers() {
        viewFactory.loadScene("users");
    }

    @FXML
    private void handleLogout() {
        Session.clear();
        viewFactory.loadScene("login");
    }

    @FXML
    private void handleNewUser() {
        viewFactory.loadScene("register");
    }

    @FXML
    private void handleEditUser() {
        AlertInformation.showInfo("Funcionalidad pendiente: Editar Usuario");
    }

    @FXML
    private void handleChangeRole() {
        AlertInformation.showInfo("Funcionalidad pendiente: Cambiar Rol");
    }

    @FXML
    private void handleDeactivateUser() {
        AlertInformation.showInfo("Funcionalidad pendiente: Desactivar Usuario");
    }
}
