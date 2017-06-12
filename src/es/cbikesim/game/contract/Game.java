package es.cbikesim.game.contract;

import es.cbikesim.game.model.Client;
import es.cbikesim.game.view.ClientView;
import es.cbikesim.game.view.VehicleView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public interface Game {

    interface View {
        void start(Stage primaryStage);

        GridPane getTopPane();

        GridPane getBottomPane();

        Pane getMapPane();

        Pane getUtilityPane();

        Text getTopTitle();

        Text getBottomTitle();
    }

    interface Presenter {
        void initGame();

        void backToMainMenu();

        void playSelect();

        void showDataFromStation(String id);

        void showDataFromVehicle(String id);

        void notifyNewClient(Client client);

        void clientPicksUpBike(String idClient, String idBike);

        void clientDepositsBike(String idClient, ClientView clientView);

        void vehiclePicksUpBike(String idBike);

        void vehicleDepositsBike(String idBike);

        void moveVehicleToAnotherStation(String idVehicle, String idStationTarget);

        void vehicleArriveStation(String idVehicle, VehicleView vehicleView);

        void setVehicleView(VehicleView vehicleView);

        void setView(Game.View view);

        void changeMusic();

    }

}
