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

        assertNull(vehicle.getTo());
        assertNull(vehicle.getFrom());
        assertEquals(a, vehicle.getAt());

        assertFalse(vehicle.getBikeList().contains(bike));
        assertTrue(a.getAvailableBikeList().contains(bike));
    }

    @Test
    public void testExecuteVehicleCanNotDepositBike() throws Exception {
        vehicle = new Vehicle("vehicle1", 5, b);
        b.getVehicleList().add(vehicle);
        vehicle.getBikeList().add(bike);

        vehicleDepositsBikeUseCase = new VehicleDepositsBikeUseCase(vehicle, bike, scenario);
        vehicleDepositsBikeUseCase.execute();

        assertNull(vehicle.getTo());
        assertNull(vehicle.getFrom());
        assertEquals(b, vehicle.getAt());

        assertTrue(vehicle.getBikeList().contains(bike));
        assertFalse(a.getAvailableBikeList().contains(bike));
    }

}