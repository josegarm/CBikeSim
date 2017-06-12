package es.cbikesim.game.usecase.client;

import es.cbikesim.game.model.Client;
import es.cbikesim.game.model.Scenario;
import es.cbikesim.game.model.Station;
import es.cbikesim.lib.exception.UseCaseException;
import es.cbikesim.lib.pattern.Command;

/**
 * Use Case: The client deposit a bike in the arrival station
 */
public class ClientDepositBikeUseCase implements Command {

    private Client client;
    private Scenario scenario;

    public ClientDepositBikeUseCase(Client client, Scenario scenario) {
        this.client = client;
        this.scenario = scenario;
    }

    @Override
    public void execute() throws UseCaseException {
        validate();

        Station to = client.getTo();
        scenario.getClientsInTransit().remove(client);

        if (to.getAvailableBikeList().size() < to.getMaxCapacity()) {
            to.getAvailableBikeList().add(client.getBike());
            client.setBike(null);
            client.setFrom(to);
            client.setTo(null);
        } else {
            if (!to.getClientWaitingToDepositList().contains(client)) to.getClientWaitingToDepositList().add(client);
        }
    }

    private void validate() throws UseCaseException {
        if (client == null) throw new UseCaseException("Error: ClientDepositBikeUseCase -> Client is null");
        if (scenario == null) throw new UseCaseException("Error: ClientDepositBikeUseCase -> Scenario is null");
    }
}
