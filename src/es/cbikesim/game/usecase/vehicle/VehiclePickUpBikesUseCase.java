package es.cbikesim.game.usecase.vehicle;


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
    private Scenario scenario;

    public VehiclePickUpBikesUseCase(Vehicle vehicle, Scenario scenario) {
        this.vehicle = vehicle;
        this.scenario = scenario;
    }

    @Override
    public void execute() throws UseCaseException {
        validate();

        Station at = vehicle.getAt();

        if (at.getAvailableBikeList().size() >= 1) {
            vehicle.getBikeList().add(at.getAvailableBikeList().remove(0));
        }
    }

    private void validate() throws UseCaseException {
        if (vehicle == null) throw new UseCaseException("Error: VehiclePickUpBikesUseCase -> Vehicle is null");
        if (scenario == null) throw new UseCaseException("Error: VehiclePickUpBikesUseCase -> Scenario is null");
    }
}
