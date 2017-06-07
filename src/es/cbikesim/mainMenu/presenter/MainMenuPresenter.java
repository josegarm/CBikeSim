package es.cbikesim.mainMenu.presenter;

import es.cbikesim.game.contract.Game;
import es.cbikesim.game.presenter.GamePresenter;
import es.cbikesim.game.view.GameView;
import es.cbikesim.mainMenu.contract.MainMenu;
import javafx.stage.Stage;

public class MainMenuPresenter implements MainMenu.Presenter{

    private MainMenu.View view;


    @Override
    public void initGame(Stage primaryStage) {
        Game.Presenter gamePresenter = new GamePresenter();

        gamePresenter.createScenario(0);

        Game.View gameView = new GameView(primaryStage, gamePresenter);
        gameView.start();
    }

    @Override
    public void setView(MainMenu.View view) {
        this.view = view;
    }
}
