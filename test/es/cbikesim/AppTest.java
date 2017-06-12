package es.cbikesim;

import es.cbikesim.game.usecase.client.ClientDepositBikeUseCaseTest;
import es.cbikesim.game.usecase.client.ClientPickUpBikeUseCaseTest;
import es.cbikesim.game.usecase.vehicle.VehicleArrivesStationUseCaseTest;
import es.cbikesim.game.usecase.vehicle.VehicleDepositsBikeUseCaseTest;
import es.cbikesim.game.usecase.vehicle.VehicleLeavesStationUseCaseTest;
import es.cbikesim.game.usecase.vehicle.VehiclePicksUpBikeUseCaseTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ClientDepositBikeUseCaseTest.class,
        ClientPickUpBikeUseCaseTest.class,
        VehicleLeavesStationUseCaseTest.class,
        VehicleArrivesStationUseCaseTest.class,
        VehicleDepositsBikeUseCaseTest.class,
        VehiclePicksUpBikeUseCaseTest.class
    })
public class AppTest {


}
