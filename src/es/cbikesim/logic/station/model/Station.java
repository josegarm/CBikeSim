package es.cbikesim.logic.station.model;

import es.cbikesim.lib.Point;
import es.cbikesim.logic.bike.model.Bike;
import es.cbikesim.logic.client.model.Client;
import es.cbikesim.logic.vehicle.model.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class Station {

    public final int ID, MAX_CAPACITY;
    public final Point POSITION;

    private List<Bike> availableBikeList;
    private List<Client> clientWaitingToDepositList;
    private List<Client> clientWaitingToPickUpList;
    private List<Vehicle> vehicleList;

    public Station(int ID, int MAX_CAPACITY, Point POSITION) {
        this.ID = ID;
        this.MAX_CAPACITY = MAX_CAPACITY;
        this.POSITION = POSITION;
        this.availableBikeList = new ArrayList<>();
        this.clientWaitingToDepositList = new ArrayList<>();
        this.clientWaitingToPickUpList = new ArrayList<>();
        this.vehicleList = new ArrayList<>();
    }

    public List<Bike> getAvailableBikeList() {
        return availableBikeList;
    }

    public void setAvailableBikeList(List<Bike> availableBikeList) {
        this.availableBikeList = availableBikeList;
    }

    public List<Client> getClientWaitingToDepositList() {
        return clientWaitingToDepositList;
    }

    public void setClientWaitingToDepositList(List<Client> clientWaitingToDepositList) {
        this.clientWaitingToDepositList = clientWaitingToDepositList;
    }

    public List<Client> getClientWaitingToPickUpList() {
        return clientWaitingToPickUpList;
    }

    public void setClientWaitingToPickUpList(List<Client> clientWaitingToPickUpList) {
        this.clientWaitingToPickUpList = clientWaitingToPickUpList;
    }

    public List<Vehicle> getVehicleList() {
        return vehicleList;
    }

    public void setVehicleList(List<Vehicle> vehicleList) {
        this.vehicleList = vehicleList;
    }
}
