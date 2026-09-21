package org.perfectsmiles.system.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.perfectsmiles.system.model.Budget;
import org.perfectsmiles.system.model.Patient;
import org.perfectsmiles.system.model.User;
import org.perfectsmiles.system.service.BudgetService;
import org.perfectsmiles.system.service.PatientService;
import org.perfectsmiles.system.utils.AlertInformation;
import org.perfectsmiles.system.utils.Session;
import org.perfectsmiles.system.utils.ViewFactory;

import java.math.BigDecimal;
import java.util.List;

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
    private TableView<Budget> tblPresupuestos;
    @FXML
    private TableColumn<Budget, Integer> colIdPresupuesto;
    @FXML
    private TableColumn<Budget, String> colPaciente;
    @FXML
    private TableColumn<Budget, String> colFechaEmision;
    @FXML
    private TableColumn<Budget, BigDecimal> colSubtotal;
    @FXML
    private TableColumn<Budget, BigDecimal> colIva;
    @FXML
    private TableColumn<Budget, BigDecimal> colTotal;
    @FXML
    private TableColumn<Budget, Boolean> colEstado;
    @FXML
    private Button btnEditarPresupuesto;
    @FXML
    private Button btnAprobarPresupuesto;
    @FXML
    private Button btnDesactivarPresupuesto;
    @FXML
    private Label lblMensajeEstado;

    private final ViewFactory viewFactory = new ViewFactory();
    private final BudgetService budgetService = new BudgetService();
    private final PatientService patientService = new PatientService();

    private ObservableList<Budget> masterData = FXCollections.observableArrayList();
    private FilteredList<Budget> filteredData;

    @FXML
    private void initialize() {
        showUserInfo();
        applyPermissions();
        setupTableColumns();
        loadBudgets();
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
        colIdPresupuesto.setCellValueFactory(new PropertyValueFactory<>("idBudget"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        colIva.setCellValueFactory(new PropertyValueFactory<>("tax"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("budgetStatus"));

        colPaciente.setCellValueFactory(cellData -> {
            try {
                Patient p = patientService.getPatientById(cellData.getValue().getIdPatient());
                return new javafx.beans.property.SimpleStringProperty(
                        p != null ? p.getFullName() : "N/A");
            } catch (Exception e) {
                return new javafx.beans.property.SimpleStringProperty("N/A");
            }
        });

        colFechaEmision.setCellValueFactory(cellData -> {
            java.time.LocalDate d = cellData.getValue().getIssueDate();
            return new javafx.beans.property.SimpleStringProperty(d != null ? d.toString() : "");
        });
    }

    private void loadBudgets() {
        try {
            List<Budget> budgets = budgetService.getAllBudgets();
            masterData.setAll(budgets);
            filteredData = new FilteredList<>(masterData, p -> true);
            tblPresupuestos.setItems(filteredData);
        } catch (Exception e) {
            AlertInformation.showError("Error al cargar presupuestos: " + e.getMessage());
        }
    }

    private void setupSearch() {
        if (txtBuscarPresupuesto == null) {
            return;
        }
        txtBuscarPresupuesto.textProperty().addListener((obs, oldVal, newVal) -> {
            if (filteredData == null) {
                return;
            }
            filteredData.setPredicate(b -> {
                if (newVal == null || newVal.isEmpty()) {
                    return true;
                }
                String lower = newVal.toLowerCase();
                try {
                    Patient p = patientService.getPatientById(b.getIdPatient());
                    String nombre = p != null ? p.getFullName().toLowerCase() : "";
                    return nombre.contains(lower) || String.valueOf(b.getIdBudget()).contains(lower);
                } catch (Exception e) {
                    return false;
                }
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
    private void handleNewBudget() {
        Session.clearBudgetToEdit();
        viewFactory.loadScene("budgetForm");
    }

    @FXML
    private void handleEditBudget() {
        Budget selected = tblPresupuestos.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertInformation.showWarning("Seleccione un presupuesto para editar.");
            return;
        }
        Session.setBudgetToEdit(selected);
        viewFactory.loadScene("budgetForm");
    }

    @FXML
    private void handleApproveBudget() {
        Budget selected = tblPresupuestos.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertInformation.showWarning("Seleccione un presupuesto para aprobar.");
            return;
        }
        try {
            budgetService.approveBudget(selected.getIdBudget());
            AlertInformation.showInfo("Presupuesto aprobado.");
            loadBudgets();
        } catch (Exception e) {
            AlertInformation.showError(e.getMessage());
        }
    }

    @FXML
    private void handleDeactivateBudget() {
        Budget selected = tblPresupuestos.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertInformation.showWarning("Seleccione un presupuesto para desactivar.");
            return;
        }
        boolean confirm = AlertInformation.showConfirmation(
                "¿Desactivar el presupuesto #" + selected.getIdBudget() + "?"
        );
        if (!confirm) {
            return;
        }
        try {
            budgetService.deactivateBudget(selected.getIdBudget());
            AlertInformation.showInfo("Presupuesto desactivado.");
            loadBudgets();
        } catch (Exception e) {
            AlertInformation.showError(e.getMessage());
        }
    }
}
