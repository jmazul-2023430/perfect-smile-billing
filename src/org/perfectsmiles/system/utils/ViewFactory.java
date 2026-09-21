package org.perfectsmiles.system.utils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import org.perfectsmiles.system.Main;

public class ViewFactory {

    private final String PATH_VIEWS = "/org/perfectsmiles/system/view/";

    public Scene loadFileFXML(String nameFXML) {
        String pathOfFile = PATH_VIEWS + nameFXML;
        try {
            FXMLLoader loaderFXML = new FXMLLoader();
            URL urlFile = Main.class.getResource(pathOfFile);
            loaderFXML.setBuilderFactory(new JavaFXBuilderFactory());
            loaderFXML.setLocation(urlFile);
            return new Scene(loaderFXML.load());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void loadScene(String nameFXML) {
        Scene scene = null;
        try {
            switch (nameFXML) {
                case "login" -> {
                    SceneManager.getInstanceSceneManager().getStagePrincipal().setTitle("LOGIN DE USUARIOS");
                    SceneManager.getInstanceSceneManager().getStagePrincipal().setResizable(false);
                    scene = loadFileFXML("LoginView.fxml");
                }
                case "register" -> {
                    SceneManager.getInstanceSceneManager().getStagePrincipal().setTitle("REGISTRO DE USUARIOS");
                    SceneManager.getInstanceSceneManager().getStagePrincipal().setResizable(false);
                    scene = loadFileFXML("RegisterView.fxml");
                }
                case "tariff" -> {
                    SceneManager.getInstanceSceneManager().getStagePrincipal().setTitle("TARIFARIO");
                    SceneManager.getInstanceSceneManager().getStagePrincipal().setResizable(false);
                    scene = loadFileFXML("TariffView.fxml");
                }
                case "users" -> {
                    SceneManager.getInstanceSceneManager().getStagePrincipal().setTitle("USUARIOS");
                    SceneManager.getInstanceSceneManager().getStagePrincipal().setResizable(false);
                    scene = loadFileFXML("UsersView.fxml");
                }
                case "patients" -> {
                    SceneManager.getInstanceSceneManager().getStagePrincipal().setTitle("PACIENTES");
                    SceneManager.getInstanceSceneManager().getStagePrincipal().setResizable(false);
                    scene = loadFileFXML("PatientsView.fxml");
                }
                case "budgets" -> {
                    SceneManager.getInstanceSceneManager().getStagePrincipal().setTitle("PRESUPUESTOS");
                    SceneManager.getInstanceSceneManager().getStagePrincipal().setResizable(false);
                    scene = loadFileFXML("BudgetsView.fxml");
                }
                case "budgetForm" -> {
                    SceneManager.getInstanceSceneManager().getStagePrincipal().setTitle("NUEVO PRESUPUESTO");
                    SceneManager.getInstanceSceneManager().getStagePrincipal().setResizable(false);
                    scene = loadFileFXML("BudgetFormView.fxml");
                }
                default ->
                    System.out.println("Vista no reconocida: " + nameFXML);
            }
            SceneManager.getInstanceSceneManager().changeScene(scene);
        } catch (NullPointerException objectNull) {
            System.out.println("Error load scene: " + objectNull.getMessage());
        }
    }

    public void viewLogin() {
        loadScene("login");
    }

    public void viewRegister() {
        loadScene("register");
    }

    public void viewTariff() {
        loadScene("tariff");
    }

    public void viewUsers() {
        loadScene("users");
    }

    public void viewPatients() {
        loadScene("patients");
    }

    public void viewBudgets() {
        loadScene("budgets");
    }

    public void viewBudgetForm() {
        loadScene("budgetForm");
    }
}
