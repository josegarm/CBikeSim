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
        Station station1 = new Station("Primado Reig",9, new Point(465,70));
        Station station2 = new Station("Alfahuir",6, new Point(120,300));
        Station station3 = new Station("Universidad",9, new Point(470,480));

        station1.getAvailableBikeList().add(BikeFactory.spawnBike(Bike.ELECTRIC));
        station1.getAvailableBikeList().add(BikeFactory.spawnBike(Bike.NORMAL));
        station1.getAvailableBikeList().add(BikeFactory.spawnBike(Bike.ELECTRIC));
        station1.getAvailableBikeList().add(BikeFactory.spawnBike(Bike.NORMAL));

        scenario.getStationList().add(station1);
        scenario.getStationList().add(station2);
        scenario.getStationList().add(station3);

        station1.getClientWaitingToPickUpList().add(new Client("Jose"));
        station1.getClientWaitingToPickUpList().add(new Client("Manuel"));
        station1.getClientWaitingToPickUpList().add(new Client("Pedro"));



        /*
        Client client = new Client(1);
        client.setFrom(station1);
        client.setTo(station2);
        client.setBike(BikeFactory.spawnBike(Bike.NORMAL));

        scenario.getClientsInTransit().add(client);
        */
    }
}

