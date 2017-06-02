package es.cbikesim.scenario.model;

import java.util.ArrayList;
import java.util.List;

public class Vehicle {

    private final int id;
    private Station to;
    private Station from;

    private List<Bike> bikeList;

    public Vehicle(int id){
        this.id = id;
        this.bikeList = new ArrayList<>();
    }

    public int getId() {
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

    @Override
    public String toString() {
        return "Vehicle \n" +
                "{ \n" +
                "   id = '" + id + "\'" + ",\n" +
                "   to = " + to + ",\n" +
                "   from = " + from + ",\n" +
                "   bikeList = " + bikeList + "\n" +
                "}";
    }
}
