package es.cbikesim.logic.vehicle.model;

import es.cbikesim.logic.bike.model.Bike;
import es.cbikesim.logic.station.model.Station;

import java.util.ArrayList;
import java.util.List;

public class Vehicle {

    private final int ID;
    private Station to;
    private Station from;

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

    public void setStationTo(Station to){ this.to = to; }

    public Station getStationTo(){ return to; }

    public void setStationFrom(Station from){ this.from = from; }

    public Station getStationFrom(){ return from; }
}
