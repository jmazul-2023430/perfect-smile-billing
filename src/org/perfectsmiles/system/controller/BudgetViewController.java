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

public class BudgetViewController {

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
    private TextField txtBuscarPresupuesto;
    @FXML
    private Button btnNuevoPresupuesto;
    @FXML
    private TableView<?> tblPresupuestos;
    @FXML
    private TableColumn<?, ?> colIdPresupuesto;
    @FXML
    private TableColumn<?, ?> colPaciente;
    @FXML
    private TableColumn<?, ?> colFechaEmision;
    @FXML
    private TableColumn<?, ?> colSubtotal;
    @FXML
    private TableColumn<?, ?> colIva;
    @FXML
    private TableColumn<?, ?> colTotal;
    @FXML
    private TableColumn<?, ?> colEstado;
    @FXML
    private Button btnEditarPresupuesto;
    @FXML
    private Button btnAprobarPresupuesto;
    @FXML
    private Button btnDesactivarPresupuesto;
    @FXML
    private Label lblMensajeEstado;

    private final ViewFactory viewFactory = new ViewFactory();

    @FXML
    private void initialize() {
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
    private void handleNewBudget() {
        AlertInformation.showInfo("Funcionalidad pendiente: Nuevo Presupuesto");
    }

    @FXML
    private void handleEditBudget() {
        AlertInformation.showInfo("Funcionalidad pendiente: Editar Presupuesto");
    }

    @FXML
    private void handleApproveBudget() {
        AlertInformation.showInfo("Funcionalidad pendiente: Aprobar Presupuesto");
    }

    @FXML
    private void handleDeactivateBudget() {
        AlertInformation.showInfo("Funcionalidad pendiente: Desactivar Presupuesto");
    }
}
