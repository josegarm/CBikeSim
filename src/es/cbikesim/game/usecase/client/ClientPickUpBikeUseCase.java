package es.cbikesim.game.usecase.client;


import es.cbikesim.game.model.Bike;
import es.cbikesim.game.model.Client;
import es.cbikesim.game.model.Scenario;
import es.cbikesim.game.model.Station;
import es.cbikesim.lib.exception.UseCaseException;
import es.cbikesim.lib.util.Command;

/**
 * Use Case: The client pick up a bike in the current station and go to other station
 */
public class ClientPickUpBikeUseCase implements Command {

    private Client client;
    private Bike bike;
    private Scenario scenario;

    public ClientPickUpBikeUseCase(Client client, Bike bike, Scenario scenario) {
        this.client = client;
        this.bike = bike;
        this.scenario = scenario;
    }

    @Override
    public void execute() throws UseCaseException {
        validate();

        Station from = client.getFrom();
        client.setBike(bike);
        from.getAvailableBikeList().remove(bike);

        from.getClientWaitingToPickUpList().remove(client);
        scenario.getClientsInTransit().add(client);
    }

    private void validate() throws UseCaseException {
        if (client == null) throw new UseCaseException("Error: ClientPickUpBikeUseCase -> Client is null");
        if (bike == null) throw new UseCaseException("Error: ClientPickUpBikeUseCase -> Bike is null");
        if (scenario == null) throw new UseCaseException("Error: ClientPickUpBikeUseCase -> Scenario is null");
    }
}
