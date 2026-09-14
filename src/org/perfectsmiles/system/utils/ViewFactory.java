package org.perfectsmiles.system.utils;

import java.awt.BorderLayout;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import org.perfectsmiles.system.Main;
import org.perfectsmiles.system.utils.SceneManager;

public class ViewFactory {

    private final String PATH_VIEWS = "/org/perfectsmiles/system/view/";

    public Scene loadFileFXML(String nameFXML, int width, int height) {
        String pathOfFile = PATH_VIEWS + nameFXML;
        try {
            FXMLLoader loaderFXML = new FXMLLoader();
            URL urlFile = Main.class.getResource(pathOfFile);
            loaderFXML.setBuilderFactory(new JavaFXBuilderFactory());
            loaderFXML.setLocation(urlFile);

            return new Scene(loaderFXML.load(), width, height);

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void loadScene(String nameFXML) {
        Scene scene = null;
        try {
            switch (nameFXML) {
                case "login" -> {
                    
                }
                case "register" -> {
                    
                }
                default ->
                    System.out.println("Hello");
            }
            SceneManager.getInstanciaSceneManager().changeScene(scene);

        } catch (NullPointerException objectNull) {
            System.out.println("error load scene: " + objectNull.getMessage());
        }
    }


}