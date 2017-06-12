package es.cbikesim.game.usecase.station;


import es.cbikesim.game.model.Client;
import es.cbikesim.game.model.Scenario;
import es.cbikesim.game.model.Station;
import es.cbikesim.lib.exception.UseCaseException;
import es.cbikesim.lib.pattern.Command;

public class NextClientPicksUpBikeFromStationUseCase implements Command {

    private Station station;
    private Scenario scenario;

    public NextClientPicksUpBikeFromStationUseCase(Station station, Scenario scenario) {
        this.station = station;
        this.scenario = scenario;
    }

    @Override
    public void execute() throws UseCaseException {
        if (!station.getClientWaitingToPickUpList().isEmpty() && !station.getAvailableBikeList().isEmpty()) {
            Client client = station.getClientWaitingToPickUpList().remove(0);
            client.setBike(station.getAvailableBikeList().remove(0));
            scenario.getClientsInTransit().add(client);
            this.execute();
        }
    }
}
