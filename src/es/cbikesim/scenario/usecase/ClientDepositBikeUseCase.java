package es.cbikesim.scenario.usecase;

import es.cbikesim.lib.pattern.Command;
import es.cbikesim.scenario.model.Client;
import es.cbikesim.scenario.model.Scenario;
import es.cbikesim.scenario.model.Station;

/**
 * Use Case: The client deposit a bike in the arrival station
 */
public class ClientDepositBikeUseCase implements Command{

    private Client client;
    private Scenario scenario;

    public ClientDepositBikeUseCase(Client client, Scenario scenario) {
        this.client = client;
        this.scenario = scenario;
    }

    @Override
    public void execute() {
        Station currentStation = client.getTo();
        client.setFrom(currentStation);
        client.setTo(null);
        scenario.getClientsInTransit().remove(client);

        if (currentStation.getAvailableBikeList().size() < currentStation.max_capacity){
            currentStation.getAvailableBikeList().add(client.getBike());
            client.setBike(null);
        } else {
            currentStation.getClientWaitingToDepositList().add(client);
        }
    }
}
