package es.cbikesim.game.usecase.station;


import es.cbikesim.game.model.Client;
import es.cbikesim.game.model.Scenario;
import es.cbikesim.game.model.Station;
import es.cbikesim.lib.exception.UseCaseException;
import es.cbikesim.lib.pattern.Command;

public class NextClientDepositsBikeInStationUseCase implements Command{

    private Station station;
    private Scenario scenario;

    public NextClientDepositsBikeInStationUseCase(Station station, Scenario scenario) {
        this.station = station;
        this.scenario = scenario;
    }

    @Override
    public void execute() throws UseCaseException {
        if (!station.getClientWaitingToDepositList().isEmpty() && station.getAvailableBikeList().size() < station.getMaxCapacity()){
            Client client = station.getClientWaitingToDepositList().remove(0);
            station.getAvailableBikeList().add(client.getBike());
            client.setBike(null);
            this.execute();
        }
    }
}
