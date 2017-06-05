package es.cbikesim.game.model;

import java.util.ArrayList;
import java.util.List;

public class Scenario {

    private List<Station> stationList;
    private List<Client> clientsInTransit;
    private List<Vehicle> vehiclesInTransit;

    public Scenario(){
        this.stationList = new ArrayList<>();
        this.clientsInTransit = new ArrayList<>();
        this.vehiclesInTransit = new ArrayList<>();
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
