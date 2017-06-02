package es.cbikesim.lib.pattern;


import es.cbikesim.scenario.model.Bike;
import es.cbikesim.scenario.model.ElectricBike;
import es.cbikesim.scenario.model.NormalBike;

public class BikeFactory {

    public static int id = 0;

    public static Bike spawnBike(int type){
        Bike bike = null;
        switch(type){
            case Bike.NORMAL:
                bike = new NormalBike(id++);
                break;
            case Bike.ELECTRIC:
                bike = new ElectricBike(id++);
                break;
            default:
                //throw exception
                break;
        }
        return bike;
    }
}
