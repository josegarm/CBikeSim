package es.cbikesim.game.presenter;

import es.cbikesim.game.contract.Game;
import es.cbikesim.game.model.Bike;
import es.cbikesim.game.model.Scenario;
import es.cbikesim.game.model.Station;
import es.cbikesim.game.usecase.CreateScenarioUseCase;
import es.cbikesim.game.view.StationView;
import es.cbikesim.lib.exception.UseCaseException;
import es.cbikesim.lib.pattern.Command;
import es.cbikesim.lib.pattern.Invoker;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class GamePresenter implements Game.Presenter {

    public static final int EASY = 0, NORMAL = 1, HARD = 2, CUSTOM = 3;

    private Game.View view;
    private Scenario scenario;
    private Station selectedStation;
    private MediaPlayer mp, mpSelect;

    private Invoker invoker = new Invoker();

    private int difficulty;

    public GamePresenter(){
        scenario = new Scenario();
    }

    @Override
    public void load() {
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
                break;
            case NORMAL:
                createScenario = new CreateScenarioUseCase(scenario);
                break;
            case HARD:
                createScenario = new CreateScenarioUseCase(scenario);
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
    }

    @Override
    public void setView(Game.View view) {
        this.view = view;
    }


    private void paintStationBikePanel(Station station){
        int count = 0;
        for (int row = 0; row < station.getMaxCapacity()/3; row++){
            for( int column = 0; column < 3 && count < station.getMaxCapacity(); column++){
                ImageView imageBikeEmpty = new ImageView(new Image(getClass().getResource("/img/bike_empty.png").toExternalForm()));
                imageBikeEmpty.setFitWidth(50.0);
                imageBikeEmpty.setFitHeight(50.0);
                view.getBikePane().add(imageBikeEmpty,column,row);
                count++;
            }
        }

        /*
        for(Bike bike : station.getAvailableBikeList()){
            view.getBikePane().getChildren().add(new ImageView(new Image(getClass().getResource("/img/bike.png").toExternalForm())));
        }
        */
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

}
