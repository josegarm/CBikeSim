package es.cbikesim.game.util.strategies;

import es.cbikesim.game.model.Client;
import es.cbikesim.game.model.Scenario;
import es.cbikesim.game.model.Station;
import es.cbikesim.game.util.factories.ClientFactory;

import java.util.Random;

public class RandomStrategy implements Strategy {

    private Scenario scenario;

    public RandomStrategy(Scenario scenario) {
        this.scenario = scenario;
    }

    @Override
    public Client generateClient() {
        int indexFrom = (new Random().nextInt(scenario.getStationList().size()));
        int indexTo = (new Random().nextInt(scenario.getStationList().size()));

        while (indexFrom == indexTo) indexTo = (new Random().nextInt(scenario.getStationList().size()));

        Station from = scenario.getStationList().get(indexFrom);
        Station to = scenario.getStationList().get(indexTo);

        Client client = ClientFactory.makeClient();
        client.setFrom(from);
        client.setTo(to);

        from.getClientWaitingToPickUpList().add(client);

        return client;
    }


}
