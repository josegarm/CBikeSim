package es.cbikesim.app;


import es.cbikesim.mainMenu.contract.MainMenu;
import es.cbikesim.mainMenu.presenter.MainMenuPresenter;
import es.cbikesim.mainMenu.view.MainMenuView;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CBikeSim extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.setResizable(false);

        MainMenu.Presenter mainMenuPresenter = new MainMenuPresenter();
        MainMenu.View mainMenuView = new MainMenuView(primaryStage, mainMenuPresenter);
        mainMenuView.start();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
