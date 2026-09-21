package org.perfectsmiles.system.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.perfectsmiles.system.model.Patient;
import org.perfectsmiles.system.model.User;
import org.perfectsmiles.system.service.PatientService;
import org.perfectsmiles.system.utils.AlertInformation;
import org.perfectsmiles.system.utils.Session;
import org.perfectsmiles.system.utils.ViewFactory;

import java.util.List;

public class PatientViewController {

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
    private TextField txtBuscarPaciente;
    @FXML
    private TableView<Patient> tblPacientes;
    @FXML
    private TableColumn<Patient, String> colDpi;
    @FXML
    private TableColumn<Patient, String> colNombre;
    @FXML
    private TableColumn<Patient, String> colApellido;
    @FXML
    private TableColumn<Patient, String> colTelefono;
    @FXML
    private TableColumn<Patient, String> colEmail;
    @FXML
    private TableColumn<Patient, Boolean> colEstado;
    @FXML
    private Button btnNuevoPaciente;
    @FXML
    private Button btnEditarPaciente;
    @FXML
    private Button btnHistorialClinico;
    @FXML
    private Button btnDesactivarPaciente;
    @FXML
    private Label lblMensajeEstado;

    private final ViewFactory viewFactory = new ViewFactory();
    private final PatientService patientService = new PatientService();

    private ObservableList<Patient> masterData = FXCollections.observableArrayList();
    private FilteredList<Patient> filteredData;

    @FXML
    private void initialize() {
        showUserInfo();
        applyPermissions();
        setupTableColumns();
        loadPatients();
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
        btnUsuarios.setVisible(isOwner);
        btnUsuarios.setManaged(isOwner);
    }

    private void setupTableColumns() {
        colDpi.setCellValueFactory(new PropertyValueFactory<>("dpi"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("patientStatus"));
    }

    private void loadPatients() {
        try {
            List<Patient> patients = patientService.getAllPatients();
            masterData.setAll(patients);
            filteredData = new FilteredList<>(masterData, p -> true);
            tblPacientes.setItems(filteredData);
        } catch (Exception e) {
            AlertInformation.showError("Error al cargar pacientes: " + e.getMessage());
        }
    }

    private void setupSearch() {
        if (txtBuscarPaciente == null) {
            return;
        }
        txtBuscarPaciente.textProperty().addListener((obs, oldVal, newVal) -> {
            if (filteredData == null) {
                return;
            }
            filteredData.setPredicate(p -> {
                if (newVal == null || newVal.isEmpty()) {
                    return true;
                }
                String lower = newVal.toLowerCase();
                return (p.getFirstName() != null && p.getFirstName().toLowerCase().contains(lower))
                        || (p.getLastName() != null && p.getLastName().toLowerCase().contains(lower))
                        || (p.getDpi() != null && p.getDpi().toLowerCase().contains(lower));
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
    private void handleNewPatient() {
        PatientFormDialog dialog = new PatientFormDialog(null);
        Patient result = dialog.showAndWait().orElse(null);
        if (result != null) {
            try {
                patientService.createPatient(result);
                AlertInformation.showInfo("Paciente creado exitosamente.");
                loadPatients();
            } catch (Exception e) {
                AlertInformation.showError(e.getMessage());
            }
        }
    }

    @FXML
    private void handleEditPatient() {
        Patient selected = tblPacientes.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertInformation.showWarning("Seleccione un paciente para editar.");
            return;
        }
        PatientFormDialog dialog = new PatientFormDialog(selected);
        Patient result = dialog.showAndWait().orElse(null);
        if (result != null) {
            try {
                patientService.updatePatient(result);
                AlertInformation.showInfo("Paciente actualizado.");
                loadPatients();
            } catch (Exception e) {
                AlertInformation.showError(e.getMessage());
            }
        }
    }

    @FXML
    private void handleClinicalHistory() {
        Patient selected = tblPacientes.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertInformation.showWarning("Seleccione un paciente para ver su historial.");
            return;
        }
        Session.setPatientForHistory(selected);
        viewFactory.loadScene("patientHistory");
    }

    @FXML
    private void handleDeactivatePatient() {
        Patient selected = tblPacientes.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertInformation.showWarning("Seleccione un paciente para desactivar.");
            return;
        }
        boolean confirm = AlertInformation.showConfirmation(
                "¿Desactivar al paciente '" + selected.getFullName() + "'?"
        );
        if (!confirm) {
            return;
        }
        try {
            patientService.deactivatePatient(selected.getIdPatient());
            AlertInformation.showInfo("Paciente desactivado.");
            loadPatients();
        } catch (Exception e) {
            AlertInformation.showError(e.getMessage());
        }
    }
}
