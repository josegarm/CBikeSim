package es.cbikesim.lib.examples;

import es.cbikesim.game.model.Bike;
import es.cbikesim.game.model.Client;
import es.cbikesim.game.model.Scenario;
import es.cbikesim.game.model.Station;
import es.cbikesim.game.usecase.ClientDepositBikeUseCase;
import es.cbikesim.game.usecase.NextClientPicksUpBikeFromStationUseCase;
import es.cbikesim.lib.exception.UseCaseException;
import es.cbikesim.lib.pattern.BikeFactory;
import es.cbikesim.lib.pattern.Command;
import es.cbikesim.lib.pattern.Invoker;
import es.cbikesim.lib.util.Point;

public class Presenter {

    Scenario scenario = new Scenario();
    View view = new View();

    public void init(){

        Station station1 = new Station(1,2, new Point(1,1));
        Station station2 = new Station(2,10, new Point(2,2));

        scenario.getStationList().add(station1);
        scenario.getStationList().add(station2);

        Client client = new Client(1);
        client.setFrom(station1);
        client.setTo(station2);
        client.setBike(BikeFactory.spawnBike(Bike.NORMAL));

        scenario.getClientsInTransit().add(client);

        this.clientDepositBike(client);
    }

    /**
     * Example method, presenter is not implemented yet
     */
    public void clientDepositBike(Client client){
        System.out.println("First \n" + client); //FIRST PRINT

        Command clientDepositBike = new ClientDepositBikeUseCase(client, scenario);
        Command nextClientsPickUpBike = new NextClientPicksUpBikeFromStationUseCase(client.getTo(), scenario);

        Invoker invoker = new Invoker();

        invoker.addCommand(clientDepositBike);
        invoker.addCommand(nextClientsPickUpBike);

        try {
            invoker.invoke();
        } catch (UseCaseException e) {
            e.printStackTrace();
        }
        System.out.println("Last \n" + scenario); //LAST PRINT

        view.updateView();
    }

}
