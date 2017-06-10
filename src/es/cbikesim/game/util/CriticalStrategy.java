package es.cbikesim.game.util;

import es.cbikesim.game.model.Scenario;
import es.cbikesim.game.model.Station;

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
        int indexSelectedStation = (new Random().nextInt(stations.size()));
        stations.get(indexSelectedStation).getClientWaitingToPickUpList().add(ClientFactory.makeClient());
    }


}
