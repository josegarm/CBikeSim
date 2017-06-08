package es.cbikesim.game.model;


public class NormalBike extends Bike{

    public NormalBike(String id) {
        super(Bike.NORMAL, id);
    }

    @Override
    public String toString() {
        return "Bike \n" +
                "{ \n" +
                "   id = '" + super.getId() + "'" + ",\n" +
                "   type = Normal\n" +
                "}";
    }

}
