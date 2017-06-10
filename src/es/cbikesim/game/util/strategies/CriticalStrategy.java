package es.cbikesim.game.util.strategies;

import es.cbikesim.game.model.Client;
import es.cbikesim.game.model.Scenario;
import es.cbikesim.game.model.Station;
import es.cbikesim.game.util.factories.ClientFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CriticalStrategy implements Strategy{

    private Scenario scenario;
    private List<Station> stations;

    public CriticalStrategy(Scenario scenario, int type){
        this.scenario = scenario;
        this.stations = new ArrayList<>();
        for (Station station : scenario.getStationList()){
            if(station.getCritical() == type) stations.add(station);
        }
    }

    @Override
    public void generateClient() {
        int indexFrom = (new Random().nextInt(stations.size()));
        int indexTo = (new Random().nextInt(scenario.getStationList().size()));

        while (indexFrom == indexTo) indexTo = (new Random().nextInt(scenario.getStationList().size()));

        Station from = stations.get(indexFrom);
        Station to = scenario.getStationList().get(indexTo);

        Client client = ClientFactory.makeClient();
        client.setFrom(from);
        client.setTo(to);

        from.getClientWaitingToPickUpList().add(client);
    }


}
