package es.cbikesim.scenario.usecase;


import es.cbikesim.lib.pattern.Command;
import es.cbikesim.scenario.model.Client;
import es.cbikesim.scenario.model.Scenario;
import es.cbikesim.scenario.model.Station;

public class NextClientPicksUpBikeOfStationUseCase implements Command{

    private Station station;
    private Scenario scenario;

    public NextClientPicksUpBikeOfStationUseCase(Station station, Scenario scenario) {
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
