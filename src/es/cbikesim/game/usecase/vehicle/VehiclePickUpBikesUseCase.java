package es.cbikesim.game.usecase.vehicle;


import es.cbikesim.game.model.Bike;
import es.cbikesim.game.model.Scenario;
import es.cbikesim.game.model.Station;
import es.cbikesim.game.model.Vehicle;
import es.cbikesim.lib.exception.UseCaseException;
import es.cbikesim.lib.pattern.Command;

/**
 * Use Case: The vehicle pick up bikes in the current station and go to other station
 */
public class VehiclePickUpBikesUseCase implements Command {

    private Vehicle vehicle;
    private Bike bike;
    private Scenario scenario;

    public VehiclePickUpBikesUseCase(Vehicle vehicle, Bike bike, Scenario scenario) {
        this.vehicle = vehicle;
        this.bike = bike;
        this.scenario = scenario;
    }

    @Override
    public void execute() throws UseCaseException {
        validate();

        Station at = vehicle.getAt();

        if (at.getAvailableBikeList().size() >= 1) {
            at.getAvailableBikeList().remove(bike);
            vehicle.getBikeList().add(bike);
        }
    }

    private void validate() throws UseCaseException {
        if (vehicle == null)    throw new UseCaseException("Error: VehiclePickUpBikesUseCase -> Vehicle is null");
        if (bike == null)       throw new UseCaseException("Error: VehicleDepositBikeUseCase -> Bike is null");
        if (scenario == null)   throw new UseCaseException("Error: VehiclePickUpBikesUseCase -> Scenario is null");
    }
}
