package es.cbikesim.game.usecase.vehicle;

import es.cbikesim.game.model.Bike;
import es.cbikesim.game.model.Scenario;
import es.cbikesim.game.model.Station;
import es.cbikesim.game.model.Vehicle;
import es.cbikesim.lib.exception.UseCaseException;
import es.cbikesim.lib.util.Command;

/**
 * Use Case: The vehicle deposits bikes in the arrival station
 */
public class VehicleDepositsBikeUseCase implements Command {

    private Vehicle vehicle;
    private Bike bike;
    private Scenario scenario;

    public VehicleDepositsBikeUseCase(Vehicle vehicle, Bike bike, Scenario scenario) {
        this.vehicle = vehicle;
        this.bike = bike;
        this.scenario = scenario;
    }

    @Override
    public void execute() throws UseCaseException {
        validate();

        Station at = vehicle.getAt();

        if (!vehicle.getBikeList().isEmpty() && at.getAvailableBikeList().size() < at.getMaxCapacity()) {
            vehicle.getBikeList().remove(bike);
            at.getAvailableBikeList().add(bike);
        }
    }

    private void validate() throws UseCaseException {
        if (vehicle == null) throw new UseCaseException("Error: VehicleDepositsBikeUseCase -> Vehicle is null");
        if (bike == null) throw new UseCaseException("Error: VehicleDepositsBikeUseCase -> Bike is null");
        if (scenario == null) throw new UseCaseException("Error: VehicleDepositsBikeUseCase -> Scenario is null");
    }
}