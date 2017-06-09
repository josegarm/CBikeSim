package es.cbikesim.game.contract;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public interface Game {

    interface View {
        void start();
        GridPane getBikePane();
        GridPane getClientPane();
        Pane getMapPane();
        Pane getUtilityPane();
        Text getTopTitle();
        Pane getTitlePaneClient();
    }

    interface Presenter {
        void load();
        void playSelect();
        void createScenario(int difficulty, int numBikes, int carCapacity);
        void showDataFromStation(String id);
        void setView(Game.View view);
    }

}
