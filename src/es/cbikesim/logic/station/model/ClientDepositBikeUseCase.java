package es.cbikesim.logic.station.model;

import es.cbikesim.lib.Command;
import es.cbikesim.logic.client.model.Client;
import es.cbikesim.logic.scenario.model.Scenario;

public class ClientDepositBikeUseCase implements Command{

    private Scenario scenario;
    private Client client;
    private Station station;

    public ClientDepositBikeUseCase(Scenario scenario, Client client, Station station){
        this.scenario = scenario;
        this.client = client;
        this.station = station;
    }


    @Override
    public void execute() {
        scenario.getClientsInTransit().remove(client);
        if (station.getAvailableBikeList().size() < station.MAX_CAPACITY){
            station.getAvailableBikeList().add(client.getBike());
            client.setBike(null);
        } else {
            station.getClientWaitingToDepositList().add(client);
        }
    }
}
