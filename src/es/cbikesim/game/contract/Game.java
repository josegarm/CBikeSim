package es.cbikesim.game.contract;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public interface Game {

    interface View {
        void start();
        GridPane getBikePane();
        GridPane getClientPane();
        Pane getUtilityPane();
        Pane getTitlePaneBike();
        Pane getTitlePaneClient();
    }

    interface Presenter {
        void createScenario(int difficult);
        void setView(Game.View view);
    }

}
