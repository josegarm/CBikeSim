package es.cbikesim.logic.bike.model;


public class BikeFactory {

    static int id = 0;

    public static Bike spawnBike(BikeType type){
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
