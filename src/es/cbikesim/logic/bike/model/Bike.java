package es.cbikesim.logic.bike.model;

public abstract class Bike {

    private final String id;
    private BikeType type = null;

    public Bike(BikeType type, String id) {
        this.type = type;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public BikeType getBikeType(){ return type; }
    public void setBikeType(BikeType type){ this.type = type; }

}
