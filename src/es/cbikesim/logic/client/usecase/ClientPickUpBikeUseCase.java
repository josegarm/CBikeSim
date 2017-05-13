package es.cbikesim.logic.client.usecase;


import es.cbikesim.lib.pattern.Command;
import es.cbikesim.logic.client.model.Client;
import es.cbikesim.logic.scenario.model.Scenario;
import es.cbikesim.logic.station.model.Station;

/**
 * Use Case: The client pick up a bike in the current station and go to other station
 */
public class ClientPickUpBikeUseCase implements Command{

    private Client client;
    private Station to;
    private Scenario scenario;

    public ClientPickUpBikeUseCase(Client client, Station to, Scenario scenario) {
        this.client = client;
        this.to = to;
        this.scenario = scenario;
    }

    @Override
    public void execute() {
        Station currentStation = client.getFrom();
        client.setTo(to);

        if (currentStation.getClientWaitingToPickUpList().isEmpty() && !currentStation.getAvailableBikeList().isEmpty()){
            client.setBike(currentStation.getAvailableBikeList().remove(0));
            scenario.getClientsInTransit().add(client);
        } else {
            currentStation.getClientWaitingToPickUpList().add(client);
        }
    }
}
