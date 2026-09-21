package org.perfectsmiles.system.controller;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.perfectsmiles.system.model.Treatment;

import java.math.BigDecimal;

public class TreatmentFormDialog extends Dialog<Treatment> {

    private final TextField txtCodigo = new TextField();
    private final TextField txtNombre = new TextField();
    private final TextArea txtDescripcion = new TextArea();
    private final TextField txtCosto = new TextField();
    private final CheckBox chkActivo = new CheckBox("Activo");

    private final Treatment treatment;

    public TreatmentFormDialog(Treatment treatment) {
        this.treatment = treatment;

        setTitle(treatment == null ? "Nuevo Tratamiento" : "Editar Tratamiento");
        setHeaderText(treatment == null ? "Ingrese los datos del nuevo tratamiento" : "Modifique los datos del tratamiento");

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().addAll(btnGuardar, btnCancelar);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        txtDescripcion.setPrefRowCount(3);
        txtDescripcion.setWrapText(true);

        grid.add(new Label("Codigo:"), 0, 0);
        grid.add(txtCodigo, 1, 0);
        grid.add(new Label("Nombre:"), 0, 1);
        grid.add(txtNombre, 1, 1);
        grid.add(new Label("Descripcion:"), 0, 2);
        grid.add(txtDescripcion, 1, 2);
        grid.add(new Label("Costo Estandar:"), 0, 3);
        grid.add(txtCosto, 1, 3);
        grid.add(chkActivo, 1, 4);

        getDialogPane().setContent(grid);

        if (treatment != null) {
            txtCodigo.setText(treatment.getInternalCode());
            txtNombre.setText(treatment.getTreatmentName());
            txtDescripcion.setText(treatment.getTreatmentDescription());
            txtCosto.setText(treatment.getStandardCost() != null ? treatment.getStandardCost().toString() : "");
            chkActivo.setSelected(treatment.isTreatmentStatus());
        } else {
            chkActivo.setSelected(true);
        }

        setResultConverter(button -> {
            if (button == btnGuardar) {
                return buildTreatment();
            }
            return null;
        });
    }

    private Treatment buildTreatment() {
        Treatment t = new Treatment();
        if (treatment != null) {
            t.setIdTreatment(treatment.getIdTreatment());
            t.setIdUser(treatment.getIdUser());
        }
        t.setInternalCode(txtCodigo.getText());
        t.setTreatmentName(txtNombre.getText());
        t.setTreatmentDescription(txtDescripcion.getText());
        try {
            t.setStandardCost(new BigDecimal(txtCosto.getText()));
        } catch (NumberFormatException e) {
            t.setStandardCost(BigDecimal.ZERO);
        }
        t.setTreatmentStatus(chkActivo.isSelected());
        return t;
    }
}