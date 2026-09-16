package org.perfectsmiles.system;

import javafx.application.Application;
import javafx.stage.Stage;
import org.perfectsmiles.system.utils.SceneManager;
import org.perfectsmiles.system.utils.ViewFactory;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stageRoot) {
        SceneManager.getInstanceSceneManager().setStagePrincipal(stageRoot);
        ViewFactory viewFacto = new ViewFactory();
//        viewFacto.viewLogin();
        viewFacto.viewRegister();
    }

}
