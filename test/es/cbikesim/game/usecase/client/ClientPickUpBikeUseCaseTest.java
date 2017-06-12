package es.cbikesim.game.usecase.client;

import es.cbikesim.game.model.*;
import es.cbikesim.lib.util.Point;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;


public class ClientPickUpBikeUseCaseTest {

    private ClientPickUpBikeUseCase clientPickUpBikeUseCase;

    private Scenario scenario;
    private Station a, b;
    private Client client;
    private Bike bike;

    @Before
    public void prepareTest() {
        scenario = new Scenario();
        a = new Station("Alfahuir", 6, new Point(110, 50), Station.MORNING);
        b = new Station("Universidades", 6, new Point(880, 45), Station.AFTERNOON);

        scenario.getStationList().addAll(Arrays.asList(a,b));

        bike = new NormalBike("bike1");
        client = new Client("client1");

        a.getAvailableBikeList().add(bike);
        a.getClientWaitingToPickUpList().add(client);
    }

    @After
    public void clearTest() {
        scenario = null;
    }

    @Test
    public void testExecuteClientPickUpBike() throws Exception {
        client.setFrom(a);
        client.setTo(b);

        clientPickUpBikeUseCase = new ClientPickUpBikeUseCase(client,bike,scenario);
        clientPickUpBikeUseCase.execute();

        assertEquals("Client does not have the bike he picked up. Bike chosen and the bike picked up should be the same.",bike,client.getBike());

        assertFalse("Bike that client picked up is still in station bike list! The bike should not be in the list!",a.getAvailableBikeList().contains(bike));
        assertFalse("Client is still in waiting list after picking up his bike. Client should not be in the list!",a.getClientWaitingToPickUpList().contains(client));

        assertTrue("Client is not in transit list! After picking up bike, client has to be in transit list!",scenario.getClientsInTransit().contains(client));
    }

}