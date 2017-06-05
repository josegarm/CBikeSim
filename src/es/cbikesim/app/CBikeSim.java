package es.cbikesim.app;


import es.cbikesim.mainMenu.presenter.MainMenuPresenter;
import es.cbikesim.mainMenu.view.MainMenuView;

import javafx.application.Application;
import javafx.stage.Stage;

public class CBikeSim extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        MainMenuPresenter mmp = new MainMenuPresenter();
        new MainMenuView(primaryStage,mmp).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
