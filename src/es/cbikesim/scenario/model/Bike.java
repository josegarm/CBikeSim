package es.cbikesim.scenario.model;

public abstract class Bike {

    public static final int NORMAL = 0, ELECTRIC = 1;
    private final String id;
    private int type;

    public Bike(int type, String id) {
        this.type = type;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public int getBikeType(){ return type; }

    public void setBikeType(int type){ this.type = type; }

    @Override
    public String toString() {
        return "Bike \n" +
                "{ \n" +
                "   id = '" + id + "\'" + ",\n" +
                "   type = " + type + "\n" +
                "}";
    }
}
