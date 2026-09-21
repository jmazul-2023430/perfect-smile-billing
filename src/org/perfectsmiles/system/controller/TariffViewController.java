package org.perfectsmiles.system.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.perfectsmiles.system.model.Treatment;
import org.perfectsmiles.system.model.User;
import org.perfectsmiles.system.service.TreatmentService;
import org.perfectsmiles.system.utils.AlertInformation;
import org.perfectsmiles.system.utils.Session;
import org.perfectsmiles.system.utils.ViewFactory;
import java.math.BigDecimal;
import java.util.List;

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
    private TableView<Treatment> tblTratamientos;
    @FXML
    private TableColumn<Treatment, String> colCodigoInterno;
    @FXML
    private TableColumn<Treatment, String> colNombre;
    @FXML
    private TableColumn<Treatment, String> colDescripcion;
    @FXML
    private TableColumn<Treatment, BigDecimal> colCostoEstandar;
    @FXML
    private TableColumn<Treatment, Boolean> colEstado;
    @FXML
    private TableColumn<Treatment, Void> colAccion;
    @FXML
    private Button btnEditarTratamiento;
    @FXML
    private Button btnDesactivarTratamiento;
    @FXML
    private Label lblMensajeEstado;

    private final ViewFactory viewFactory = new ViewFactory();
    private final TreatmentService treatmentService = new TreatmentService();

    private ObservableList<Treatment> masterData = FXCollections.observableArrayList();
    private FilteredList<Treatment> filteredData;

    @FXML
    private void initialize() {
        showUserInfo();
        applyPermissions();
        setupTableColumns();
        loadTreatments();
        setupSearch();
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

    private void setupTableColumns() {
        colCodigoInterno.setCellValueFactory(new PropertyValueFactory<>("internalCode"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("treatmentName"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("treatmentDescription"));
        colCostoEstandar.setCellValueFactory(new PropertyValueFactory<>("standardCost"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("treatmentStatus"));
    }

    private void loadTreatments() {
        try {
            List<Treatment> treatments = treatmentService.getAllTreatments();
            masterData.setAll(treatments);
            filteredData = new FilteredList<>(masterData, p -> true);
            tblTratamientos.setItems(filteredData);
        } catch (Exception e) {
            AlertInformation.showError("Error al cargar tratamientos: " + e.getMessage());
        }
    }

    private void setupSearch() {
        if (txtBuscarTratamiento == null) {
            return;
        }
        txtBuscarTratamiento.textProperty().addListener((obs, oldVal, newVal) -> {
            if (filteredData == null) {
                return;
            }
            filteredData.setPredicate(t -> {
                if (newVal == null || newVal.isEmpty()) {
                    return true;
                }
                String lower = newVal.toLowerCase();
                return (t.getTreatmentName() != null && t.getTreatmentName().toLowerCase().contains(lower))
                        || (t.getInternalCode() != null && t.getInternalCode().toLowerCase().contains(lower));
            });
        });
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
        TreatmentFormDialog dialog = new TreatmentFormDialog(null);
        Treatment result = dialog.showAndWait().orElse(null);
        if (result != null) {
            try {
                result.setIdUser(Session.getCurrentUser().getIdUser());
                treatmentService.createTreatment(result);
                AlertInformation.showInfo("Tratamiento creado exitosamente.");
                loadTreatments();
            } catch (Exception e) {
                AlertInformation.showError(e.getMessage());
            }
        }
    }

    @FXML
    private void handleEditTreatment() {
        Treatment selected = tblTratamientos.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertInformation.showWarning("Seleccione un tratamiento para editar.");
            return;
        }
        TreatmentFormDialog dialog = new TreatmentFormDialog(selected);
        Treatment result = dialog.showAndWait().orElse(null);
        if (result != null) {
            try {
                treatmentService.updateTreatment(result);
                AlertInformation.showInfo("Tratamiento actualizado.");
                loadTreatments();
            } catch (Exception e) {
                AlertInformation.showError(e.getMessage());
            }
        }
    }

    @FXML
    private void handleDeactivateTreatment() {
        Treatment selected = tblTratamientos.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertInformation.showWarning("Seleccione un tratamiento para desactivar.");
            return;
        }
        boolean confirm = AlertInformation.showConfirmation(
                "¿Desactivar el tratamiento '" + selected.getTreatmentName() + "'?"
        );
        if (!confirm) {
            return;
        }
        try {
            treatmentService.deactivateTreatment(selected.getIdTreatment());
            AlertInformation.showInfo("Tratamiento desactivado.");
            loadTreatments();
        } catch (Exception e) {
            AlertInformation.showError(e.getMessage());
        }
    }
}
