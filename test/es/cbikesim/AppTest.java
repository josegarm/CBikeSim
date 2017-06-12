package es.cbikesim;

import es.cbikesim.game.model.*;
import es.cbikesim.game.usecase.client.ClientDepositBikeUseCaseTest;
import es.cbikesim.game.usecase.vehicle.VehicleArrivesStationUseCaseTest;
import es.cbikesim.lib.util.Point;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.util.Arrays;

@RunWith(Suite.class)
@Suite.SuiteClasses({ClientDepositBikeUseCaseTest.class, VehicleArrivesStationUseCaseTest.class})
public class AppTest {

    private Scenario scenario;

    @Before
    public void prepareTest() {
        scenario = new Scenario();

        Station a = new Station("Alfahuir", 9, new Point(110, 50), Station.MORNING),
                b = new Station("Universidades", 9, new Point(880, 45), Station.AFTERNOON),
                c = new Station("Viveros", 6, new Point(885, 515));

        scenario.getStationList().addAll(Arrays.asList(a,b,c));

        Vehicle vehicle = new Vehicle("vehicle1", 6, a);
        Bike normalBike = new NormalBike("bike1");
        Bike electricBike = new ElectricBike("bike2");

        a.getVehicleList().add(vehicle);

        a.getAvailableBikeList().add(normalBike);
        a.getAvailableBikeList().add(electricBike);
    }

}
