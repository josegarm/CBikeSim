package es.cbikesim.scenario.model;

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

    public List<Station> getStationList() {
        return stationList;
    }

    public List<Client> getClientsInTransit() {
        return clientsInTransit;
    }

    public List<Vehicle> getVehiclesInTransit() {
        return vehiclesInTransit;
    }

    @Override
    public String toString() {
        return "Scenario \n" +
                "{ \n" +
                "   stationList = " + stationList + ",\n" +
                "   clientsInTransit = " + clientsInTransit + ",\n" +
                "   vehiclesInTransit = " + vehiclesInTransit + "\n" +
                "}";
    }
}
