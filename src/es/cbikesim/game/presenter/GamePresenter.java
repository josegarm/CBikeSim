package es.cbikesim.game.presenter;

import es.cbikesim.app.CBikeSimState;
import es.cbikesim.game.command.CreateStations;
import es.cbikesim.game.command.GenerateEasyStationBikes;
import es.cbikesim.game.command.GenerateNormalStationBikes;
import es.cbikesim.game.command.GenerateVehicles;
import es.cbikesim.game.contract.Game;
import es.cbikesim.game.gameMenu.GameMenuView;
import es.cbikesim.game.model.*;
import es.cbikesim.game.usecase.client.ClientDepositBikeUseCase;
import es.cbikesim.game.usecase.client.ClientPickUpBikeUseCase;
import es.cbikesim.game.usecase.vehicle.VehicleArrivesStationUseCase;
import es.cbikesim.game.usecase.vehicle.VehicleDepositBikeUseCase;
import es.cbikesim.game.usecase.vehicle.VehicleLeavesStationUseCase;
import es.cbikesim.game.usecase.vehicle.VehiclePickUpBikesUseCase;
import es.cbikesim.game.util.ClientGenerator;
import es.cbikesim.game.util.factories.PathAnimationFactory;
import es.cbikesim.game.view.*;
import es.cbikesim.lib.exception.UseCaseException;
import es.cbikesim.lib.pattern.Command;
import es.cbikesim.lib.pattern.Invoker;
import es.cbikesim.lib.util.Point;
import es.cbikesim.lib.util.Timer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class GamePresenter implements Game.Presenter {

    public static final int EASY = 0, NORMAL = 1, HARD = 2, CUSTOM = 3;
    public static final String FEW_BIKES = "FEW", NORMAL_BIKES = "NORMAL", MANY_BIKES = "MANY";

    private Game.View view;
    private Scenario scenario;
    private Station selectedStation;
    private Vehicle selectedVehicle;
    private VehicleView selectedVehicleView;

    private MediaPlayer mp, mpSelect;
    private Timer timer;

    private ClientGenerator clientGenerator;

    private int difficulty;

    public GamePresenter() {
        scenario = new Scenario();
    }

    @Override
    public void createScenario(int difficulty, int time, String numBikes, int carCapacity) {
        this.difficulty = difficulty;
        prepareTimer(time);

        Invoker invoker = new Invoker();
        Command createStations = new CreateStations(scenario);
        Command generateVehicles, generateBikes;

        switch (difficulty) {
            case EASY:
                generateVehicles = new GenerateVehicles(scenario, 9);
                generateBikes = new GenerateEasyStationBikes(scenario);
                break;
            case NORMAL:
                generateVehicles = new GenerateVehicles(scenario, 6);
                generateBikes = new GenerateNormalStationBikes(scenario);
                break;
            case HARD:
                generateVehicles = new GenerateVehicles(scenario, 3);
                generateBikes = new GenerateNormalStationBikes(scenario);
                break;
            case CUSTOM:
                generateVehicles = new GenerateVehicles(scenario, carCapacity);
                generateBikes = new GenerateNormalStationBikes(scenario);
                break;
            default:
                generateVehicles = new GenerateVehicles(scenario, 6);
                generateBikes = new GenerateNormalStationBikes(scenario);
        }

        invoker.addCommand(createStations);
        invoker.addCommand(generateVehicles);
        invoker.addCommand(generateBikes);
        try {
            invoker.invoke();
        } catch (UseCaseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void load() {
        prepareMusic();
        paintMap();
        timerStart();
        addPause();
        startClientGenerator();
        if (CBikeSimState.getInstance().getAudio()) mp.play();
    }

    @Override
    public void playSelect() {
        if (CBikeSimState.getInstance().getAudio()) {
            mpSelect.stop();
            mpSelect.play();
        }
    }

    @Override
    public void showDataFromStation(String id) {
        Station station = getSelectedStationWith(id);
        selectedStation = station;
        paintStationPanel(station);
    }

    @Override
    public void showDataFromVehicle(String id) {
        Vehicle vehicle = getVehicleWith(id);
        selectedVehicle = vehicle;
        selectedStation = vehicle.getAt();
        paintVehiclePanel(vehicle);
    }

    @Override
    public void notifyNewClient(Client client) {
        ImageView newClientNotification = new ClientNotificationView(client.getFrom().getPosition());

        Platform.runLater(() -> {
            view.getMapPane().getChildren().add(newClientNotification);
            new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                view.getMapPane().getChildren().remove(newClientNotification);
            })).play();
        });
    }

    @Override
    public void clientPicksUpBike(String idClient, String idBike) {
        Client client = getClientWith(idClient);
        Bike bike = getBikeWith(idBike);

        Invoker invoker = new Invoker();
        Command clientPicksUpBike = new ClientPickUpBikeUseCase(client, bike, scenario);

        invoker.addCommand(clientPicksUpBike);
        try {
            invoker.invoke();
            paintStationPanel(selectedStation);
            paintClientInTransit(client);
        } catch (UseCaseException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void clientDepositsBike(String idClient, ClientView clientView) {
        Client client = getClientWith(idClient);

        Invoker invoker = new Invoker();
        Command clientDepositBike = new ClientDepositBikeUseCase(client, scenario);

        invoker.addCommand(clientDepositBike);
        try {
            invoker.invoke();
            if (client.getBike() == null) {
                Platform.runLater(() -> view.getMapPane().getChildren().remove(clientView));
                clientView.stop();
            } else {
                Platform.runLater(() ->
                        clientView.setCenterX(client.getTo().getPosition().getX() - 25.0)
                );
            }
        } catch (UseCaseException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void vehiclePicksUpBike(String idBike) {
        Vehicle vehicle = selectedVehicle;

        Invoker invoker = new Invoker();
        Command vehiclePicksUpBike = new VehiclePickUpBikesUseCase(vehicle, scenario);

        invoker.addCommand(vehiclePicksUpBike);
        try {
            invoker.invoke();
            paintVehiclePanel(vehicle);
        } catch (UseCaseException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void vehicleDepositsBike(String idBike) {
        Vehicle vehicle = selectedVehicle;

        Invoker invoker = new Invoker();
        Command vehiclePicksUpBike = new VehicleDepositBikeUseCase(vehicle, scenario);

        invoker.addCommand(vehiclePicksUpBike);
        try {
            invoker.invoke();
            paintVehiclePanel(vehicle);
        } catch (UseCaseException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void moveVehicleToAnotherStation(String idVehicle, String idStationTarget) {
        Vehicle vehicle = getVehicleWith(idVehicle);
        Station to = getStationWith(idStationTarget);

        Invoker invoker = new Invoker();
        Command vehicleLeavesStation = new VehicleLeavesStationUseCase(vehicle, to, scenario);

        invoker.addCommand(vehicleLeavesStation);
        try {
            invoker.invoke();
            showDataFromVehicle(idVehicle);
            paintVehicleInTransit(vehicle);
        } catch (UseCaseException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void vehicleArriveStation(String idVehicle, VehicleView vehicleView) {
        Vehicle vehicle = getVehicleWith(idVehicle);

        Invoker invoker = new Invoker();
        Command vehicleArrivesStation = new VehicleArrivesStationUseCase(vehicle, scenario);

        invoker.addCommand(vehicleArrivesStation);
        try {
            invoker.invoke();
            vehicleView.stop();

        } catch (UseCaseException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void setVehicleView(VehicleView vehicleView) {
        selectedVehicleView = vehicleView;
    }


    @Override
    public void setView(Game.View view) {
        this.view = view;
    }


    private void paintMap() {
        for (Station station : scenario.getStationList()) {
            paintStation(station);
            for (Vehicle vehicle : station.getVehicleList()) {
                paintVehicle(vehicle);
            }
        }
    }

    private void paintStation(Station station) {
        StationView stationView = new StationView(station.getPosition(), station.getId(), this);
        view.getMapPane().getChildren().add(stationView);
    }

    private void paintVehicle(Vehicle vehicle) {
        vehicle.setFrom(vehicle.getAt());
        Point point = new Point(vehicle.getAt().getPosition().getX() + 40.0, vehicle.getAt().getPosition().getY());
        VehicleView vehicleView = new VehicleView(point, vehicle.getId(), this);
        view.getMapPane().getChildren().add(vehicleView);
    }

    private void paintClientInTransit(Client client) {
        ClientView clientView = new ClientView(client.getFrom().getPosition(), client.getId(), this);
        view.getMapPane().getChildren().add(clientView);
        int seconds = calculateDurationClient(client);

        clientView.setAnimation(PathAnimationFactory.pathAnimationFactory(client.getFrom().getPosition(), client.getTo().getPosition()));
        clientView.setDuration(seconds);

        new Thread(clientView).start();
    }

    private void paintVehicleInTransit(Vehicle vehicle) {
        VehicleView vehicleView = selectedVehicleView;
        vehicleView.toFront();

        Point secondPoint = vehicle.getFrom().getPosition();
        Point startPoint = new Point(secondPoint.getX() + 40.00, secondPoint.getY());
        Point thirdPoint = vehicle.getTo().getPosition();
        Point endPoint = new Point(thirdPoint.getX() + 40.00, thirdPoint.getY());

        vehicleView.setAnimation(PathAnimationFactory.pathAnimationFactory(startPoint, secondPoint, thirdPoint, endPoint));
        vehicleView.setDuration(calculateDurationVehicle(vehicle));

        new Thread(selectedVehicleView).start();
    }

    private int calculateDurationClient(Client client) {
        int duration;
        double distance =
                Math.abs(client.getTo().getPosition().getX() - client.getFrom().getPosition().getX()) +
                        Math.abs(client.getTo().getPosition().getY() - client.getFrom().getPosition().getY());
        duration = (int) distance / 35;
        return duration;
    }

    private int calculateDurationVehicle(Vehicle vehicle) {
        int duration;
        double distance =
                Math.abs(vehicle.getTo().getPosition().getX() - vehicle.getFrom().getPosition().getX()) +
                        Math.abs(vehicle.getTo().getPosition().getY() - vehicle.getFrom().getPosition().getY());
        duration = (int) distance / 45;
        return duration;
    }

    private void paintStationPanel(Station station) {
        paintStationBikePanel(station);
        paintStationClientPanel(station);
    }

    private void paintVehiclePanel(Vehicle vehicle) {
        paintStationBikePanel(vehicle.getAt());
        paintVehicleBikePanel(vehicle);
    }

    private void paintStationBikePanel(Station station) {
        view.getTopTitle().setText("");
        view.getTopPane().getChildren().clear();

        if (station == null) return;

        view.getTopTitle().setText(station.getId() + " - Bikes Status");

        int count = 0;
        int rows = view.getTopPane().getRowConstraints().size();
        int columns = view.getTopPane().getColumnConstraints().size();
        int numBikes = station.getAvailableBikeList().size();

        Image bikeEmptyImage = new Image(getClass().getResource("/img/bike_empty.png").toExternalForm());
        Image bikeImage = new Image(getClass().getResource("/img/bike.png").toExternalForm());

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns && count < station.getMaxCapacity(); column++) {
                if (count < numBikes) {
                    view.getTopPane().add(new BikeStallView(bikeImage, station.getAvailableBikeList().get(count).getId(), this, BikeStallView.STATION, true, false), column, row);
                } else {
                    view.getTopPane().add(new BikeStallView(bikeEmptyImage, this, BikeStallView.STATION, false, true), column, row);
                }
                count++;
            }
        }
    }

    private void paintStationClientPanel(Station station) {
        view.getBottomTitle().setText("");
        view.getBottomPane().getChildren().clear();

        if (station == null) return;

        view.getBottomTitle().setText("Clients Waiting");

        int count = 0;
        int rows = view.getBottomPane().getRowConstraints().size();
        int columns = view.getBottomPane().getColumnConstraints().size();
        int numClients = station.getClientWaitingToPickUpList().size();

        Image clientEmptyImage = new Image(getClass().getResource("/img/client_empty.png").toExternalForm());
        Image clientImage = new Image(getClass().getResource("/img/client.png").toExternalForm());

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns && count < 6; column++) {
                if (count < numClients) {
                    view.getBottomPane().add(new ClientInStationView(clientImage, station.getClientWaitingToPickUpList().get(count).getId(), this), column, row);
                } else {
                    view.getBottomPane().add(new ClientInStationView(clientEmptyImage), column, row);
                }
                count++;
            }
        }
    }

    private void paintVehicleBikePanel(Vehicle vehicle) {
        view.getBottomTitle().setText("");
        view.getBottomPane().getChildren().clear();

        if (vehicle == null) return;

        view.getBottomTitle().setText("Vehicle Bikes");

        int count = 0;
        int rows = view.getTopPane().getRowConstraints().size();
        int columns = view.getTopPane().getColumnConstraints().size();
        int numBikes = vehicle.getBikeList().size();

        Image bikeEmptyImage = new Image(getClass().getResource("/img/bike_empty.png").toExternalForm());
        Image bikeImage = new Image(getClass().getResource("/img/bike.png").toExternalForm());

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns && count < vehicle.getMaxCapacity(); column++) {
                if (count < numBikes) {
                    view.getBottomPane().add(new BikeStallView(bikeImage, vehicle.getBikeList().get(count).getId(), this, BikeStallView.VEHICLE, true, false), column, row);
                } else {
                    view.getBottomPane().add(new BikeStallView(bikeEmptyImage, this, BikeStallView.VEHICLE, false, true), column, row);
                }
                count++;
            }
        }
    }

    private Station getSelectedStationWith(String id) {
        for (Station station : scenario.getStationList()) {
            if (station.getId().equals(id)) return station;
        }
        return null;
    }

    private Vehicle getVehicleWith(String id) {
        for (Vehicle vehicle : scenario.getVehiclesInTransit()) {
            if (id.equals(vehicle.getId())) return vehicle;
        }
        for (Station station : scenario.getStationList()) {
            for (Vehicle vehicle : station.getVehicleList()) {
                if (id.equals(vehicle.getId())) return vehicle;
            }
        }
        return null;
    }

    public Client getClientWith(String id) {
        for (Client client : scenario.getClientsInTransit()) {
            if (client.getId().equals(id)) return client;
        }
        for (Station station : scenario.getStationList()) {
            for (Client client : station.getClientWaitingToPickUpList()) {
                if (client.getId().equals(id)) return client;
            }
            for (Client client : station.getClientWaitingToDepositList()) {
                if (client.getId().equals(id)) return client;
            }
        }
        // throw error
        return null;
    }

    public Bike getBikeWith(String id) {
        for (Bike bike : selectedStation.getAvailableBikeList()) {
            if (bike.getId().equals(id)) return bike;
        }
        // throw error
        return null;
    }

    public Station getStationWith(String id) {
        for (Station station : scenario.getStationList()) {
            if (station.getId().equals(id)) return station;
        }
        //throw error
        return null;
    }


    private void prepareMusic() {
        String pathSelect = getClass().getResource("/music/select.wav").toString();
        String path = getClass().getResource("/music/soundtrack_game.mp3").toString();

        Media mediaSelect = new Media(pathSelect);
        Media media = new Media(path);

        this.mpSelect = new MediaPlayer(mediaSelect);
        this.mp = new MediaPlayer(media);

        this.mpSelect.setVolume(1.0);
    }

    private void prepareTimer(int seconds) {
        timer = new Timer(seconds);
    }

    private void startClientGenerator() {
        clientGenerator = new ClientGenerator(scenario, this, 5000);
        clientGenerator.start();
        CBikeSimState.getInstance().getPrimaryStage().setOnCloseRequest(event -> clientGenerator.cancel());
    }

    //DURATION in SECONDS
    private void timerStart() {
        //countdown
        timer.startTimer();
        Label timerText = timer.getTimerLabel();
        timerText.setTranslateX(180);
        view.getUtilityPane().getChildren().addAll(
                timer.getTimerTitle(),
                timerText
        );

    }

    private void addPause() {
        ImageView play_pause = new ImageView(new Image(getClass().getResource("/img/pause-play-button.png").toExternalForm()));
        play_pause.setLayoutX(10);
        play_pause.setLayoutY(10);
        view.getMapPane().getChildren().add(play_pause);
        play_pause.setOnMouseClicked(e -> {
            try {
                new GameMenuView(this).start(new Stage(StageStyle.UNDECORATED));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
    }


}
