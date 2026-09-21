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

public class TariffViewController {

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
    private TextField txtBuscarTratamiento;
    @FXML
    private Button btnNuevoTratamiento;
    @FXML
    private TableView<?> tblTratamientos;
    @FXML
    private TableColumn<?, ?> colCodigoInterno;
    @FXML
    private TableColumn<?, ?> colNombre;
    @FXML
    private TableColumn<?, ?> colDescripcion;
    @FXML
    private TableColumn<?, ?> colCostoEstandar;
    @FXML
    private TableColumn<?, ?> colEstado;
    @FXML
    private TableColumn<?, ?> colAccion;
    @FXML
    private Button btnEditarTratamiento;
    @FXML
    private Button btnDesactivarTratamiento;
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
        boolean isDentist = Session.hasRole(3);

        btnUsuarios.setVisible(isOwner);
        btnUsuarios.setManaged(isOwner);

        btnNuevoTratamiento.setVisible(!isDentist);
        btnNuevoTratamiento.setManaged(!isDentist);

        btnEditarTratamiento.setVisible(!isDentist);
        btnEditarTratamiento.setManaged(!isDentist);

        btnDesactivarTratamiento.setVisible(!isDentist);
        btnDesactivarTratamiento.setManaged(!isDentist);
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
    private void handleNewTreatment() {
        AlertInformation.showInfo("Funcionalidad pendiente: Nuevo Tratamiento");
    }

    @FXML
    private void handleEditTreatment() {
        AlertInformation.showInfo("Funcionalidad pendiente: Editar Tratamiento");
    }

    @FXML
    private void handleDeactivateTreatment() {
        AlertInformation.showInfo("Funcionalidad pendiente: Desactivar Tratamiento");
    }
}
