package org.perfectsmiles.system.controller;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.perfectsmiles.system.model.Role;
import org.perfectsmiles.system.model.User;

public class UserFormDialog extends Dialog<User> {

    private final TextField txtNombreCompleto = new TextField();
    private final TextField txtUsuario = new TextField();
    private final TextField txtEmail = new TextField();
    private final TextField txtTelefono = new TextField();
    private final ComboBox<Role> cmbRol = new ComboBox<>();

    private final User user;

    public UserFormDialog(User user, java.util.List<Role> roles) {
        this.user = user;

        setTitle(user == null ? "Nuevo Usuario" : "Editar Usuario");
        setHeaderText(user == null ? "Ingrese los datos del nuevo usuario" : "Modifique los datos del usuario");

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().addAll(btnGuardar, btnCancelar);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        cmbRol.setPrefWidth(220);

        // Filtrar "owner" para que no se pueda asignar a otro usuario
        for (Role r : roles) {
            if (!"owner".equalsIgnoreCase(r.getRoleName())) {
                cmbRol.getItems().add(r);
            }
        }

        grid.add(new Label("Nombre Completo:"), 0, 0);
        grid.add(txtNombreCompleto, 1, 0);
        grid.add(new Label("Usuario:"), 0, 1);
        grid.add(txtUsuario, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(txtEmail, 1, 2);
        grid.add(new Label("Telefono:"), 0, 3);
        grid.add(txtTelefono, 1, 3);
        grid.add(new Label("Rol:"), 0, 4);
        grid.add(cmbRol, 1, 4);

        getDialogPane().setContent(grid);

        if (user != null) {
            txtNombreCompleto.setText(user.getCompleteName());
            txtUsuario.setText(user.getUserName());
            txtEmail.setText(user.getEmail());
            txtTelefono.setText(user.getPhone());
            for (Role r : cmbRol.getItems()) {
                if (r.getIdRole() == user.getIdRole()) {
                    cmbRol.setValue(r);
                    break;
                }
            }
        }

        setResultConverter(button -> {
            if (button == btnGuardar) {
                return buildUser();
            }
            return null;
        });
    }

    private User buildUser() {
        User u = new User();
        if (user != null) {
            u.setIdUser(user.getIdUser());
        }
        u.setCompleteName(txtNombreCompleto.getText());
        u.setUserName(txtUsuario.getText());
        u.setEmail(txtEmail.getText());
        u.setPhone(txtTelefono.getText());

        Role selected = cmbRol.getValue();
        if (selected != null) {
            u.setIdRole(selected.getIdRole());
        }

        u.setActive(true);
        return u;
    }
}
