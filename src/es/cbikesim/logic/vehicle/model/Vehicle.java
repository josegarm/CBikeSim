package es.cbikesim.logic.vehicle.model;

import es.cbikesim.logic.bike.model.Bike;

import java.util.ArrayList;
import java.util.List;

public class Vehicle {

    private final int ID;

    private List<Bike> bikeList;

    public Vehicle(int ID){
        this.ID = ID;
        this.bikeList = new ArrayList<>();
    }

    public int getID() {
        return ID;
    }

    public List<Bike> getBikeList() {
        return bikeList;
    }

    public void setBikeList(List<Bike> bikeList) {
        this.bikeList = bikeList;
    }
}
