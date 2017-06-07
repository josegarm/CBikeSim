package es.cbikesim.game.usecase;


import es.cbikesim.game.model.Client;
import es.cbikesim.game.model.Scenario;
import es.cbikesim.game.model.Station;
import es.cbikesim.lib.exception.UseCaseException;
import es.cbikesim.lib.pattern.Command;

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
    public void execute() throws UseCaseException {
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
