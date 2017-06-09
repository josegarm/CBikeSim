package es.cbikesim.game.presenter;

import es.cbikesim.game.contract.Game;
import es.cbikesim.game.model.Scenario;
import es.cbikesim.game.model.Station;
import es.cbikesim.game.usecase.CreateScenarioUseCase;
import es.cbikesim.game.view.BikeStallView;
import es.cbikesim.game.view.ClientListView;
import es.cbikesim.game.view.StationView;
import es.cbikesim.lib.exception.UseCaseException;
import es.cbikesim.lib.pattern.Command;
import es.cbikesim.lib.pattern.Invoker;
import es.cbikesim.lib.util.Timer;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class GamePresenter implements Game.Presenter {

    public static final int EASY = 0, NORMAL = 1, HARD = 2, CUSTOM = 3;

    private Game.View view;
    private Scenario scenario;
    private Station selectedStation;
    private MediaPlayer mp, mpSelect;
    private Timer timer;

    private Invoker invoker = new Invoker();

    private int difficulty;

    public GamePresenter(){
        scenario = new Scenario();
    }

    @Override
    public void load() {
        timerStart();
        prepareMusic();
        mp.play();

        paintMap();

    }

    @Override
    public void playSelect() {
        mpSelect.stop();
        mpSelect.play();
    }

    @Override
    public void createScenario(int difficulty, int numBikes, int carCapacity) {
        this.difficulty = difficulty;
        Command createScenario;

        switch (difficulty){
            case EASY:
                createScenario = new CreateScenarioUseCase(scenario);
                prepareTimer(120);
                break;
            case NORMAL:
                createScenario = new CreateScenarioUseCase(scenario);
                prepareTimer(160);
                break;
            case HARD:
                createScenario = new CreateScenarioUseCase(scenario);
                prepareTimer(180);
                break;
            case CUSTOM:
                createScenario = new CreateScenarioUseCase(scenario);
                break;
            default:
                createScenario = new CreateScenarioUseCase(scenario);
        }

        invoker.setCommand(createScenario);
        try { invoker.invoke(); }
        catch (UseCaseException e) { e.printStackTrace(); }
    }

    @Override
    public void showDataFromStation(String id) {
        Station station = getSelectedStationWith(id);
        selectedStation = station;
        paintStationBikePanel(selectedStation);
        paintStationClientPanel(selectedStation);
    }

    @Override
    public void setView(Game.View view) {
        this.view = view;
    }



    private void paintStationBikePanel(Station station){
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
        view.getTopTitle().setText(station.getId() + " - Bikes Status");
    }

    private void paintStationClientPanel(Station station){
        view.getClientPane().getChildren().clear();

        int count = 0;
        int rows = view.getClientPane().getRowConstraints().size();
        int columns = view.getClientPane().getColumnConstraints().size();
        int numClients = station.getAvailableBikeList().size();

        Image clientEmptyImage = new Image(getClass().getResource("/img/client_empty.png").toExternalForm());
        Image clientImage = new Image(getClass().getResource("/img/client.png").toExternalForm());

        for (int row = 0; row < rows; row++){
            for(int column = 0; column < columns && count < station.getClientWaitingToPickUpList().size(); column++){
                if(count < numClients){
                    view.getClientPane().add(new ClientListView(clientImage, station.getClientWaitingToPickUpList().get(count).getId(),this), column, row);
                } else {
                    view.getClientPane().add(new ClientListView(clientEmptyImage), column, row);
                }
                count++;
            }
        }
        view.getBottomTitle().setText("Clients Waiting");
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

    private Station getSelectedStationWith(String id){
        for(Station station : scenario.getStationList()){
            if(station.getId().equals(id)) return station;
        }
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
