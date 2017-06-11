package es.cbikesim.game.usecase.vehicle;

import es.cbikesim.game.model.Scenario;
import es.cbikesim.game.model.Vehicle;
import es.cbikesim.lib.exception.UseCaseException;
import es.cbikesim.lib.pattern.Command;

public class VehicleArrivesStationUseCase implements Command {

    private Vehicle vehicle;
    private Scenario scenario;

    public VehicleArrivesStationUseCase(Vehicle vehicle, Scenario scenario) {
        this.vehicle = vehicle;
        this.scenario = scenario;
    }

    @Override
    public void execute() throws UseCaseException {
        validate();

        vehicle.setAt(vehicle.getTo());
        vehicle.setTo(null);
        vehicle.setFrom(null);

        vehicle.getAt().getVehicleList().add(vehicle);
        scenario.getVehiclesInTransit().remove(vehicle);
    }

    private void validate() throws UseCaseException {
        if (vehicle == null) throw new UseCaseException("Error: VehicleLeavesStationUseCase -> Vehicle is null");
        if (scenario == null) throw new UseCaseException("Error: VehicleLeavesStationUseCase -> Scenario is null");
    }
}

