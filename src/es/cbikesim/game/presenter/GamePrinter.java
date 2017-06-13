package es.cbikesim.game.presenter;

import es.cbikesim.game.model.Bike;
import es.cbikesim.game.model.Client;
import es.cbikesim.game.model.Station;
import es.cbikesim.game.model.Vehicle;
import es.cbikesim.game.util.factories.PathAnimationFactory;
import es.cbikesim.game.view.*;
import es.cbikesim.lib.util.Point;
import javafx.scene.image.Image;

public class GamePrinter {

    private final GamePresenter gamePresenter;

    GamePrinter(GamePresenter gamePresenter) {
        this.gamePresenter = gamePresenter;
    }

    void paintMap() {
        for (Station station : gamePresenter.getScenario().getStationList()) {
            paintStation(station);
            for (Vehicle vehicle : station.getVehicleList()) {
                paintVehicle(vehicle);
            }
        }
    }

    void paintStation(Station station) {
        StationView stationView = new StationView(station.getPosition(), station.getId(), gamePresenter);
        gamePresenter.getView().getMapPane().getChildren().add(stationView);
    }

    void paintVehicle(Vehicle vehicle) {
        vehicle.setFrom(vehicle.getAt());
        Point point = new Point(vehicle.getAt().getPosition().getX() + 40.0, vehicle.getAt().getPosition().getY());
        VehicleView vehicleView = new VehicleView(point, vehicle.getId(), gamePresenter);
        gamePresenter.getView().getMapPane().getChildren().add(vehicleView);
    }

    void paintClientInTransit(Client client) {
        gamePresenter.getScore().changeScore(30);
        ClientView clientView = new ClientView(client.getFrom().getPosition(), client.getId(), gamePresenter);
        gamePresenter.getView().getMapPane().getChildren().add(clientView);
        int seconds = calculateDuration(client);

        clientView.setAnimation(PathAnimationFactory.pathAnimationFactory(client.getFrom().getPosition(), client.getTo().getPosition()));
        clientView.setDuration(seconds);

        Thread thread = new Thread(clientView);
        thread.setDaemon(true);
        gamePresenter.getThreads().add(thread);
        thread.start();
    }

    void paintVehicleInTransit(Vehicle vehicle) {
        gamePresenter.getScore().changeScore(5);
        VehicleView vehicleView = gamePresenter.getSelectedVehicleView();
        vehicleView.toFront();

        Point secondPoint = vehicle.getFrom().getPosition();
        Point startPoint = new Point(secondPoint.getX() + 40.00, secondPoint.getY());
        Point thirdPoint = vehicle.getTo().getPosition();
        Point endPoint = new Point(thirdPoint.getX() + 40.00, thirdPoint.getY());

        vehicleView.setAnimation(PathAnimationFactory.pathAnimationFactory(startPoint, secondPoint, thirdPoint, endPoint));
        vehicleView.setDuration(calculateDuration(vehicle));

        Thread thread = new Thread(gamePresenter.getSelectedVehicleView());
        thread.setDaemon(true);
        gamePresenter.getThreads().add(thread);
        thread.start();
    }

    void paintStationPanel(Station station) {
        paintStationBikePanel(station);
        paintStationClientPanel(station);
    }

    void paintVehiclePanel(Vehicle vehicle) {
        paintStationBikePanel(vehicle.getAt());
        paintVehicleBikePanel(vehicle);
    }

    private int calculateDuration(Client client) {
        int duration;
        int velocity = 25;
        double distance =
                Math.abs(client.getTo().getPosition().getX() - client.getFrom().getPosition().getX()) +
                        Math.abs(client.getTo().getPosition().getY() - client.getFrom().getPosition().getY());

        if (client.getBike().getBikeType() == Bike.ELECTRIC) velocity = 45;
        else if (client.getBike().getBikeType() == Bike.NORMAL) velocity = 30;

        duration = (int) distance / velocity;
        return duration;
    }

    private int calculateDuration(Vehicle vehicle) {
        int duration;
        int velocity = 50;
        double distance =
                Math.abs(vehicle.getTo().getPosition().getX() - vehicle.getFrom().getPosition().getX()) +
                        Math.abs(vehicle.getTo().getPosition().getY() - vehicle.getFrom().getPosition().getY());
        duration = (int) distance / velocity;
        return duration;
    }

    private void paintStationBikePanel(Station station) {
        gamePresenter.getView().getTopTitle().setText("");
        gamePresenter.getView().getTopPane().getChildren().clear();

        if (station == null) return;

        gamePresenter.getView().getTopTitle().setText(station.getId() + " - Bikes Status");

        int count = 0;
        int rows = gamePresenter.getView().getTopPane().getRowConstraints().size();
        int columns = gamePresenter.getView().getTopPane().getColumnConstraints().size();
        int numBikes = station.getAvailableBikeList().size();

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns && count < station.getMaxCapacity(); column++) {

                if (count < numBikes) {
                    Image type = (station.getAvailableBikeList().get(count).getBikeType() == Bike.NORMAL) ? BikeStallView.NORMAL : BikeStallView.ELECTRIC;
                    String id = station.getAvailableBikeList().get(count).getId();

                    gamePresenter.getView().getTopPane().add(new BikeStallView(type, id, gamePresenter, BikeStallView.STATION, true, false), column, row);
                } else {
                    gamePresenter.getView().getTopPane().add(new BikeStallView(BikeStallView.EMPTY, gamePresenter, BikeStallView.STATION, false, true), column, row);
                }

                count++;
            }
        }
    }

    private void paintStationClientPanel(Station station) {
        gamePresenter.getView().getBottomTitle().setText("");
        gamePresenter.getView().getBottomPane().getChildren().clear();

        if (station == null) return;

        gamePresenter.getView().getBottomTitle().setText("Clients Waiting");

        int count = 0;
        int rows = gamePresenter.getView().getBottomPane().getRowConstraints().size();
        int columns = gamePresenter.getView().getBottomPane().getColumnConstraints().size();
        int numClients = station.getClientWaitingToPickUpList().size();

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns && count < 9; column++) {

                if (count < numClients) {
                    String idClient = station.getClientWaitingToPickUpList().get(count).getId();
                    gamePresenter.getView().getBottomPane().add(new ClientInStationView(ClientInStationView.CLIENT, idClient, gamePresenter), column, row);
                } else {
                    gamePresenter.getView().getBottomPane().add(new ClientInStationView(ClientInStationView.EMPTY), column, row);
                }

                count++;
            }
        }
    }

    private void paintVehicleBikePanel(Vehicle vehicle) {
        gamePresenter.getView().getBottomTitle().setText("");
        gamePresenter.getView().getBottomPane().getChildren().clear();

        if (vehicle == null) return;

        gamePresenter.getView().getBottomTitle().setText("Vehicle Bikes");

        int count = 0;
        int rows = gamePresenter.getView().getTopPane().getRowConstraints().size();
        int columns = gamePresenter.getView().getTopPane().getColumnConstraints().size();
        int numBikes = vehicle.getBikeList().size();

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns && count < vehicle.getMaxCapacity(); column++) {

                if (count < numBikes) {
                    Image type = (vehicle.getBikeList().get(count).getBikeType() == Bike.NORMAL) ? BikeStallView.NORMAL : BikeStallView.ELECTRIC;
                    String id = vehicle.getBikeList().get(count).getId();

                    gamePresenter.getView().getBottomPane().add(new BikeStallView(type, id, gamePresenter, BikeStallView.VEHICLE, true, false), column, row);
                } else {
                    gamePresenter.getView().getBottomPane().add(new BikeStallView(BikeStallView.EMPTY, gamePresenter, BikeStallView.VEHICLE, false, true), column, row);
                }

                count++;
            }
        }
    }
}