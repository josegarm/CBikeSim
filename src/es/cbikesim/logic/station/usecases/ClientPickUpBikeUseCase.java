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
            //if client first in line then we assign him a bike
            Bike pickedUp = station.getAvailableBikeList().iterator().next();
            client.setBike(pickedUp);
            //remove bike from available list, picked up by client
            station.getAvailableBikeList().remove(pickedUp);
            //add to map in transitList
            scenario.getClientsInTransit().add(station.getClientWaitingToPickUpList().get(0));
            //add destination station
            station.getClientWaitingToPickUpList().get(0).setTo(to);
            //remove from waiting list
            station.getClientWaitingToPickUpList().remove(station.getClientWaitingToPickUpList().get(0));
        }else{
            //error
            System.out.println("Client id" + client.getID() + " is not first in line!");
        }
    }
}
