package es.cbikesim.logic.station.model;

import es.cbikesim.lib.Point;
import es.cbikesim.logic.bike.model.Bike;
import es.cbikesim.logic.client.model.Client;
import es.cbikesim.logic.vehicle.model.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class Station {

    private final String ID;
    public final int max_capacity;
    public final Point position;

    private List<Bike> availableBikeList;
    private List<Client> clientWaitingToDepositList;
    private List<Client> clientWaitingToPickUpList;
    private List<Vehicle> vehicleList;

    public Station(String ID, int max_capacity, Point position) {
        this.ID = ID;
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
}
