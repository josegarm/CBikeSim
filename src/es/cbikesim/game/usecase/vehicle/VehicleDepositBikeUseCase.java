package es.cbikesim.game.usecase.vehicle;

import es.cbikesim.game.model.Bike;
import es.cbikesim.game.model.Scenario;
import es.cbikesim.game.model.Station;
import es.cbikesim.game.model.Vehicle;
import es.cbikesim.lib.exception.UseCaseException;
import es.cbikesim.lib.pattern.Command;

/**
 * Use Case: The vehicle deposit bikes in the arrival station
 */
public class VehicleDepositBikeUseCase implements Command {

    private Vehicle vehicle;
    private Bike bike;
    private Scenario scenario;

    public VehicleDepositBikeUseCase(Vehicle vehicle, Bike bike, Scenario scenario) {
        this.vehicle = vehicle;
        this.bike = bike;
        this.scenario = scenario;
    }

    @Override
    public void execute() throws UseCaseException {
        validate();

        Station at = vehicle.getAt();
        vehicle.getAt().getVehicleList().add(vehicle);

        if (!vehicle.getBikeList().isEmpty() && at.getAvailableBikeList().size() < at.getMaxCapacity()) {
            vehicle.getBikeList().remove(bike);
            at.getAvailableBikeList().add(bike);
        }
    }

    private void validate() throws UseCaseException {
        if (vehicle == null) throw new UseCaseException("Error: VehicleDepositBikeUseCase -> Vehicle is null");
        if (bike == null) throw new UseCaseException("Error: VehicleDepositBikeUseCase -> Bike is null");
        if (scenario == null) throw new UseCaseException("Error: VehicleDepositBikeUseCase -> Scenario is null");
    }
}