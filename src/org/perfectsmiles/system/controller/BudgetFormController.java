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
import org.perfectsmiles.system.service.BudgetService;
import org.perfectsmiles.system.service.PatientService;
import org.perfectsmiles.system.service.TreatmentService;
import org.perfectsmiles.system.utils.AlertInformation;
import org.perfectsmiles.system.utils.Session;
import org.perfectsmiles.system.utils.Validations;
import org.perfectsmiles.system.utils.ViewFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class BudgetFormController {

    @FXML
    private ComboBox<Patient> cmbPaciente;
    @FXML
    private TextField txtDescripcion;
    @FXML
    private ComboBox<Treatment> cmbTratamiento;
    @FXML
    private TextField txtCantidad;
    @FXML
    private Button btnAgregar;
    @FXML
    private TableView<BudgetDetail> tblDetalles;
    @FXML
    private TableColumn<BudgetDetail, String> colTratamiento;
    @FXML
    private TableColumn<BudgetDetail, Integer> colCantidad;
    @FXML
    private TableColumn<BudgetDetail, BigDecimal> colPrecioUnitario;
    @FXML
    private TableColumn<BudgetDetail, BigDecimal> colSubtotalLinea;
    @FXML
    private TableColumn<BudgetDetail, Void> colQuitar;
    @FXML
    private Label lblSubtotal;
    @FXML
    private Label lblIva;
    @FXML
    private Label lblTotal;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnEmitir;
    @FXML
    private Label lblMensajeEstado;

    private final ViewFactory viewFactory = new ViewFactory();
    private final PatientService patientService = new PatientService();
    private final TreatmentService treatmentService = new TreatmentService();
    private final BudgetService budgetService = new BudgetService();

    private final ObservableList<BudgetDetail> detalles = FXCollections.observableArrayList();
    private Budget editingBudget;

    @FXML
    private void initialize() {
        loadPatients();
        loadTreatments();
        setupTable();
        txtCantidad.setText("1");

        editingBudget = Session.getBudgetToEdit();
        if (editingBudget != null) {
            loadBudgetForEdit();
        }

        updateTotals();
    }

    private void loadPatients() {
        try {
            List<Patient> patients = patientService.getActivePatients();
            cmbPaciente.getItems().setAll(patients);
        } catch (Exception e) {
            AlertInformation.showError("Error al cargar pacientes: " + e.getMessage());
        }
    }

    private void loadTreatments() {
        try {
            List<Treatment> treatments = treatmentService.getActiveTreatments();
            cmbTratamiento.getItems().setAll(treatments);
        } catch (Exception e) {
            AlertInformation.showError("Error al cargar tratamientos: " + e.getMessage());
        }
    }

    private void loadBudgetForEdit() {
        txtDescripcion.setText(editingBudget.getDescriptionBudget());

        try {
            Patient p = patientService.getPatientById(editingBudget.getIdPatient());
            cmbPaciente.setValue(p);
        } catch (Exception ignored) {
        }

        try {
            List<BudgetDetail> oldDetails = budgetService.getDetailsByBudget(editingBudget.getIdBudget());
            detalles.setAll(oldDetails);
        } catch (Exception e) {
            AlertInformation.showError("Error al cargar detalles: " + e.getMessage());
        }
    }

    private void setupTable() {
        colTratamiento.setCellValueFactory(cellData -> {
            Treatment t = treatmentService.getTreatmentByIdSafe(cellData.getValue().getIdTreatment());
            return new javafx.beans.property.SimpleStringProperty(t != null ? t.getTreatmentName() : "N/A");
        });
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("itemQuantity"));
        colPrecioUnitario.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colSubtotalLinea.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        colQuitar.setCellFactory(param -> new TableCell<>() {
            private final Button btnQuitar = new Button("Quitar");

            {
                btnQuitar.setOnAction(e -> {
                    BudgetDetail d = getTableView().getItems().get(getIndex());
                    detalles.remove(d);
                    updateTotals();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnQuitar);
            }
        });

        tblDetalles.setItems(detalles);
    }

    @FXML
    private void handleAddTreatment() {
        Treatment t = cmbTratamiento.getValue();
        if (t == null) {
            AlertInformation.showWarning("Seleccione un tratamiento.");
            return;
        }
        int cantidad;
        try {
            cantidad = Integer.parseInt(txtCantidad.getText());
            if (cantidad <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            AlertInformation.showWarning("La cantidad debe ser un número mayor a cero.");
            return;
        }

        BudgetDetail d = new BudgetDetail();
        d.setIdTreatment(t.getIdTreatment());
        d.setUnitPrice(t.getStandardCost());
        d.setItemQuantity(cantidad);
        d.setSubtotal(t.getStandardCost().multiply(BigDecimal.valueOf(cantidad)).setScale(2, RoundingMode.HALF_UP));

        detalles.add(d);
        updateTotals();
        txtCantidad.setText("1");
    }

    private void updateTotals() {
        BigDecimal[] totals = budgetService.calculateTotals(detalles);
        lblSubtotal.setText("Subtotal: Q " + totals[0]);
        lblIva.setText("IVA (12%): Q " + totals[1]);
        lblTotal.setText("Total: Q " + totals[2]);
    }

    @FXML
    private void handleCancel() {
        Session.clearBudgetToEdit();
        viewFactory.loadScene("budgets");
    }

    @FXML
    private void handleEmit() {
        Patient p = cmbPaciente.getValue();
        if (p == null) {
            AlertInformation.showWarning("Seleccione un paciente.");
            return;
        }
        if (detalles.isEmpty()) {
            AlertInformation.showWarning("Debe agregar al menos un tratamiento.");
            return;
        }

        try {
            Budget b = editingBudget != null ? editingBudget : new Budget();
            b.setIdPatient(p.getIdPatient());
            b.setIdUser(Session.getCurrentUser().getIdUser());
            b.setDescriptionBudget(Validations.isNullOrEmpty(txtDescripcion.getText())
                    ? "Presupuesto sin descripcion"
                    : txtDescripcion.getText());

            if (editingBudget != null) {
                budgetService.updateBudgetWithDetails(b, detalles);
                AlertInformation.showInfo("Presupuesto actualizado exitosamente.");
            } else {
                budgetService.createBudget(b, detalles);
                AlertInformation.showInfo("Presupuesto emitido exitosamente.");
            }

            Session.clearBudgetToEdit();
            viewFactory.loadScene("budgets");
        } catch (Exception e) {
            AlertInformation.showError("Error al guardar el presupuesto: " + e.getMessage());
        }
    }
}
