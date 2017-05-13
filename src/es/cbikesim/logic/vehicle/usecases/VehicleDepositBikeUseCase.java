package es.cbikesim.logic.vehicle.usecases;

import es.cbikesim.lib.pattern.Command;
import es.cbikesim.logic.scenario.model.Scenario;
import es.cbikesim.logic.station.model.Station;
import es.cbikesim.logic.vehicle.model.Vehicle;

import java.util.Iterator;

public class VehicleDepositBikeUseCase implements Command{

    private Vehicle vehicle;
    private Scenario scenario;

    public VehicleDepositBikeUseCase(Vehicle vehicle, Scenario scenario) {
        this.vehicle = vehicle;
        this.scenario = scenario;
    }

    @Override
    public void execute(){
        Station currentStation = vehicle.getTo();
        vehicle.setFrom(currentStation);
        vehicle.setTo(null);
        scenario.getVehiclesInTransit().remove(vehicle);
        currentStation.getVehicleList().add(vehicle);

        while (!vehicle.getBikeList().isEmpty() && currentStation.getAvailableBikeList().size() < currentStation.max_capacity){
            currentStation.getAvailableBikeList().add(vehicle.getBikeList().remove(0));
        }
    }
}