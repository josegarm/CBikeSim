package es.cbikesim.game.model;


public class ElectricBike extends Bike{

    public ElectricBike(int id) {
        super(Bike.ELECTRIC, id);
    }

    @Override
    public String toString() {
        return "Bike \n" +
                "{ \n" +
                "   id = '" + super.getId() + "'" + ",\n" +
                "   type = Electric\n" +
                "}";
    }

}
