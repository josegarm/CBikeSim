package es.cbikesim.game.util.factories;


import es.cbikesim.game.model.Bike;
import es.cbikesim.game.model.ElectricBike;
import es.cbikesim.game.model.NormalBike;

public class BikeFactory {

    private static int id = 0;

    public static Bike makeBike(int type){
        Bike bike = null;

        switch(type){
            case Bike.NORMAL:
                bike = new NormalBike(""+id++);
                break;
            case Bike.ELECTRIC:
                bike = new ElectricBike(""+id++);
                break;
            case Bike.RANDOM:
                if (id%2 == 0)  bike = new NormalBike(""+id++);
                else            bike = new ElectricBike(""+id++);
                break;
            default:
                //throw exception
                break;
        }

        return bike;
    }
}
