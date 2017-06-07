package es.cbikesim.game.usecase;

import es.cbikesim.game.model.Bike;
import es.cbikesim.game.model.Client;
import es.cbikesim.game.model.Scenario;
import es.cbikesim.game.model.Station;
import es.cbikesim.lib.exception.UseCaseException;
import es.cbikesim.lib.pattern.BikeFactory;
import es.cbikesim.lib.pattern.Command;
import es.cbikesim.lib.util.Point;

public class CreateScenarioUseCase implements Command {

    private Scenario scenario;

    public CreateScenarioUseCase(Scenario scenario) {
        this.scenario = scenario;
    }

    @Override
    public void execute() throws UseCaseException {
        Station station1 = new Station(1,2, new Point(50,90));
        Station station2 = new Station(2,10, new Point(120,300));

        scenario.getStationList().add(station1);
        scenario.getStationList().add(station2);

        Client client = new Client(1);
        client.setFrom(station1);
        client.setTo(station2);
        client.setBike(BikeFactory.spawnBike(Bike.NORMAL));

        scenario.getClientsInTransit().add(client);
    }
}

