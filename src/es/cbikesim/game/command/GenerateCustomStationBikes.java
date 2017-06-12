package es.cbikesim.game.command;

import es.cbikesim.game.model.Bike;
import es.cbikesim.game.model.Scenario;
import es.cbikesim.game.model.Station;
import es.cbikesim.game.presenter.GamePresenter;
import es.cbikesim.game.util.factories.BikeFactory;
import es.cbikesim.lib.exception.UseCaseException;
import es.cbikesim.lib.pattern.Command;

public class GenerateCustomStationBikes implements Command {

    private Scenario scenario;
    private String numBikes;

    public GenerateCustomStationBikes(Scenario scenario, String numBikes) {
        this.scenario = scenario;
        this.numBikes = numBikes;
    }

    @Override
    public void execute() throws UseCaseException {
        double n;

        switch (numBikes){
            case GamePresenter.FEW_BIKES:
                n = 1.0/4;
                break;
            case GamePresenter.NORMAL_BIKES:
                n = 1.0/2;
                break;
            case GamePresenter.MANY_BIKES:
                n = 3.0/4;
                break;
            default:
                n = 1.0/2;
                break;
        }

        for (Station station : scenario.getStationList()) {
            for (int numBike = 0; numBike < station.getMaxCapacity() * n; numBike++) {
                station.getAvailableBikeList().add(BikeFactory.makeBike(Bike.RANDOM));
            }
        }
    }
}

