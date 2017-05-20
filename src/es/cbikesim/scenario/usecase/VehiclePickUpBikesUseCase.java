package es.cbikesim.scenario.usecase;


import es.cbikesim.lib.pattern.Command;
import es.cbikesim.scenario.model.Scenario;
import es.cbikesim.scenario.model.Station;
import es.cbikesim.scenario.model.Vehicle;

/**
 * Use Case: The vehicle pick up bikes in the current station and go to other station
 */
public class VehiclePickUpBikesUseCase implements Command{

    private Vehicle vehicle;
    private Station to;
    private int bikesToPickUp;
    private Scenario scenario;

    public VehiclePickUpBikesUseCase(Vehicle vehicle, Station to, int bikesToPickUp, Scenario scenario) {
        this.vehicle = vehicle;
        this.to = to;
        this.bikesToPickUp = bikesToPickUp;
        this.scenario = scenario;
    }

    @Override
    public void execute(){
        Station currentStation = vehicle.getFrom();

        if(currentStation.getAvailableBikeList().size() - bikesToPickUp >= 0){
            for ( ; bikesToPickUp > 0; bikesToPickUp-- ){
                vehicle.getBikeList().add(currentStation.getAvailableBikeList().remove(0));
            }
            vehicle.setTo(to);
            currentStation.getVehicleList().remove(vehicle);
            scenario.getVehiclesInTransit().add(vehicle);
        }
    }
}