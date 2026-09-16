package org.perfectsmiles.system.utils;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {

    private static SceneManager instanceSceneManager;
    private Stage stagePrincipal;

    private SceneManager() {

    }

    public static SceneManager getInstanceSceneManager() {
        if (instanceSceneManager == null) {
            instanceSceneManager = new SceneManager();
        }
        return instanceSceneManager;
    }
    
    public void changeScene(Scene scene) {
        try {
            stagePrincipal.setScene(scene);
            stagePrincipal.sizeToScene();
            stagePrincipal.show();

        } catch (NullPointerException objectNull) {

        }

    }

    public Stage getStagePrincipal() {
        return stagePrincipal;
    }

    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }

}
