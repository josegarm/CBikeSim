package es.cbikesim.game.command;

import es.cbikesim.game.model.Bike;
import es.cbikesim.game.model.Scenario;
import es.cbikesim.game.model.Station;
import es.cbikesim.game.util.factories.BikeFactory;
import es.cbikesim.lib.exception.UseCaseException;
import es.cbikesim.lib.pattern.Command;

public class GenerateNormalStationBikes implements Command {

    private Scenario scenario;

    public GenerateNormalStationBikes(Scenario scenario) {
        this.scenario = scenario;
    }

    @Override
    public void execute() throws UseCaseException {
        for(Station station : scenario.getStationList()){
            for (int numBike = 0; numBike < station.getMaxCapacity()/3; numBike++){
                station.getAvailableBikeList().add(BikeFactory.makeBike(Bike.RANDOM));
            }
        }
    }
}

