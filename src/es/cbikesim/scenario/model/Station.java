package es.cbikesim.scenario.model;

import es.cbikesim.lib.util.Point;

import java.util.ArrayList;
import java.util.List;

public class Station{

    private final int id;
    public final int max_capacity;
    public final Point position;

    private List<Bike> availableBikeList;
    private List<Client> clientWaitingToDepositList;
    private List<Client> clientWaitingToPickUpList;
    private List<Vehicle> vehicleList;

    public Station(int id, int max_capacity, Point position) {
        this.id = id;
        this.max_capacity = max_capacity;
        this.position = position;
        this.availableBikeList = new ArrayList<>();
        this.clientWaitingToDepositList = new ArrayList<>();
        this.clientWaitingToPickUpList = new ArrayList<>();
        this.vehicleList = new ArrayList<>();
    }

    public List<Bike> getAvailableBikeList() {
        return availableBikeList;
    }

    public List<Client> getClientWaitingToDepositList() {
        return clientWaitingToDepositList;
    }

    public List<Client> getClientWaitingToPickUpList() {
        return clientWaitingToPickUpList;
    }

    public List<Vehicle> getVehicleList() {
        return vehicleList;
    }

    @Override
    public String toString() {
        return "Station \n" +
                "{ \n" +
                "   id = '" + id + "\'" + ",\n" +
                "   max_capacity = " + max_capacity + ",\n" +
                "   position = " + position + ",\n" +
                "   availableBikeList = " + availableBikeList + ",\n" +
                "   clientWaitingToDepositList = " + clientWaitingToDepositList + ",\n" +
                "   clientWaitingToPickUpList = " + clientWaitingToPickUpList + ",\n" +
                "   vehicleList = " + vehicleList + "\n" +
                "}";
    }
}
