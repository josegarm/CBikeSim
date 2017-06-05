package es.cbikesim.app;


import es.cbikesim.mainMenu.view.MainMenu;

import javafx.application.Application;
import javafx.stage.Stage;

public class CBikeSim extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        new MainMenu(primaryStage).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
