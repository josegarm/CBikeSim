package es.cbikesim.game.usecase.vehicle;


import es.cbikesim.game.model.Bike;
import es.cbikesim.game.model.Scenario;
import es.cbikesim.game.model.Station;
import es.cbikesim.game.model.Vehicle;
import es.cbikesim.lib.exception.UseCaseException;
import es.cbikesim.lib.util.Command;

/**
 * Use Case: The vehicle picks up bikes in the current station and go to other station
 */
public class VehiclePicksUpBikeUseCase implements Command {

    private Vehicle vehicle;
    private Bike bike;
    private Scenario scenario;

    public VehiclePicksUpBikeUseCase(Vehicle vehicle, Bike bike, Scenario scenario) {
        this.vehicle = vehicle;
        this.bike = bike;
        this.scenario = scenario;
    }

    @Override
    public void execute() throws UseCaseException {
        validate();

        Station at = vehicle.getAt();

        if (at.getAvailableBikeList().size() >= 1 && vehicle.getBikeList().size() < vehicle.getMaxCapacity()) {
            at.getAvailableBikeList().remove(bike);
            vehicle.getBikeList().add(bike);
        }
    }

    private void validate() throws UseCaseException {
        if (vehicle == null) throw new UseCaseException("Error: VehiclePicksUpBikeUseCase -> Vehicle is null");
        if (bike == null) throw new UseCaseException("Error: VehicleDepositsBikeUseCase -> Bike is null");
        if (scenario == null) throw new UseCaseException("Error: VehiclePicksUpBikeUseCase -> Scenario is null");
    }
}
