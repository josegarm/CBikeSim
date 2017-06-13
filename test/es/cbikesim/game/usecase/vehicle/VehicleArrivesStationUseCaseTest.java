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
    public void testExecuteVehicleArrivesStation() throws Exception {
        vehicle.setFrom(a);
        vehicle.setTo(b);

        vehicleArrivesStationUseCase = new VehicleArrivesStationUseCase(vehicle,scenario);
        vehicleArrivesStationUseCase.execute();

        assertNull("Vehicle still has origin station after arriving. Should be null!",vehicle.getFrom());
        assertNull("Vehicle still has destination station after arriving. Should be null!",vehicle.getTo());
        assertEquals("Vehicle is not at its destination station! Vehicle destination should be the same as the current station!",b, vehicle.getAt());

        assertTrue("Vehicle is not in station list after arriving! Vehicle should be in station vehicle list!",vehicle.getAt().getVehicleList().contains(vehicle));
        assertFalse("Vechicle is still in transit list after arriving to station! Vehicle should not be in transit list.",scenario.getVehiclesInTransit().contains(vehicle));
    }

}