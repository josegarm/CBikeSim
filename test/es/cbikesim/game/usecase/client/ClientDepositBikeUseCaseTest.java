package es.cbikesim.game.usecase.client;

import es.cbikesim.game.model.*;
import es.cbikesim.lib.util.Point;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;


public class ClientDepositBikeUseCaseTest {

    private ClientDepositBikeUseCase clientDepositBikeUseCase;

    private Scenario scenario;
    private Station a, b;
    private Client client;
    private Bike bike;

    @Before
    public void prepareTest() {
        scenario = new Scenario();
        a = new Station("Alfahuir", 6, new Point(110, 50), Station.MORNING);
        b = new Station("Universidades", 0, new Point(880, 45), Station.AFTERNOON);

        scenario.getStationList().addAll(Arrays.asList(a,b));

        bike = new NormalBike("bike1");
        client = new Client("client1");

        client.setBike(bike);
        scenario.getClientsInTransit().add(client);
    }

    @After
    public void clearTest() {
        scenario = null;
    }

    @Test
    public void testExecuteClientCanDepositBike() throws Exception {
        client.setFrom(b);
        client.setTo(a);

        clientDepositBikeUseCase = new ClientDepositBikeUseCase(client,scenario);
        clientDepositBikeUseCase.execute();

        assertNull("Client still has bike after depositing it. Should be null.",client.getBike());
        assertNull("Client still has 'to' parameter after arriving. Should be null.",client.getTo());

        assertEquals("Clients current station is not equal to the destination station. Destination and current should be the same.",a, client.getFrom());

        assertTrue("Clients bike has not been deposited in the station. Clients bike should be in station bike list.",client.getFrom().getAvailableBikeList().contains(bike));
        assertFalse("Client still in transit after arriving. Client should have dissapeared from transit list.",scenario.getClientsInTransit().contains(client));
    }

    @Test
    public void testExecuteClientCanNotDepositBike() throws Exception {
        client.setFrom(a);
        client.setTo(b);

        clientDepositBikeUseCase = new ClientDepositBikeUseCase(client,scenario);
        clientDepositBikeUseCase.execute();

        assertNotNull("Client does not have bike. He can't deposit it yet he doesn't have it. He should have it because he hasn't deposited the bike yet!",client.getBike());
        assertNotNull("Client doesn't have destination station. He should have it because he hasn't deposited the bike yet!",client.getTo());
        assertNotNull("Client origin station is null. This parameter should still exist because he hasn't deposited the bike yet!",client.getFrom());

        assertTrue(client.getTo().getClientWaitingToDepositList().contains(client));
        assertFalse(scenario.getClientsInTransit().contains(client));
    }

}