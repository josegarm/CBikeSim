package es.cbikesim.game.usecase.vehicle;

import es.cbikesim.game.model.*;
import es.cbikesim.lib.util.Point;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class VehicleArrivesStationUseCaseTest {

    private VehicleArrivesStationUseCase vehicleArrivesStationUseCase;

    private Scenario scenario;
    private Station a, b;
    private Vehicle vehicle;

    @Before
    public void prepareTest() {
        scenario = new Scenario();
        a = new Station("Alfahuir", 6, new Point(110, 50), Station.MORNING);
        b = new Station("Universidades", 0, new Point(880, 45), Station.AFTERNOON);

        scenario.getStationList().addAll(Arrays.asList(a,b));

        vehicle = new Vehicle("vehicle1", 2, null);

        scenario.getVehiclesInTransit().add(vehicle);
    }

    @After
    public void clearTest() {
        scenario = null;
    }

    @Test
    public void executeVehicleCanArrive() throws Exception {
        vehicle.setFrom(a);
        vehicle.setTo(b);

        vehicleArrivesStationUseCase = new VehicleArrivesStationUseCase(vehicle,scenario);
        vehicleArrivesStationUseCase.execute();

        assertNull(vehicle.getFrom());
        assertNull(vehicle.getTo());

        assertEquals(b, vehicle.getAt());

        assertTrue(vehicle.getAt().getVehicleList().contains(vehicle));
        assertFalse(scenario.getVehiclesInTransit().contains(vehicle));
    }

}