package es.cbikesim.logic.client.model;

import es.cbikesim.logic.bike.model.Bike;
import es.cbikesim.logic.station.model.Station;

public class Client {

    public static final int OFF = 0, ON = 1;

    private final String ID;

    private int state;
    private Bike bike;
    private Station from;
    private Station to;

    public Client(String ID){
        this.ID = ID;
        this.state = Client.OFF;
    }

    public String getID() {
        return ID;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Bike getBike() {
        return bike;
    }

    public void setBike(Bike bike) {
        this.bike = bike;
    }

    public Station getFrom(){ return this.from; }

    public void setFrom(Station station){ this.from = from; }

    public Station getTo(){ return this.to; }

    public void setTo(Station to) { this.to = to; }

}
