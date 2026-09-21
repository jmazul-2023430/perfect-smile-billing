package org.perfectsmiles.system.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.perfectsmiles.system.model.Budget;
import org.perfectsmiles.system.model.BudgetDetail;
import org.perfectsmiles.system.model.Patient;
import org.perfectsmiles.system.model.Treatment;
import org.perfectsmiles.system.model.User;
import org.perfectsmiles.system.service.BudgetService;
import org.perfectsmiles.system.service.TreatmentService;
import org.perfectsmiles.system.utils.AlertInformation;
import org.perfectsmiles.system.utils.Session;
import org.perfectsmiles.system.utils.ViewFactory;

import java.math.BigDecimal;
import java.util.List;

public class PatientHistoryController {

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
    private Label lblTituloModulo;
    @FXML
    private Label lblPacienteInfo;

    @FXML
    private TableView<Budget> tblHistorial;
    @FXML
    private TableColumn<Budget, Integer> colIdPresupuesto;
    @FXML
    private TableColumn<Budget, String> colFecha;
    @FXML
    private TableColumn<Budget, String> colDescripcion;
    @FXML
    private TableColumn<Budget, BigDecimal> colSubtotal;
    @FXML
    private TableColumn<Budget, BigDecimal> colIva;
    @FXML
    private TableColumn<Budget, BigDecimal> colTotal;
    @FXML
    private TableColumn<Budget, Boolean> colEstado;

    @FXML
    private TableView<BudgetDetail> tblDetalle;
    @FXML
    private TableColumn<BudgetDetail, String> colTratamiento;
    @FXML
    private TableColumn<BudgetDetail, Integer> colCantidad;
    @FXML
    private TableColumn<BudgetDetail, BigDecimal> colPrecioUnitario;
    @FXML
    private TableColumn<BudgetDetail, BigDecimal> colSubtotalLinea;

    @FXML
    private Button btnVolver;
    @FXML
    private Label lblMensajeEstado;

    private final ViewFactory viewFactory = new ViewFactory();
    private final BudgetService budgetService = new BudgetService();
    private final TreatmentService treatmentService = new TreatmentService();

    private Patient patient;

    @FXML
    private void initialize() {
        if (!Session.hasRole(1)) {
            btnUsuarios.setVisible(false);
            btnUsuarios.setManaged(false);
        }
        showUserInfo();

        patient = Session.getPatientForHistory();
        if (patient == null) {
            AlertInformation.showError("No se selecciono ningun paciente.");
            viewFactory.loadScene("patients");
            return;
        }

        lblPacienteInfo.setText("Paciente: " + patient.getFullName() + " (DPI: " + patient.getDpi() + ")");

        setupHistorialColumns();
        setupDetalleColumns();
        loadHistorial();

        tblHistorial.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected != null) {
                loadDetalle(selected.getIdBudget());
            }
        });
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

    private void setupHistorialColumns() {
        colIdPresupuesto.setCellValueFactory(new PropertyValueFactory<>("idBudget"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descriptionBudget"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        colIva.setCellValueFactory(new PropertyValueFactory<>("tax"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("budgetStatus"));

        colFecha.setCellValueFactory(cellData -> {
            java.time.LocalDate d = cellData.getValue().getIssueDate();
            return new javafx.beans.property.SimpleStringProperty(d != null ? d.toString() : "");
        });
    }

    private void setupDetalleColumns() {
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("itemQuantity"));
        colPrecioUnitario.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colSubtotalLinea.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        colTratamiento.setCellValueFactory(cellData -> {
            Treatment t = treatmentService.getTreatmentByIdSafe(cellData.getValue().getIdTreatment());
            return new javafx.beans.property.SimpleStringProperty(t != null ? t.getTreatmentName() : "N/A");
        });
    }

    private void loadHistorial() {
        try {
            List<Budget> budgets = budgetService.getBudgetsByPatient(patient.getIdPatient());
            ObservableList<Budget> list = FXCollections.observableArrayList(budgets);
            tblHistorial.setItems(list);
        } catch (Exception e) {
            AlertInformation.showError("Error al cargar historial: " + e.getMessage());
        }
    }

    private void loadDetalle(int idBudget) {
        try {
            List<BudgetDetail> details = budgetService.getDetailsByBudget(idBudget);
            ObservableList<BudgetDetail> list = FXCollections.observableArrayList(details);
            tblDetalle.setItems(list);
        } catch (Exception e) {
            AlertInformation.showError("Error al cargar detalle: " + e.getMessage());
        }
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
    private void handleBack() {
        Session.clearPatientForHistory();
        viewFactory.loadScene("patients");
    }
}
