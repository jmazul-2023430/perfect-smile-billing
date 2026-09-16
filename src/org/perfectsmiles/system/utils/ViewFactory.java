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
                case "dashboard" -> {

                }
                default ->
                    System.out.println("Hello");
            }
            SceneManager.getInstanceSceneManager().changeScene(scene);

        } catch (NullPointerException objectNull) {
            System.out.println("error load scene: " + objectNull.getMessage());
        }
    }

    public void viewLogin(){
        loadScene("login");
    }
    public void viewRegister(){
        loadScene("register");
    }

}