package es.cbikesim.logic.station.model;

import es.cbikesim.lib.Point;
import es.cbikesim.logic.bike.model.Bike;
import es.cbikesim.logic.client.model.Client;
import es.cbikesim.logic.vehicle.model.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class Station {

    private final int ID, MAX_CAPACITY;
    private final Point POSITION;

    private List<Bike> availableBikeList;
    private List<Client> bikeDepositList;
    private List<Client> clientWaitingList;
    private List<Vehicle> vehicleList;

    public Station(int ID, int MAX_CAPACITY, Point POSITION) {
        this.ID = ID;
        this.MAX_CAPACITY = MAX_CAPACITY;
        this.POSITION = POSITION;
        this.availableBikeList = new ArrayList<>();
        this.bikeDepositList = new ArrayList<>();
        this.clientWaitingList = new ArrayList<>();
        this.vehicleList = new ArrayList<>();
    }

    public int getID() {
        return ID;
    }

    public int getMAX_CAPACITY() {
        return MAX_CAPACITY;
    }

    public Point getPOSITION() {
        return POSITION;
    }

    public List<Bike> getAvailableBikeList() {
        return availableBikeList;
    }

    public void setAvailableBikeList(List<Bike> availableBikeList) {
        this.availableBikeList = availableBikeList;
    }

    public List<Client> getBikeDepositList() {
        return bikeDepositList;
    }

    public void setBikeDepositList(List<Client> bikeDepositList) {
        this.bikeDepositList = bikeDepositList;
    }

    public List<Client> getClientWaitingList() {
        return clientWaitingList;
    }

    public void setClientWaitingList(List<Client> clientWaitingList) {
        this.clientWaitingList = clientWaitingList;
    }

    public List<Vehicle> getVehicleList() {
        return vehicleList;
    }

    public void setVehicleList(List<Vehicle> vehicleList) {
        this.vehicleList = vehicleList;
    }
}
