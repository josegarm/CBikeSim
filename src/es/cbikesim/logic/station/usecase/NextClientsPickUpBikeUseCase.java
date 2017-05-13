package es.cbikesim.logic.station.usecase;


import es.cbikesim.lib.pattern.Command;
import es.cbikesim.logic.client.model.Client;
import es.cbikesim.logic.scenario.model.Scenario;
import es.cbikesim.logic.station.model.Station;

public class NextClientsPickUpBikeUseCase implements Command{

    private Station station;
    private Scenario scenario;

    public NextClientsPickUpBikeUseCase(Station station, Scenario scenario) {
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
