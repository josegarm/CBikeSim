package es.cbikesim.logic.station.usecases;

import es.cbikesim.lib.pattern.Command;
import es.cbikesim.logic.scenario.model.Scenario;
import es.cbikesim.logic.station.model.Station;
import es.cbikesim.logic.vehicle.model.Vehicle;

public class VehicleDepositBikeUseCase implements Command{

    private Scenario scenario;
    private Vehicle vehicle;
    private Station station;

    public VehicleDepositBikeUseCase(Scenario scenario, Vehicle vehicle, Station station){
        this.scenario = scenario;
        this.vehicle = vehicle;
        this.station = station;
    }

    @Override
    public void execute(){
        scenario.getVehiclesInTransit().remove(vehicle);
        //check if bikes carried in vehicle plus the bikes in the station are less than the max capacity
        if(station.getAvailableBikeList().size() + vehicle.getBikeList().size() < station.MAX_CAPACITY){
            //bikes fit
            station.getAvailableBikeList().addAll(vehicle.getBikeList());
            //remove bikes from vehicle
            vehicle.getBikeList().removeAll(vehicle.getBikeList());
            station.getVehicleList().add(vehicle);
        }else{
            station.getVehicleList().add(vehicle);
            //indicate that vehicle is waiting with bikes
        }
    }
}