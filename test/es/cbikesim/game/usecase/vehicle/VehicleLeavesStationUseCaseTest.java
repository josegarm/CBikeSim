package es.cbikesim.game.usecase.vehicle;

import es.cbikesim.game.model.Scenario;
import es.cbikesim.game.model.Station;
import es.cbikesim.game.model.Vehicle;
import es.cbikesim.lib.util.Point;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class VehicleLeavesStationUseCaseTest {

    private VehicleLeavesStationUseCase vehicleLeavesStationUseCase;

    private Scenario scenario;
    private Station a, b;
    private Vehicle vehicle;

    @Before
    public void prepareTest() {
        scenario = new Scenario();
        a = new Station("Alfahuir", 6, new Point(110, 50), Station.MORNING);
        b = new Station("Universidades", 0, new Point(880, 45), Station.AFTERNOON);

        scenario.getStationList().addAll(Arrays.asList(a,b));

        vehicle = new Vehicle("vehicle1", 2, a);

        a.getVehicleList().add(vehicle);
    }

    @After
    public void clearTest() {
        scenario = null;
    }

    @Test
    public void testExecuteVehicleLeavesStation() throws Exception {
        vehicleLeavesStationUseCase = new VehicleLeavesStationUseCase(vehicle,b,scenario);
        vehicleLeavesStationUseCase.execute();

        assertNull("Vehicle 'at' parameter should be null! Vehicle has left the station!",vehicle.getAt());
        assertEquals("Vehicle origin station is not equal to perceived origin parameter! Should be the same.",a,vehicle.getFrom());
        assertEquals("Vehicle destination station is not equal to perceived destionation parameter! Should be the same.",b,vehicle.getTo());

        assertTrue("Vehicle is not in transit list. It has left the station! It should be in transit list.",scenario.getVehiclesInTransit().contains(vehicle));
        assertFalse("Station still has vehicle. Should not have a departed vehicle still in the list!",a.getVehicleList().contains(vehicle));
    }

}