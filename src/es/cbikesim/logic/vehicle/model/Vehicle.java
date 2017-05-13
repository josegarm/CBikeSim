package es.cbikesim.logic.vehicle.model;

import es.cbikesim.logic.bike.model.Bike;
import es.cbikesim.logic.station.model.Station;

import java.util.ArrayList;
import java.util.List;

public class Vehicle {

    private final String id;
    private Station to;
    private Station from;

    private List<Bike> bikeList;

    public Vehicle(String id){
        this.id = id;
        this.bikeList = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public List<Bike> getBikeList() {
        return bikeList;
    }

    public Station getTo() {
        return to;
    }

    public void setTo(Station to) {
        this.to = to;
    }

    public Station getFrom() {
        return from;
    }

    public void setFrom(Station from) {
        this.from = from;
    }
}
