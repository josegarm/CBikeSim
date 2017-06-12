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

        assertNull(client.getBike());
        assertNull(client.getTo());

        assertNotNull(client.getFrom());

        assertTrue(client.getFrom().getAvailableBikeList().contains(bike));
        assertFalse(scenario.getClientsInTransit().contains(client));
    }

    @Test
    public void testExecuteClientCanNotDepositBike() throws Exception {
        client.setFrom(a);
        client.setTo(b);

        clientDepositBikeUseCase = new ClientDepositBikeUseCase(client,scenario);
        clientDepositBikeUseCase.execute();

        assertNotNull(client.getBike());
        assertNotNull(client.getTo());
        assertNotNull(client.getFrom());

        assertTrue(client.getTo().getClientWaitingToDepositList().contains(client));
        assertFalse(scenario.getClientsInTransit().contains(client));
    }

}