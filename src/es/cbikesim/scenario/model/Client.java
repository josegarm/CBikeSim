package es.cbikesim.scenario.model;

public class Client {

    private final int id;

    private Bike bike;
    private Station from;
    private Station to;

    public Client(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Bike getBike() {
        return bike;
    }

    public void setBike(Bike bike) {
        this.bike = bike;
    }

    public Station getFrom(){ return this.from; }

    public void setFrom(Station from){ this.from = from; }

    public Station getTo(){ return this.to; }

    public void setTo(Station to) { this.to = to; }

    @Override
    public String toString() {
        return "Client \n" +
                "{ \n" +
                "   id = '" + id + "\'" + ",\n" +
                "   bike = " + bike + ",\n" +
                "   from = " + from + ",\n" +
                "   to = " + to + "\n" +
                "}";
    }
}
