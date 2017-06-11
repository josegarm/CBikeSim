package es.cbikesim.game.usecase.vehicle;

import es.cbikesim.game.model.Scenario;
import es.cbikesim.game.model.Station;
import es.cbikesim.game.model.Vehicle;
import es.cbikesim.lib.exception.UseCaseException;
import es.cbikesim.lib.pattern.Command;

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
    public void execute() throws UseCaseException {
        Station currentStation = vehicle.getAt();
        //vehicle.setFrom(currentStation);
        //vehicle.setTo(null);
        //scenario.getVehiclesInTransit().remove(vehicle);
        vehicle.getFrom().getVehicleList().add(vehicle);

        if (!vehicle.getBikeList().isEmpty() && currentStation.getAvailableBikeList().size() < currentStation.getMaxCapacity()){
            currentStation.getAvailableBikeList().add(vehicle.getBikeList().remove(0));
        }
    }
}