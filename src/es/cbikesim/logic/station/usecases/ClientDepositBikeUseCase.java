package es.cbikesim.logic.station.usecases;

import es.cbikesim.lib.pattern.Command;
import es.cbikesim.logic.client.model.Client;
import es.cbikesim.logic.scenario.model.Scenario;
import es.cbikesim.logic.station.model.Station;

public class ClientDepositBikeUseCase implements Command{

    private Scenario scenario;
    private Client client;

    public ClientDepositBikeUseCase(Scenario scenario, Client client){
        this.scenario = scenario;
        this.client = client;
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
