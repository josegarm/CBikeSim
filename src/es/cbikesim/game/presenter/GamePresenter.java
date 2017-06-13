package es.cbikesim.game.presenter;

import es.cbikesim.app.CBikeSimState;
import es.cbikesim.game.command.*;
import es.cbikesim.game.contract.Game;
import es.cbikesim.game.model.*;
import es.cbikesim.game.usecase.client.ClientDepositBikeUseCase;
import es.cbikesim.game.usecase.client.ClientPickUpBikeUseCase;
import es.cbikesim.game.usecase.vehicle.VehicleArrivesStationUseCase;
import es.cbikesim.game.usecase.vehicle.VehicleDepositsBikeUseCase;
import es.cbikesim.game.usecase.vehicle.VehicleLeavesStationUseCase;
import es.cbikesim.game.usecase.vehicle.VehiclePicksUpBikeUseCase;
import es.cbikesim.game.util.ClientGenerator;
import es.cbikesim.game.util.ClientGeneratorStrategySelector;
import es.cbikesim.game.view.*;
import es.cbikesim.gameMenu.contract.GameMenu;
import es.cbikesim.gameMenu.presenter.GameMenuPresenter;
import es.cbikesim.gameMenu.view.GameMenuView;
import es.cbikesim.lib.exception.UseCaseException;
import es.cbikesim.lib.util.Command;
import es.cbikesim.lib.util.Invoker;
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

import java.util.ArrayList;
import java.util.List;

public class GamePresenter implements Game.Presenter {

    public static final int EASY = 0, NORMAL = 1, HARD = 2, CUSTOM = 3;
    public static final String FEW_BIKES = "FEW", NORMAL_BIKES = "NORMAL", MANY_BIKES = "MANY";
    private static final int NOTHING = 0, STATION = 1, VEHICLE = 2;
    private final GamePrinter gamePrinter = new GamePrinter(this);

    private Game.View view;
    private GameModelFinder gameModelFinder;

    private ClientGenerator clientGenerator;
    private ClientGeneratorStrategySelector clientGeneratorStrategySelector;

    private Scenario scenario;

    private Station selectedStation;
    private Vehicle selectedVehicle;

    private VehicleView selectedVehicleView;

    private MediaPlayer mp, mpSelect, mpStart, mpClient;
    private Timer timer;
    private Score score;

    private List<Thread> threads;

    private String numBikes;
    private int difficulty, time, carCapacity;
    private int itemSelectedType = NOTHING;


    public GamePresenter(int difficulty, int time, String numBikes, int carCapacity) {
        this.difficulty = difficulty;
        this.time = time;
        this.numBikes = numBikes;
        this.carCapacity = carCapacity;

        this.threads = new ArrayList<>();
    }

    @Override
    public void initGame() {
        scenario = new Scenario();
        gameModelFinder = new GameModelFinder(scenario);

        prepareTimer();
        prepareClientGenerator();
        prepareMusic();

        createScenario(difficulty, time, numBikes, carCapacity);

        view.start(CBikeSimState.getInstance().getPrimaryStage());

        load();
    }

    @Override
    public void stopGame() {
        stopTimer();
        stopClientGenerator();
        mp.stop();
        for(Thread t : threads) t.interrupt();
    }

    @Override
    public void backToMainMenu() {
        stopGame();

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
        try {
            Station station = gameModelFinder.getStationWith(id);
            selectedStation = station;
            gamePrinter.paintStationPanel(station);
            itemSelectedType = STATION;
        } catch (UseCaseException e) { System.err.println(e.getMessage()); }
    }

    @Override
    public void showDataFromVehicle(String id) {
        try {
            Vehicle vehicle = gameModelFinder.getVehicleWith(id);
            selectedVehicle = vehicle;
            selectedStation = vehicle.getAt();
            gamePrinter.paintVehiclePanel(vehicle);
            itemSelectedType = VEHICLE;
        } catch (UseCaseException e) { System.err.println(e.getMessage()); }
    }

    @Override
    public void notifyNewClient(Client client) {
        ImageView newClientNotification = new ClientNotificationView(client.getFrom().getPosition());

        Platform.runLater(() -> {
            if (CBikeSimState.getInstance().getAudio()) {
                mpClient.stop();
                mpClient.play();
            }

            if (client.getFrom() == selectedStation && itemSelectedType == STATION)
                gamePrinter.paintStationPanel(selectedStation);
            view.getMapPane().getChildren().add(newClientNotification);
            new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                view.getMapPane().getChildren().remove(newClientNotification);
            })).play();
        });
    }

    @Override
    public void clientPicksUpBike(String idClient, String idBike) {
        try {
            Client client = gameModelFinder.getClientWith(idClient);
            Bike bike = gameModelFinder.getBikeWith(idBike);

            Invoker invoker = new Invoker();
            Command clientPicksUpBike = new ClientPickUpBikeUseCase(client, bike, scenario);

            invoker.addCommand(clientPicksUpBike);
            invoker.invoke();

            gamePrinter.paintStationPanel(selectedStation);
            gamePrinter.paintClientInTransit(client);
        } catch (UseCaseException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void clientDepositsBike(String idClient, ClientView clientView) {
        try {
            Client client = gameModelFinder.getClientWith(idClient);

            Invoker invoker = new Invoker();
            Command clientDepositBike = new ClientDepositBikeUseCase(client, scenario);

            invoker.addCommand(clientDepositBike);
            invoker.invoke();

            if (client.getBike() == null) {
                Platform.runLater(() -> {
                    if (client.getFrom() == selectedStation && itemSelectedType == STATION)
                        gamePrinter.paintStationPanel(selectedStation);
                    view.getMapPane().getChildren().remove(clientView);
                });
                clientView.stopRun();
            } else {
                Platform.runLater(() -> {
                    score.changeScore(-10);
                    clientView.setLayoutX(-25 + (-25.0 * client.getTo().getClientWaitingToDepositList().indexOf(client)));
                });
            }
        } catch (UseCaseException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void vehiclePicksUpBike(String idBike) {
        try {
            Bike bike = gameModelFinder.getBikeWith(idBike);
            Vehicle vehicle = selectedVehicle;

            Invoker invoker = new Invoker();
            Command vehiclePicksUpBike = new VehiclePicksUpBikeUseCase(vehicle, bike, scenario);

            invoker.addCommand(vehiclePicksUpBike);
            invoker.invoke();

            gamePrinter.paintVehiclePanel(vehicle);
        } catch (UseCaseException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void vehicleDepositsBike(String idBike) {
        try {
            Bike bike = gameModelFinder.getBikeWith(idBike);
            Vehicle vehicle = selectedVehicle;

            Invoker invoker = new Invoker();
            Command vehiclePicksUpBike = new VehicleDepositsBikeUseCase(vehicle, bike, scenario);

            invoker.addCommand(vehiclePicksUpBike);
            invoker.invoke();

            gamePrinter.paintVehiclePanel(vehicle);
        } catch (UseCaseException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void moveVehicleToAnotherStation(String idVehicle, String idStationTarget) {
        try {
            Vehicle vehicle = gameModelFinder.getVehicleWith(idVehicle);
            Station to = gameModelFinder.getStationWith(idStationTarget);

            Invoker invoker = new Invoker();
            Command vehicleLeavesStation = new VehicleLeavesStationUseCase(vehicle, to, scenario);

            invoker.addCommand(vehicleLeavesStation);
            invoker.invoke();

            showDataFromVehicle(idVehicle);
            gamePrinter.paintVehicleInTransit(vehicle);
        } catch (UseCaseException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void vehicleArriveStation(String idVehicle, VehicleView vehicleView) {
        try {
            Vehicle vehicle = gameModelFinder.getVehicleWith(idVehicle);

            Invoker invoker = new Invoker();
            Command vehicleArrivesStation = new VehicleArrivesStationUseCase(vehicle, scenario);

            invoker.addCommand(vehicleArrivesStation);
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
        if (CBikeSimState.getInstance().getAudio()) mp.play();
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
                generateBikes = new GenerateHardStationBikes(scenario);
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
        gamePrinter.paintMap();
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

    private void prepareTimer() {
        stopTimer();
        timer = new Timer(time);
    }

    private void stopTimer() {
        if (timer != null) timer.stopTimer();
    }

    private void prepareClientGenerator() {
        stopClientGenerator();
        this.clientGenerator = new ClientGenerator(ClientGenerator.RANDOM, scenario, this, 5);
        this.clientGeneratorStrategySelector = new ClientGeneratorStrategySelector(clientGenerator, time, difficulty);
    }

    private void stopClientGenerator() {
        if (this.clientGenerator != null) this.clientGenerator.stopRun();
        if (this.clientGeneratorStrategySelector != null) this.clientGeneratorStrategySelector.stopRun();
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
        int count = 0;
        timer.getTime().addListener(e -> {
            if (timer.getTime().getValue() == 0) {
                GameMenu.Presenter menu = new GameMenuPresenter(this, score);
                GameMenu.View view = new GameMenuView(menu);
                menu.initMenu();
            }
        });


        Label timerText = timer.getTimerLabel();
        timerText.setTranslateX(180);
        view.getUtilityPane().getChildren().addAll(timer.getTimerTitle(), timerText);
    }

    private void startScore() {
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

    private void startClientGeneratorStrategySelector() {
        clientGeneratorStrategySelector.start();
        CBikeSimState.getInstance().addThread(clientGeneratorStrategySelector);
    }

    private void addPause() {
        ImageView play_pause = new ImageView(new Image(getClass().getResource("/img/icon-menu.png").toExternalForm()));
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


    public Scenario getScenario() {
        return scenario;
    }

    public List<Thread> getThreads() {
        return threads;
    }

    public Game.View getView() {
        return view;
    }

    public VehicleView getSelectedVehicleView() {
        return selectedVehicleView;
    }

    public Score getScore() {
        return score;
    }
}
