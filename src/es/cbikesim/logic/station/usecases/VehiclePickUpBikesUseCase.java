package es.cbikesim.logic.station.usecases;


import es.cbikesim.lib.pattern.Command;
import es.cbikesim.logic.bike.model.Bike;
import es.cbikesim.logic.scenario.model.Scenario;
import es.cbikesim.logic.station.model.Station;
import es.cbikesim.logic.vehicle.model.Vehicle;

import java.util.Iterator;
import java.util.List;

public class VehiclePickUpBikesUseCase implements Command{

    private Station station;
    private Station to;
    private int bikesToPickUp;
    private Scenario scenario;
    private Vehicle vehicle;

    public VehiclePickUpBikesUseCase(Station station, int bikesToPickUp, Scenario scenario, Vehicle vehicle, Station to){
        this.station = station;
        this.to = to;
        this.bikesToPickUp = bikesToPickUp;
        this.scenario = scenario;
        this.vehicle = vehicle;
    }

    @Override
    public void execute(){
        if(station.getAvailableBikeList().size() - bikesToPickUp >= 0){
            Iterator it = station.getAvailableBikeList().iterator();
            while(bikesToPickUp >= 0){
                Bike chosen = (Bike) it.next();
                vehicle.getBikeList().add(chosen);
                station.getAvailableBikeList().remove(chosen);
            }
            vehicle.setStationTo(to);
            scenario.getVehiclesInTransit().add(vehicle);
            station.getVehicleList().remove(vehicle);
        }
    }
}
