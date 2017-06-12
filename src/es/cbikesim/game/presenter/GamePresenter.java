package es.cbikesim.game.presenter;

import es.cbikesim.app.CBikeSimState;
import es.cbikesim.game.command.*;
import es.cbikesim.game.contract.Game;
import es.cbikesim.game.model.*;
import es.cbikesim.game.usecase.client.ClientDepositBikeUseCase;
import es.cbikesim.game.usecase.client.ClientPickUpBikeUseCase;
import es.cbikesim.game.usecase.vehicle.VehicleArrivesStationUseCase;
import es.cbikesim.game.usecase.vehicle.VehicleDepositBikeUseCase;
import es.cbikesim.game.usecase.vehicle.VehicleLeavesStationUseCase;
import es.cbikesim.game.usecase.vehicle.VehiclePickUpBikesUseCase;
import es.cbikesim.game.util.ClientGenerator;
import es.cbikesim.game.util.ClientGeneratorStrategySelector;
import es.cbikesim.game.util.factories.PathAnimationFactory;
import es.cbikesim.game.view.*;
import es.cbikesim.lib.exception.UseCaseException;
import es.cbikesim.gameMenu.contract.GameMenu;
import es.cbikesim.gameMenu.presenter.GameMenuPresenter;
import es.cbikesim.gameMenu.view.GameMenuView;
import es.cbikesim.lib.pattern.Command;
import es.cbikesim.lib.pattern.Invoker;
import es.cbikesim.lib.util.Point;
import es.cbikesim.lib.util.Score;
import es.cbikesim.lib.util.Timer;
import es.cbikesim.mainMenu.contract.MainMenu;
import es.cbikesim.mainMenu.presenter.MainMenuPresenter;
import es.cbikesim.mainMenu.view.MainMenuView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class GamePresenter implements Game.Presenter {

    public static final int EASY = 0, NORMAL = 1, HARD = 2, CUSTOM = 3;
    public static final String FEW_BIKES = "FEW", NORMAL_BIKES = "NORMAL", MANY_BIKES = "MANY";
    private static final int NOTHING = 0, STATION = 1, VEHICLE = 2;

    private Game.View view;
    private Scenario scenario;

    private Station selectedStation;
    private Vehicle selectedVehicle;

    private VehicleView selectedVehicleView;

    private MediaPlayer mp, mpSelect, mpStart, mpClient;
    private Timer timer;

    private ClientGenerator clientGenerator;
    private ClientGeneratorStrategySelector clientGeneratorStrategySelector;

    private int itemSelectedType = NOTHING;

    private Score score;

    private int difficulty, time, carCapacity;
    private String numBikes;

    public GamePresenter(int difficulty, int time, String numBikes, int carCapacity) {
        this.difficulty = difficulty;
        this.time = time;
        this.numBikes = numBikes;
        this.carCapacity = carCapacity;
    }

    @Override
    public void initGame() {
        scenario = new Scenario();

        prepareTimer();
        prepareClientGenerator();
        prepareMusic();

        createScenario(difficulty,time,numBikes,carCapacity);

        view.start(CBikeSimState.getInstance().getPrimaryStage());

        load();
    }

    @Override
    public void backToMainMenu() {
        stopTimer();
        stopClientGenerator();

        mp.stop();
        MainMenu.Presenter mainMenuPresenter = new MainMenuPresenter();
        MainMenu.View mainMenuView = new MainMenuView(mainMenuPresenter);
        mainMenuPresenter.initMenu();
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
        itemSelectedType = STATION;
    }

    @Override
    public void showDataFromVehicle(String id) {
        Vehicle vehicle = getVehicleWith(id);
        selectedVehicle = vehicle;
        selectedStation = vehicle.getAt();
        paintVehiclePanel(vehicle);
        itemSelectedType = VEHICLE;
    }

    @Override
    public void notifyNewClient(Client client) {
        if (CBikeSimState.getInstance().getAudio()) {
            mpClient.stop();
            mpClient.play();
        }

        ImageView newClientNotification = new ClientNotificationView(client.getFrom().getPosition());

        Platform.runLater(() -> {
            if(client.getFrom() == selectedStation && itemSelectedType == STATION) paintStationPanel(selectedStation);
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
                Platform.runLater(() -> {
                    if(client.getFrom() == selectedStation && itemSelectedType == STATION) paintStationPanel(selectedStation);
                    view.getMapPane().getChildren().remove(clientView);
                });
                clientView.stopRun();
            } else {
                Platform.runLater(() -> {
                    score.changeScore(-1);
                    clientView.setLayoutX(-25 + (-25.0 * client.getTo().getClientWaitingToDepositList().indexOf(client)));
                });
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
            vehicleView.stopRun();

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

    @Override
    public void changeMusic() {
        CBikeSimState.getInstance().turnAudio();
        if(CBikeSimState.getInstance().getAudio()) mp.play();
        else mp.pause();
    }

    private void createScenario(int difficulty, int time, String numBikes, int carCapacity) {
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
                generateBikes = new GenerateCustomStationBikes(scenario, numBikes);
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

    private void load() {
        paintMap();
        addPause();

        startTimer();
        startScore();
        startClientGenerator();
        startClientGeneratorStrategySelector();

        if (CBikeSimState.getInstance().getAudio()) {
            mpStart.play();
            mp.play();
        }
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
        score.changeScore(30);
        ClientView clientView = new ClientView(client.getFrom().getPosition(), client.getId(), this);
        view.getMapPane().getChildren().add(clientView);
        int seconds = calculateDuration(client);

        clientView.setAnimation(PathAnimationFactory.pathAnimationFactory(client.getFrom().getPosition(), client.getTo().getPosition()));
        clientView.setDuration(seconds);

        new Thread(clientView).start();
        CBikeSimState.getInstance().addThread(clientView);
    }

    private void paintVehicleInTransit(Vehicle vehicle) {
        score.changeScore(5);
        VehicleView vehicleView = selectedVehicleView;
        vehicleView.toFront();

        Point secondPoint = vehicle.getFrom().getPosition();
        Point startPoint = new Point(secondPoint.getX() + 40.00, secondPoint.getY());
        Point thirdPoint = vehicle.getTo().getPosition();
        Point endPoint = new Point(thirdPoint.getX() + 40.00, thirdPoint.getY());

        vehicleView.setAnimation(PathAnimationFactory.pathAnimationFactory(startPoint, secondPoint, thirdPoint, endPoint));
        vehicleView.setDuration(calculateDuration(vehicle));

        new Thread(selectedVehicleView).start();
        CBikeSimState.getInstance().addThread(selectedVehicleView);
    }

    private int calculateDuration(Client client) {
        int duration;
        int velocity = 25;
        double distance =
                Math.abs(client.getTo().getPosition().getX() - client.getFrom().getPosition().getX()) +
                        Math.abs(client.getTo().getPosition().getY() - client.getFrom().getPosition().getY());

        if(client.getBike().getBikeType() == Bike.ELECTRIC) velocity = 45;
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
        Image normalBikeImage = new Image(getClass().getResource("/img/bike.png").toExternalForm());
        Image electricBikeImage = new Image(getClass().getResource("/img/bike_elec.png").toExternalForm());

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns && count < station.getMaxCapacity(); column++) {
                if (count < numBikes) {
                    view.getTopPane().add(
                            new BikeStallView(
                                    (station.getAvailableBikeList().get(count).getBikeType() == Bike.ELECTRIC) ? electricBikeImage : normalBikeImage,
                                    station.getAvailableBikeList().get(count).getId(),
                                    this, BikeStallView.STATION,
                                    true,
                                    false),
                            column, row);
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
            for (int column = 0; column < columns && count < 9; column++) {
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
        Image normalBikeImage = new Image(getClass().getResource("/img/bike.png").toExternalForm());
        Image electricBikeImage = new Image(getClass().getResource("/img/bike_elec.png").toExternalForm());

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns && count < vehicle.getMaxCapacity(); column++) {
                if (count < numBikes) {
                    view.getBottomPane().add(
                            new BikeStallView(
                                    (vehicle.getBikeList().get(count).getBikeType() == Bike.ELECTRIC) ? electricBikeImage : normalBikeImage,
                                    vehicle.getBikeList().get(count).getId(),
                                    this,
                                    BikeStallView.VEHICLE,
                                    true,
                                    false),
                            column, row);
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

    private Client getClientWith(String id) {
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

    private Bike getBikeWith(String id) {
        for (Bike bike : selectedStation.getAvailableBikeList()) {
            if (bike.getId().equals(id)) return bike;
        }
        // throw error
        return null;
    }

    private Station getStationWith(String id) {
        for (Station station : scenario.getStationList()) {
            if (station.getId().equals(id)) return station;
        }
        //throw error
        return null;
    }

    private void prepareTimer() {
        stopTimer();
        timer = new Timer(time);
    }

    private void stopTimer(){
        if(timer != null) timer.stopTimer();
    }

    private void prepareClientGenerator(){
        stopClientGenerator();
        this.clientGenerator = new ClientGenerator(ClientGenerator.RANDOM, scenario, this, 5);
        this.clientGeneratorStrategySelector = new ClientGeneratorStrategySelector(clientGenerator,time,difficulty);
    }

    private void stopClientGenerator(){
        if(this.clientGenerator != null) this.clientGenerator.stopRun();
        if(this.clientGeneratorStrategySelector != null) this.clientGeneratorStrategySelector.stopRun();
    }

    private void prepareMusic() {
        String pathSelect = getClass().getResource("/music/select.wav").toString();
        String path = getClass().getResource("/music/soundtrack_game.mp3").toString();
        String pathStart = getClass().getResource("/music/startGame.mp3").toString();
        String pathClient = getClass().getResource("/music/client_arrives.mp3").toString();

        Media mediaSelect = new Media(pathSelect);
        Media media = new Media(path);
        Media mediaStart = new Media(pathStart);
        Media mediaClient = new Media(pathClient);

        this.mpSelect = new MediaPlayer(mediaSelect);
        this.mp = new MediaPlayer(media);
        this.mpStart = new MediaPlayer(mediaStart);
        this.mpClient = new MediaPlayer(mediaClient);

        this.mp.setVolume(0.3);
        this.mpStart.setVolume(0.5);
        this.mpSelect.setVolume(0.3);
        this.mpClient.setVolume(0.2);
    }

    //DURATION in SECONDS
    private void startTimer() {
        //countdown
        timer.startTimer();
        Label timerText = timer.getTimerLabel();
        timerText.setTranslateX(180);
        view.getUtilityPane().getChildren().addAll( timer.getTimerTitle(), timerText);
    }

    private void startScore(){
        score = new Score();

        Label label = score.getScore();
        label.setTranslateX(140);
        label.setTranslateY(50);

        Label labelTitle = score.getScoreTitle();
        labelTitle.setTranslateY(50);

        view.getUtilityPane().getChildren().addAll(labelTitle, label);
    }

    private void startClientGenerator() {
        clientGenerator.start();
        CBikeSimState.getInstance().addThread(clientGenerator);
    }

    private void startClientGeneratorStrategySelector(){
        clientGeneratorStrategySelector.start();
        CBikeSimState.getInstance().addThread(clientGeneratorStrategySelector);
    }

    private void addPause() {
        ImageView play_pause = new ImageView(new Image(getClass().getResource("/img/pause-play-button.png").toExternalForm()));
        play_pause.setLayoutX(10);
        play_pause.setLayoutY(10);
        view.getMapPane().getChildren().add(play_pause);
        play_pause.setOnMouseClicked(e -> {
            try {
                GameMenu.Presenter menu = new GameMenuPresenter(this);
                GameMenu.View view = new GameMenuView(menu);
                menu.initMenu();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
    }


}
