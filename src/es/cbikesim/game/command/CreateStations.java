package es.cbikesim.game.command;

import es.cbikesim.game.model.Scenario;
import es.cbikesim.game.model.Station;
import es.cbikesim.lib.exception.UseCaseException;
import es.cbikesim.lib.pattern.Command;
import es.cbikesim.lib.util.Point;

import java.util.Arrays;

public class CreateStations implements Command {

    private Scenario scenario;

    public CreateStations(Scenario scenario) {
        this.scenario = scenario;
    }

    @Override
    public void execute() throws UseCaseException {
        scenario.getStationList().addAll(Arrays.asList(
                new Station("Alfahuir", 9, new Point(110, 50), Station.MORNING),
                new Station("Universidades", 9, new Point(880, 45), Station.AFTERNOON),
                new Station("Benimaclet", 9, new Point(460, 360), Station.MORNING),
                new Station("Primado Reig", 6, new Point(120, 520), Station.MORNING),
                new Station("Viveros", 6, new Point(885, 515))
        ));
    }
}

