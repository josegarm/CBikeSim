package es.cbikesim.logic.station.usecases;


import es.cbikesim.lib.pattern.Command;
import es.cbikesim.logic.client.model.Client;
import es.cbikesim.logic.scenario.model.Scenario;
import es.cbikesim.logic.station.model.Station;

public class NextClientsDepositBikeUseCase implements Command{

    private Station station;
    private Scenario scenario;

    public NextClientsDepositBikeUseCase(Station station, Scenario scenario) {
        this.station = station;
        this.scenario = scenario;
    }

    @Override
    public void execute() {
        if (!station.getClientWaitingToDepositList().isEmpty() && station.getAvailableBikeList().size() < station.max_capacity){
            Client client = station.getClientWaitingToDepositList().remove(0);
            station.getAvailableBikeList().add(client.getBike());
            client.setBike(null);
            this.execute();
        }
    }
}
