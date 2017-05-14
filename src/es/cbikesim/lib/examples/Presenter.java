package es.cbikesim.lib.examples;

import es.cbikesim.lib.Point;
import es.cbikesim.lib.pattern.Command;
import es.cbikesim.lib.pattern.Invoker;
import es.cbikesim.logic.bike.model.BikeFactory;
import es.cbikesim.logic.bike.model.BikeType;
import es.cbikesim.logic.client.model.Client;
import es.cbikesim.logic.client.usecase.ClientDepositBikeUseCase;
import es.cbikesim.logic.scenario.model.Scenario;
import es.cbikesim.logic.station.model.Station;
import es.cbikesim.logic.station.usecase.NextClientsPickUpBikeUseCase;

public class Presenter {

    View view = new View();

    public void init(){
        Scenario scenario = Scenario.getInstance();

        Station station1 = new Station("MyStation1",2,new Point(1,1));
        Station station2 = new Station("MyStation2",10,new Point(2,2));

        scenario.getStationList().add(station1);
        scenario.getStationList().add(station2);

        Client client = new Client("MyClient");
        client.setFrom(station1);
        client.setTo(station2);
        client.setBike(BikeFactory.spawnBike(BikeType.NORMAL));

        scenario.getClientsInTransit().add(client);

        this.clientDepositBike(client);
    }

    /**
     * Example method, presenter is not implemented yet
     */
    public void clientDepositBike(Client client){
        Scenario scenario = Scenario.getInstance();

        System.out.println("First \n" + client); //FIRST PRINT

        Command clientDepositBike = new ClientDepositBikeUseCase(client, scenario);
        Command nextClientsPickUpBike = new NextClientsPickUpBikeUseCase(client.getTo(), scenario);

        Invoker invoker = new Invoker();

        invoker.setCommand(clientDepositBike);
        invoker.invoke();
        invoker.setCommand(nextClientsPickUpBike);
        invoker.invoke();

        System.out.println("Last \n" + client); //LAST PRINT

        view.updateView();
    }

}
