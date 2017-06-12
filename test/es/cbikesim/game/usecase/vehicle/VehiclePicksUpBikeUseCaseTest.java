package es.cbikesim.game.usecase.vehicle;

import es.cbikesim.game.model.*;
import es.cbikesim.lib.util.Point;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class VehiclePicksUpBikeUseCaseTest {

    private VehiclePicksUpBikeUseCase vehiclePicksUpBikeUseCase;

    private Scenario scenario;
    private Station a, b;
    private Vehicle vehicle;
    private Bike bike;

    @Before
    public void prepareTest() {
        scenario = new Scenario();
        a = new Station("Alfahuir", 6, new Point(110, 50), Station.MORNING);
        b = new Station("Universidades", 6, new Point(880, 45), Station.AFTERNOON);

        scenario.getStationList().addAll(Arrays.asList(a,b));

        bike = new NormalBike("bike1");
    }

    @After
    public void clearTest() {
        scenario = null;
    }

    @Test
    public void testExecuteVehicleCanPickUpBike() throws Exception {
        vehicle = new Vehicle("vehicle1", 6, a);
        a.getVehicleList().add(vehicle);
        a.getAvailableBikeList().add(bike);

        vehiclePicksUpBikeUseCase = new VehiclePicksUpBikeUseCase(vehicle, bike, scenario);
        vehiclePicksUpBikeUseCase.execute();

        assertNull("Vehicle is at station! Destination station (to) should be null!",vehicle.getTo());
        assertNull("Vehicle is at station! Origin station (from) should be null!",vehicle.getFrom());
        assertEquals("Vehicle's current station is not the same as perceived current station! Should be the same.",a, vehicle.getAt());

        assertTrue("Vechicle doesn't have picked up bike! Vehicle should have picked up bike!",vehicle.getBikeList().contains(bike));
        assertFalse("Station has picked up bike! Station should not have bike after vehicle picks it up!",a.getAvailableBikeList().contains(bike));
    }

    @Test
    public void testExecuteVehicleCanNotPicksUpBike() throws Exception {
        vehicle = new Vehicle("vehicle1", 0, a);
        a.getVehicleList().add(vehicle);
        a.getAvailableBikeList().add(bike);

        vehiclePicksUpBikeUseCase = new VehiclePicksUpBikeUseCase(vehicle, bike, scenario);
        vehiclePicksUpBikeUseCase.execute();

        assertNull("Vehicle is at station! Destination station (to) should be null!",vehicle.getTo());
        assertNull("Vehicle is at station! Origin station (from) should be null!",vehicle.getFrom());
        assertEquals("Vehicle's current station is not the same as perceived current station! Should be the same.",a, vehicle.getAt());

        assertFalse("Vehicle has bike after not picking it up! Should not have bike that is still in station!",vehicle.getBikeList().contains(bike));
        assertTrue("Station does not have bike after vehicle doesn't pick it up. Station should still have bike!.",a.getAvailableBikeList().contains(bike));
    }

}