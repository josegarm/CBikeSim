package es.cbikesim.logic.scenario.model;

import es.cbikesim.logic.client.model.Client;
import es.cbikesim.logic.station.model.Station;
import es.cbikesim.logic.vehicle.model.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class Scenario {

    private static Scenario instance;

    private List<Station> stationList;
    private List<Client> clientsInTransit;
    private List<Vehicle> vehiclesInTransit;

    private Scenario(){
        this.stationList = new ArrayList<>();
        this.clientsInTransit = new ArrayList<>();
        this.vehiclesInTransit = new ArrayList<>();
    }

    public static Scenario getInstance(){
        if (instance == null) instance = new Scenario();
        return instance;
    }



}
