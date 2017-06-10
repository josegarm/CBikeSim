package es.cbikesim.game.contract;

import es.cbikesim.game.view.ClientView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public interface Game {

    interface View {
        void start();
        GridPane getBikePane();
        GridPane getClientPane();
        Pane getMapPane();
        Pane getUtilityPane();
        Text getTopTitle();
        Text getBottomTitle();
        Pane getBottomPane();
    }

    interface Presenter {
        void load();
        void playSelect();
        void createScenario(int difficulty, int time, String numBikes, int carCapacity);
        void showDataFromStation(String id);
        void clientPicksUpBike(String idClient, String idBike);
        void clientDepositsBike(String idClient, ClientView clientView);
        void setView(Game.View view);
    }

}
