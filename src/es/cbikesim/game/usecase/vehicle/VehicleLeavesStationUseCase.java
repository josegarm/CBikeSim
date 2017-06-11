package es.cbikesim.game.usecase.vehicle;

import es.cbikesim.game.model.Scenario;
import es.cbikesim.game.model.Station;
import es.cbikesim.game.model.Vehicle;
import es.cbikesim.lib.exception.UseCaseException;
import es.cbikesim.lib.pattern.Command;

public class VehicleLeavesStationUseCase implements Command {

    private Vehicle vehicle;
    private Station to;
    private Scenario scenario;

    public VehicleLeavesStationUseCase(Vehicle vehicle, Station to, Scenario scenario) {
        this.vehicle = vehicle;
        this.to = to;
        this.scenario = scenario;
    }

    @Override
    public void execute() throws UseCaseException {
        validate();

        vehicle.setFrom(vehicle.getAt());
        vehicle.setTo(to);
        vehicle.setAt(null);

        vehicle.getFrom().getVehicleList().remove(vehicle);
        scenario.getVehiclesInTransit().add(vehicle);
    }

    private void validate() throws UseCaseException{
        if(vehicle == null)     throw new UseCaseException("Error: VehicleLeavesStationUseCase -> Vehicle is null");
        if(to == null)          throw new UseCaseException("Error: VehicleLeavesStationUseCase -> Station is null");
        if(scenario == null)    throw new UseCaseException("Error: VehicleLeavesStationUseCase -> Scenario is null");
    }
}

