package es.cbikesim.game.model;

import es.cbikesim.lib.util.Point;

import java.util.ArrayList;
import java.util.List;

public class Station{

    private final String id;
    private final int maxCapacity;
    private final Point position;

    private List<Bike> availableBikeList;
    private List<Client> clientWaitingToDepositList;
    private List<Client> clientWaitingToPickUpList;
    private List<Vehicle> vehicleList;

    public Station(String id, int maxCapacity, Point position) {
        this.id = id;
        this.maxCapacity = maxCapacity;
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

    public String getId(){
        return id;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public Point getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "Station \n" +
                "{ \n" +
                "   id = '" + id + "\'" + ",\n" +
                "   maxCapacity = " + maxCapacity + ",\n" +
                "   position = " + position + ",\n" +
                "   availableBikeList = " + availableBikeList + ",\n" +
                "   clientWaitingToDepositList = " + clientWaitingToDepositList + ",\n" +
                "   clientWaitingToPickUpList = " + clientWaitingToPickUpList + ",\n" +
                "   vehicleList = " + vehicleList + "\n" +
                "}";
    }
}
