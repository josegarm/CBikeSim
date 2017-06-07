package es.cbikesim.game.presenter;

import es.cbikesim.game.contract.Game;
import es.cbikesim.game.model.Scenario;
import es.cbikesim.game.model.Station;
import es.cbikesim.game.usecase.CreateScenarioUseCase;
import es.cbikesim.game.view.Sprite;
import es.cbikesim.lib.exception.UseCaseException;
import es.cbikesim.lib.pattern.Command;
import es.cbikesim.lib.pattern.Invoker;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class GamePresenter implements Game.Presenter {

    private Game.View view;
    private Scenario scenario;
    private Station selectedStation;

    private Invoker invoker = new Invoker();

    @Override
    public void createScenario(int difficult) {
        this.scenario = new Scenario();
        Command createScenario = new CreateScenarioUseCase(scenario);
        invoker.setCommand(createScenario);
        try {
            invoker.invoke();
        } catch (UseCaseException e) {
            e.printStackTrace();
        }
        paintStations();
    }

    @Override
    public void setView(Game.View view) {
        this.view = view;
    }

    public void setSelectedStation(int station){
        selectedStation = scenario.getStationList().get(station);
    }

    public void addBikesFromStationToBikePane(){
        if(selectedStation != null){
            for(int i = 0; i < selectedStation.getAvailableBikeList().size(); i++){
                view.getBikePane().getChildren().add(new ImageView(new Image(getClass().getResource("/img/bike.png").toExternalForm())));
            }
        }

    }

    private void paintStations(){
        Pane map = view.getMapPane();
        for(Station station : scenario.getStationList()){
            Circle renderStation = new Circle(station.position.getX(), station.position.getY(), 20);
            renderStation.setFill(Color.rgb(0,128,255));
            renderStation.setStroke(Color.rgb(0,76,153));
            renderStation.setStrokeWidth(3);
            renderStation.setId(new Integer(station.getId()).toString());
            renderStation.setOnMouseClicked(e -> {
                System.out.println(renderStation.getId());
            });
            map.getChildren().add(renderStation);
        }
    }




}
