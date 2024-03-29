package es.cbikesim.app;


import es.cbikesim.mainMenu.contract.MainMenu;
import es.cbikesim.mainMenu.presenter.MainMenuPresenter;
import es.cbikesim.mainMenu.view.MainMenuView;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CBikeSim extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.setResizable(false);

        CBikeSimState.getInstance().setPrimaryStage(primaryStage);

        MainMenu.Presenter mainMenuPresenter = new MainMenuPresenter();
        MainMenu.View mainMenuView = new MainMenuView(mainMenuPresenter);
        mainMenuPresenter.initMenu();
    }
}
