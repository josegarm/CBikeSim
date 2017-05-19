package es.cbikesim.lib.pattern;


import es.cbikesim.scenario.model.Bike;
import es.cbikesim.scenario.model.ElectricBike;
import es.cbikesim.scenario.model.NormalBike;

public class BikeFactory {

    static int id = 0;

    public static Bike spawnBike(Bike.BikeType type){
        Bike bike = null;
        switch(type){
            case NORMAL:
                bike = new NormalBike("normalBike" + id);
                break;
            case ELECTRIC:
                bike = new ElectricBike("electricBike" + id);
                break;
            default:
                //throw exception
                break;
        }
        incrementId();
        return bike;
    }

    private static void incrementId(){
        BikeFactory.id++;
    }
}
