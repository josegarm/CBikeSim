package es.cbikesim.scenario.usecase;

import es.cbikesim.lib.pattern.Command;
import es.cbikesim.scenario.model.Scenario;
import es.cbikesim.scenario.model.Station;
import es.cbikesim.scenario.model.Vehicle;

/**
 * Use Case: The vehicle deposit bikes in the arrival station
 */
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