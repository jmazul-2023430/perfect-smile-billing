package org.perfectsmiles.system;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;
import org.perfectsmiles.system.utils.SceneManager;
import org.perfectsmiles.system.utils.ViewFactory;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stageRoot) {
        SceneManager.getInstanciaSceneManager().setStagePrincipal(stageRoot);
        ViewFactory viewFacto = new ViewFactory();

    }

}
