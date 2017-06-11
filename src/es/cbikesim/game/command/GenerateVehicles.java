package es.cbikesim.game.command;

import es.cbikesim.game.model.Scenario;
import es.cbikesim.game.model.Station;
import es.cbikesim.game.model.Vehicle;
import es.cbikesim.lib.exception.UseCaseException;
import es.cbikesim.lib.pattern.Command;

public class GenerateVehicles implements Command {

    private Scenario scenario;
    private int carCapacity;

    public GenerateVehicles(Scenario scenario, int carCapacity) {
        this.scenario = scenario;
        this.carCapacity = carCapacity;
    }

    @Override
    public void execute() throws UseCaseException {
        Station station = scenario.getStationList().get(0);
        station.getVehicleList().add(new Vehicle("Vehicle", carCapacity, station));
    }
}
