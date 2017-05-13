package es.cbikesim.logic.station.usecases;


import es.cbikesim.lib.pattern.Command;
import es.cbikesim.logic.bike.model.Bike;
import es.cbikesim.logic.client.model.Client;
import es.cbikesim.logic.scenario.model.Scenario;
import es.cbikesim.logic.station.model.Station;

public class ClientPickUpBikeUseCase implements Command{

    private Station station;
    private Station to;
    private Client client;
    private Scenario scenario;

    public ClientPickUpBikeUseCase(Station station, Client client, Scenario scenario, Station to){
        this.station = station;
        this.to = to;
        this.client = client;
        this.scenario = scenario;
    }

    @Override
    public void execute() {
        if(station.getClientWaitingToPickUpList().indexOf(client) == 0){
            Bike pickedUp = station.getAvailableBikeList().iterator().next();
            client.setBike(pickedUp);
            station.getAvailableBikeList().remove(pickedUp);
            scenario.getClientsInTransit().add(client);
            station.getClientWaitingToPickUpList().get(0).setTo(to);
            station.getClientWaitingToPickUpList().remove(station.getClientWaitingToPickUpList().get(0));
        }else{
            //error
            System.out.println("Client id" + client.getId() + " is not first in line!");
        }
    }
}
