package org.perfectsmiles.system.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.perfectsmiles.system.model.Role;
import org.perfectsmiles.system.model.User;
import org.perfectsmiles.system.service.RoleService;
import org.perfectsmiles.system.service.UserService;
import org.perfectsmiles.system.utils.AlertInformation;
import org.perfectsmiles.system.utils.Session;
import org.perfectsmiles.system.utils.ViewFactory;

import java.util.List;

public class UsersViewController {

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
    private TextField txtBuscarUsuario;
    @FXML
    private Button btnNuevoUsuario;
    @FXML
    private TableView<User> tblUsuarios;
    @FXML
    private TableColumn<User, Integer> colIdUsuario;
    @FXML
    private TableColumn<User, String> colUsuario;
    @FXML
    private TableColumn<User, String> colNombreCompleto;
    @FXML
    private TableColumn<User, String> colRol;
    @FXML
    private TableColumn<User, String> colEmail;
    @FXML
    private TableColumn<User, String> colTelefono;
    @FXML
    private TableColumn<User, String> colUltimoAcceso;
    @FXML
    private TableColumn<User, Boolean> colEstado;
    @FXML
    private Button btnEditarUsuario;
    @FXML
    private Button btnCambiarRol;
    @FXML
    private Button btnDesactivarUsuario;
    @FXML
    private Label lblMensajeEstado;

    private final ViewFactory viewFactory = new ViewFactory();
    private final UserService userService = new UserService();
    private final RoleService roleService = new RoleService();

    private ObservableList<User> masterData = FXCollections.observableArrayList();
    private FilteredList<User> filteredData;

    @FXML
    private void initialize() {
        if (!Session.hasRole(1)) {
            AlertInformation.showError("No tiene permiso para acceder a Usuarios.");
            viewFactory.loadScene("tariff");
            return;
        }
        showUserInfo();
        applyPermissions();
        setupTableColumns();
        loadUsers();
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
        colIdUsuario.setCellValueFactory(new PropertyValueFactory<>("idUser"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colNombreCompleto.setCellValueFactory(new PropertyValueFactory<>("completeName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("active"));

        colRol.setCellValueFactory(cellData -> {
            int idRole = cellData.getValue().getIdRole();
            return new javafx.beans.property.SimpleStringProperty(getRoleName(idRole));
        });

        colUltimoAcceso.setCellValueFactory(cellData -> {
            java.time.LocalDateTime d = cellData.getValue().getLastAccess();
            return new javafx.beans.property.SimpleStringProperty(d != null ? d.toString().replace("T", " ") : "");
        });
    }

    private void loadUsers() {
        try {
            List<User> users = userService.listAll();
            masterData.setAll(users);
            filteredData = new FilteredList<>(masterData, p -> true);
            tblUsuarios.setItems(filteredData);
        } catch (Exception e) {
            AlertInformation.showError("Error al cargar usuarios: " + e.getMessage());
        }
    }

    private void setupSearch() {
        if (txtBuscarUsuario == null) {
            return;
        }
        txtBuscarUsuario.textProperty().addListener((obs, oldVal, newVal) -> {
            if (filteredData == null) {
                return;
            }
            filteredData.setPredicate(u -> {
                if (newVal == null || newVal.isEmpty()) {
                    return true;
                }
                String lower = newVal.toLowerCase();
                return (u.getUserName() != null && u.getUserName().toLowerCase().contains(lower))
                        || (u.getCompleteName() != null && u.getCompleteName().toLowerCase().contains(lower))
                        || (u.getEmail() != null && u.getEmail().toLowerCase().contains(lower));
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
    private void handleNewUser() {
        viewFactory.loadScene("register");
    }

    @FXML
    private void handleEditUser() {
        User selected = tblUsuarios.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertInformation.showWarning("Seleccione un usuario para editar.");
            return;
        }
        try {
            List<Role> roles = roleService.getActiveRoles();
            UserFormDialog dialog = new UserFormDialog(selected, roles);
            User result = dialog.showAndWait().orElse(null);
            if (result != null) {
                userService.updateUser(result);
                AlertInformation.showInfo("Usuario actualizado.");
                loadUsers();
            }
        } catch (Exception e) {
            AlertInformation.showError(e.getMessage());
        }
    }

    @FXML
    private void handleChangeRole() {
        handleEditUser();
    }

    @FXML
    private void handleDeactivateUser() {
        User selected = tblUsuarios.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertInformation.showWarning("Seleccione un usuario para desactivar.");
            return;
        }
        if (selected.getIdUser() == Session.getCurrentUser().getIdUser()) {
            AlertInformation.showWarning("No puede desactivar su propio usuario.");
            return;
        }
        boolean confirm = AlertInformation.showConfirmation(
                "¿Desactivar al usuario '" + selected.getCompleteName() + "'?"
        );
        if (!confirm) {
            return;
        }
        try {
            userService.deactivateUser(selected.getIdUser());
            AlertInformation.showInfo("Usuario desactivado.");
            loadUsers();
        } catch (Exception e) {
            AlertInformation.showError(e.getMessage());
        }
    }
}
