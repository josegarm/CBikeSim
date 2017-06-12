package es.cbikesim.game.usecase.vehicle;

import es.cbikesim.game.model.*;
import es.cbikesim.lib.util.Point;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class VehicleDepositsBikeUseCaseTest {

    private VehicleDepositsBikeUseCase vehicleDepositsBikeUseCase;

    private Scenario scenario;
    private Station a, b;
    private Vehicle vehicle;
    private Bike bike;

    @Before
    public void prepareTest() {
        scenario = new Scenario();
        a = new Station("Alfahuir", 6, new Point(110, 50), Station.MORNING);
        b = new Station("Universidades", 0, new Point(880, 45), Station.AFTERNOON);

        scenario.getStationList().addAll(Arrays.asList(a,b));

        bike = new NormalBike("bike1");
    }

    @After
    public void clearTest() {
        scenario = null;
    }

    @Test
    public void testExecuteVehicleCanDepositBike() throws Exception {
        vehicle = new Vehicle("vehicle1", 5, a);
        a.getVehicleList().add(vehicle);
        vehicle.getBikeList().add(bike);

        vehicleDepositsBikeUseCase = new VehicleDepositsBikeUseCase(vehicle, bike, scenario);
        vehicleDepositsBikeUseCase.execute();

        assertNull("Vehicle is at station! Destination station (to) should be null!",vehicle.getTo());
        assertNull("Vehicle is at station! Origin station (from) should be null!",vehicle.getFrom());
        assertEquals("Vehicle's current station is not the same as perceived current station! Should be the same.",a, vehicle.getAt());

        assertFalse("Vehicle has deposited bike! The bike should not be in the vehicle!",vehicle.getBikeList().contains(bike));
        assertTrue("Station bike list does not have deposited bike! Should have the deposited bike.",a.getAvailableBikeList().contains(bike));
    }

    @Test
    public void testExecuteVehicleCanNotDepositBike() throws Exception {
        vehicle = new Vehicle("vehicle1", 5, b);
        b.getVehicleList().add(vehicle);
        vehicle.getBikeList().add(bike);

        vehicleDepositsBikeUseCase = new VehicleDepositsBikeUseCase(vehicle, bike, scenario);
        vehicleDepositsBikeUseCase.execute();

        assertNull("Vehicle is at station! Destination station (to) should be null!",vehicle.getTo());
        assertNull("Vehicle is at station! Origin station (from) should be null!",vehicle.getFrom());
        assertEquals("Vehicle's current station is not the same as perceived current station! Should be the same.",b, vehicle.getAt());

        assertTrue("Vehicle has not deposited bike and bike is not in vehicle. Bike should still be in vehicle!",vehicle.getBikeList().contains(bike));
        assertFalse("Station has bike that was not deposited. Station should not have bike!",a.getAvailableBikeList().contains(bike));
    }

}