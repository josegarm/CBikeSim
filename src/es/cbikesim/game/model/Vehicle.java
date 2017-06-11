package es.cbikesim.game.model;

import java.util.ArrayList;
import java.util.List;

public class Vehicle {

    private final String id;
    private final int maxCapacity;
    private Station from;
    private Station to;
    private Station at;

    private List<Bike> bikeList;

    public Vehicle(String id, int maxCapacity, Station at) {
        this.id = id;
        this.maxCapacity = maxCapacity;
        this.at = at;
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

    public Station getAt() {
        return at;
    }

    public void setAt(Station at) {
        this.at = at;
    }

    public int getMaxCapacity() {
        return this.maxCapacity;
    }

    @Override
    public String toString() {
        return "Vehicle \n" +
                "{ \n" +
                "   id = '" + id + "\'" + ",\n" +
                "   to = " + to + ",\n" +
                "   from = " + from + ",\n" +
                "   bikeList = " + bikeList + ",\n" +
                "}";
    }
}
