package es.cbikesim.scenario.usecase;


import es.cbikesim.lib.pattern.Command;
import es.cbikesim.scenario.model.Client;
import es.cbikesim.scenario.model.Scenario;
import es.cbikesim.scenario.model.Station;

public class NextClientDepositsBikeInStationUseCase implements Command{

    private Station station;
    private Scenario scenario;

    public NextClientDepositsBikeInStationUseCase(Station station, Scenario scenario) {
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
