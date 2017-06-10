package es.cbikesim.game.presenter;

import es.cbikesim.app.CBikeSimState;
import es.cbikesim.game.command.CreateStations;
import es.cbikesim.game.command.GenerateEasyStationBikes;
import es.cbikesim.game.command.GenerateNormalStationBikes;
import es.cbikesim.game.contract.Game;
import es.cbikesim.game.model.Bike;
import es.cbikesim.game.model.Client;
import es.cbikesim.game.model.Scenario;
import es.cbikesim.game.model.Station;
import es.cbikesim.game.usecase.ClientDepositBikeUseCase;
import es.cbikesim.game.usecase.ClientPickUpBikeUseCase;
import es.cbikesim.game.util.ClientGenerator;
import es.cbikesim.game.util.factories.PathAnimationFactory;
import es.cbikesim.game.view.BikeStallView;
import es.cbikesim.game.view.ClientInStationView;
import es.cbikesim.game.view.ClientView;
import es.cbikesim.game.view.StationView;
import es.cbikesim.lib.exception.UseCaseException;
import es.cbikesim.lib.pattern.Command;
import es.cbikesim.lib.pattern.Invoker;
import es.cbikesim.lib.util.Timer;
import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class GamePresenter implements Game.Presenter {

    public static final int EASY = 0, NORMAL = 1, HARD = 2, CUSTOM = 3;
    public static final String FEW_BIKES = "FEW", NORMAL_BIKES = "NORMAL", MANY_BIKES = "MANY";

    private Game.View view;
    private Scenario scenario;
    private Station selectedStation;

    private MediaPlayer mp, mpSelect;
    private Timer timer;

    private Invoker invoker = new Invoker();
    private ClientGenerator clientGenerator;

    private int difficulty;

    public GamePresenter(){
        scenario = new Scenario();
    }

    @Override
    public void createScenario(int difficulty, int time, String numBikes, int carCapacity) {
        this.difficulty = difficulty;
        prepareTimer(time);

        Command createStations = new CreateStations(scenario);
        Command generateBikes;

        switch (difficulty){
            case EASY:
                generateBikes = new GenerateEasyStationBikes(scenario);
                break;
            case NORMAL:
                generateBikes = new GenerateNormalStationBikes(scenario);
                break;
            case HARD:
                generateBikes = new GenerateNormalStationBikes(scenario);
                break;
            case CUSTOM:
                generateBikes = new GenerateNormalStationBikes(scenario);
                break;
            default:
                generateBikes = new GenerateNormalStationBikes(scenario);
        }

        invoker.clear();
        invoker.addCommand(createStations);
        invoker.addCommand(generateBikes);
        try { invoker.invoke(); }
        catch (UseCaseException e) { e.printStackTrace(); }
    }

    @Override
    public void load() {
        prepareMusic();
        paintMap();
        timerStart();
        startClientGenerator();
        if(CBikeSimState.getInstance().getAudio()) mp.play();
    }

    @Override
    public void playSelect() {
        if(CBikeSimState.getInstance().getAudio()) {
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
    public void clientPicksUpBike(String idClient, String idBike) {
        Client client = getClientWith(idClient);
        Bike bike = getBikeWith(idBike);

        Command clientPicksUpBike = new ClientPickUpBikeUseCase(client, bike, scenario);

        invoker.clear();
        invoker.addCommand(clientPicksUpBike);
        try { invoker.invoke(); }
        catch (UseCaseException e) { e.printStackTrace(); }

        paintStationPanel(selectedStation);
        paintClientInTransit(client);
    }

    @Override
    public void clientDepositsBike(String idClient, ClientView clientView) {
        Client client = getClientWith(idClient);

        Command clientDepositBike = new ClientDepositBikeUseCase(client, scenario);

        invoker.clear();
        invoker.addCommand(clientDepositBike);
        try { invoker.invoke(); }
        catch (UseCaseException e) { e.printStackTrace(); }

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                view.getMapPane().getChildren().remove(clientView);
            }
        });


    }


    @Override
    public void setView(Game.View view) {
        this.view = view;
    }


    private void paintMap(){
        for(Station station : scenario.getStationList()){
            paintStation(station);
        }
    }

    private void paintStation(Station station){
        StationView stationView = new StationView(station.getPosition(), station.getId(),this);
        view.getMapPane().getChildren().add(stationView);
    }

    private void paintClientInTransit(Client client){
        ClientView clientView = new ClientView(client.getFrom().getPosition(), client.getId(),this);
        view.getMapPane().getChildren().add(clientView);
        int seconds = calculateDuration(client);

        clientView.setAnimation(PathAnimationFactory.pathAnimationFactory(client.getFrom().getPosition(), client.getTo().getPosition()));
        clientView.setDuration(seconds);

        new Thread(clientView).start();
    }

    private int calculateDuration(Client client){
        int duration;
        double distance =
                Math.abs(client.getTo().getPosition().getX() - client.getFrom().getPosition().getX()) +
                Math.abs(client.getTo().getPosition().getY() - client.getFrom().getPosition().getY());
        duration = (int) distance/35;
        return duration;
    }

    private void paintStationPanel(Station station){
        paintStationBikePanel(station);
        paintStationClientPanel(station);
    }

    private void paintStationBikePanel(Station station){
        view.getTopTitle().setText(station.getId() + " - Bikes Status");
        view.getBikePane().getChildren().clear();

        int count = 0;
        int rows = view.getBikePane().getRowConstraints().size();
        int columns = view.getBikePane().getColumnConstraints().size();
        int numBikes = station.getAvailableBikeList().size();

        Image bikeEmptyImage = new Image(getClass().getResource("/img/bike_empty.png").toExternalForm());
        Image bikeImage = new Image(getClass().getResource("/img/bike.png").toExternalForm());

        for (int row = 0; row < rows; row++){
            for(int column = 0; column < columns && count < station.getMaxCapacity(); column++){
                if(count < numBikes){
                    view.getBikePane().add(new BikeStallView(bikeImage, station.getAvailableBikeList().get(count).getId(),this), column, row);
                } else {
                    view.getBikePane().add(new BikeStallView(bikeEmptyImage), column, row);
                }
                count++;
            }
        }
    }

    private void paintStationClientPanel(Station station){
        view.getBottomTitle().setText("Clients Waiting");
        view.getClientPane().getChildren().clear();

        int count = 0;
        int rows = view.getClientPane().getRowConstraints().size();
        int columns = view.getClientPane().getColumnConstraints().size();
        int numClients = station.getClientWaitingToPickUpList().size();

        Image clientEmptyImage = new Image(getClass().getResource("/img/client_empty.png").toExternalForm());
        Image clientImage = new Image(getClass().getResource("/img/client.png").toExternalForm());

        for (int row = 0; row < rows; row++){
            for(int column = 0; column < columns && count < 6; column++){
                if(count < numClients){
                    view.getClientPane().add(new ClientInStationView(clientImage, station.getClientWaitingToPickUpList().get(count).getId(),this), column, row);
                } else {
                    view.getClientPane().add(new ClientInStationView(clientEmptyImage), column, row);
                }
                count++;
            }
        }
    }

    private Station getSelectedStationWith(String id){
        for(Station station : scenario.getStationList()){
            if(station.getId().equals(id)) return station;
        }
        return null;
    }

    public Client getClientWith(String id){
        for (Client client : scenario.getClientsInTransit()) {
            if(client.getId().equals(id)) return client;
        }
        for (Station station : scenario.getStationList()) {
            for (Client client : station.getClientWaitingToPickUpList()){
                if(client.getId().equals(id)) return client;
            }
            for (Client client : station.getClientWaitingToDepositList()){
                if(client.getId().equals(id)) return client;
            }
        }
        // throw error
        return null;
    }

    public Bike getBikeWith(String id){
        for (Bike bike : selectedStation.getAvailableBikeList()) {
            if(bike.getId().equals(id)) return bike;
        }
        // throw error
        return null;
    }

    private void prepareMusic(){
        String pathSelect = getClass().getResource("/music/select.wav").toString();
        String path = getClass().getResource("/music/soundtrack_game.mp3").toString();

        Media mediaSelect = new Media(pathSelect);
        Media media = new Media(path);

        this.mpSelect = new MediaPlayer(mediaSelect);
        this.mp = new MediaPlayer(media);

        this.mpSelect.setVolume(1.0);
    }

    private void prepareTimer(int seconds){
        timer = new Timer(seconds);
    }

    private void startClientGenerator(){
        clientGenerator = new ClientGenerator(scenario, 3000);
        clientGenerator.start();
        CBikeSimState.getInstance().getPrimaryStage().setOnCloseRequest(event -> clientGenerator.cancel());
    }

    //DURATION in SECONDS
    private void timerStart(){
        //countdown
        timer.startTimer();
        Label timerText = timer.getTimerLabel();
        timerText.setTranslateX(180);
        view.getUtilityPane().getChildren().addAll(
                timer.getTimerTitle(),
                timerText
        );

    }

}
