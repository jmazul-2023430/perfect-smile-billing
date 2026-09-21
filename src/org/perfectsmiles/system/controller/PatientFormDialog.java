package org.perfectsmiles.system.controller;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.perfectsmiles.system.model.Patient;

public class PatientFormDialog extends Dialog<Patient> {

    private final TextField txtNombre = new TextField();
    private final TextField txtApellido = new TextField();
    private final TextField txtDpi = new TextField();
    private final TextField txtTelefono = new TextField();
    private final TextField txtEmail = new TextField();
    private final TextField txtDireccion = new TextField();

    private final Patient patient;

    public PatientFormDialog(Patient patient) {
        this.patient = patient;

        setTitle(patient == null ? "Nuevo Paciente" : "Editar Paciente");
        setHeaderText(patient == null ? "Ingrese los datos del nuevo paciente" : "Modifique los datos del paciente");

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().addAll(btnGuardar, btnCancelar);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        grid.add(new Label("Nombre:"), 0, 0);
        grid.add(txtNombre, 1, 0);
        grid.add(new Label("Apellido:"), 0, 1);
        grid.add(txtApellido, 1, 1);
        grid.add(new Label("DPI:"), 0, 2);
        grid.add(txtDpi, 1, 2);
        grid.add(new Label("Telefono:"), 0, 3);
        grid.add(txtTelefono, 1, 3);
        grid.add(new Label("Email:"), 0, 4);
        grid.add(txtEmail, 1, 4);
        grid.add(new Label("Direccion:"), 0, 5);
        grid.add(txtDireccion, 1, 5);

        getDialogPane().setContent(grid);

        if (patient != null) {
            txtNombre.setText(patient.getFirstName());
            txtApellido.setText(patient.getLastName());
            txtDpi.setText(patient.getDpi());
            txtTelefono.setText(patient.getPhone());
            txtEmail.setText(patient.getEmail());
            txtDireccion.setText(patient.getAddress());
        }

        setResultConverter(button -> {
            if (button == btnGuardar) {
                return buildPatient();
            }
            return null;
        });
    }

    private Patient buildPatient() {
        Patient p = new Patient();
        if (patient != null) {
            p.setIdPatient(patient.getIdPatient());
        }
        p.setFirstName(txtNombre.getText());
        p.setLastName(txtApellido.getText());
        p.setDpi(txtDpi.getText());
        p.setPhone(txtTelefono.getText());
        p.setEmail(txtEmail.getText());
        p.setAddress(txtDireccion.getText());
        p.setPatientStatus(true);
        return p;
    }
}
