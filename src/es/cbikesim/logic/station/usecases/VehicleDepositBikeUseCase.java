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
        if(station.getAvailableBikeList().size() + vehicle.getBikeList().size() < station.MAX_CAPACITY){
            station.getAvailableBikeList().addAll(vehicle.getBikeList());
            vehicle.getBikeList().removeAll(vehicle.getBikeList());
            station.getVehicleList().add(vehicle);
        }
    }
}