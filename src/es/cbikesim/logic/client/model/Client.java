package es.cbikesim.logic.client.model;

import es.cbikesim.logic.bike.model.Bike;

public class Client {

    public static final int OFF = 0, ON = 1;

    private final int ID;

    private int state;
    private Bike bike;

    public Client(int ID){
        this.ID = ID;
        this.state = Client.OFF;
        this.bike = null;
    }

    public int getID() {
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

}
