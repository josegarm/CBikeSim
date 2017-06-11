package es.cbikesim.game.contract;

import es.cbikesim.game.model.Client;
import es.cbikesim.game.model.Station;
import es.cbikesim.game.view.ClientView;
import es.cbikesim.game.view.VehicleView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public interface Game {

    interface View {
        void start();
        GridPane getTopPane();
        GridPane getBottomPane();
        Pane getMapPane();
        Pane getUtilityPane();
        Text getTopTitle();
        Text getBottomTitle();
        javafx.scene.image.ImageView getClientHasArrivedIcon();
    }

    interface Presenter {
        void load();
        void playSelect();
        void createScenario(int difficulty, int time, String numBikes, int carCapacity);
        void showDataFromStation(String id);
        void showDataFromVehicle(String id);
        void notifyNewClient(Client client);
        void clientPicksUpBike(String idClient, String idBike);
        void clientDepositsBike(String idClient, ClientView clientView);
        void vehiclePicksUpBike(String idBike);
        void vehicleDepositsBike(String idBike);
        void vehicleToAnotherStation(Station to);
        void setVehicleView(VehicleView vehicleView);
        void setView(Game.View view);

    }

}
