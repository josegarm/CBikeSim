package es.cbikesim.lib.pattern;


import es.cbikesim.logic.bike.model.Bike;
import es.cbikesim.logic.bike.model.BikeType;
import es.cbikesim.logic.bike.model.ElectricBike;
import es.cbikesim.logic.bike.model.NormalBike;

public class BikeFactory {

     int id = 0;

    public Bike spawnBike(BikeType type){
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

    private void incrementId(){
        id++;
    }
}
