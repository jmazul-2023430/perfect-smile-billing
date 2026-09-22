package org.perfectsmiles.system.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertInformation {

    public static void showInfo(String message) {
        showAlert(AlertType.INFORMATION, "Información", message);
    }

    public static void showError(String message) {
        showAlert(AlertType.ERROR, "Error", message);
    }

    public static void showWarning(String message) {
        showAlert(AlertType.WARNING, "Advertencia", message);
    }

    public static boolean showConfirmation(String message) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait()
                .filter(response -> response.getButtonData().isDefaultButton())
                .isPresent();
    }

    private static void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
