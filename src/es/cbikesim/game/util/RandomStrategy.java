package es.cbikesim.game.util;

import es.cbikesim.game.model.Scenario;

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
