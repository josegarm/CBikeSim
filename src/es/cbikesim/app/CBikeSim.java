package es.cbikesim.app;


import es.cbikesim.mainMenu.contract.MainMenu;
import es.cbikesim.mainMenu.presenter.MainMenuPresenter;
import es.cbikesim.mainMenu.view.MainMenuView;

import javafx.application.Application;
import javafx.stage.Stage;

public class CBikeSim extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        MainMenu.Presenter mainMenuPresenter = new MainMenuPresenter();
        MainMenu.View mainMenuView = new MainMenuView(primaryStage, mainMenuPresenter);
        mainMenuView.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
