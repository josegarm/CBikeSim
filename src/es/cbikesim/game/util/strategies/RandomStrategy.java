package es.cbikesim.game.util.strategies;

import es.cbikesim.game.model.Scenario;
import es.cbikesim.game.util.factories.ClientFactory;

import java.util.Random;

public class RandomStrategy implements Strategy{

    private Scenario scenario;

    public RandomStrategy(Scenario scenario){
        this.scenario = scenario;
    }

    @Override
    public void generateClient() {
        int indexSelectedStation = (new Random().nextInt(scenario.getStationList().size()));
        scenario.getStationList().get(indexSelectedStation).getClientWaitingToPickUpList().add(ClientFactory.makeClient());
    }


}
