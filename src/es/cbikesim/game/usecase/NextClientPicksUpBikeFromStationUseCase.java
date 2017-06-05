package es.cbikesim.game.usecase;


import es.cbikesim.lib.pattern.Command;
import es.cbikesim.game.model.Client;
import es.cbikesim.game.model.Scenario;
import es.cbikesim.game.model.Station;

public class NextClientPicksUpBikeFromStationUseCase implements Command{

    private Station station;
    private Scenario scenario;

    public NextClientPicksUpBikeFromStationUseCase(Station station, Scenario scenario) {
        this.station = station;
        this.scenario = scenario;
    }

    @Override
    public void execute() {
        if (!station.getClientWaitingToPickUpList().isEmpty() && !station.getAvailableBikeList().isEmpty()){
            Client client = station.getClientWaitingToPickUpList().remove(0);
            client.setBike(station.getAvailableBikeList().remove(0));
            scenario.getClientsInTransit().add(client);
            this.execute();
        }
    }
}
