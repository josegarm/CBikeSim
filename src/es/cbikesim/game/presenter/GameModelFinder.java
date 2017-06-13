package es.cbikesim.game.presenter;

import es.cbikesim.game.model.*;
import es.cbikesim.lib.exception.UseCaseException;

public class GameModelFinder {

    private Scenario scenario;

    public GameModelFinder(Scenario scenario) {
        this.scenario = scenario;
    }

    public Station getStationWith(String id) throws UseCaseException {
        for (Station station : scenario.getStationList()) {
            if (station.getId().equals(id)) return station;
        }
        throw new UseCaseException("Station with id " + id + " not found");
    }

    public Vehicle getVehicleWith(String id) throws UseCaseException {
        for (Vehicle vehicle : scenario.getVehiclesInTransit()) {
            if (id.equals(vehicle.getId())) return vehicle;
        }
        for (Station station : scenario.getStationList()) {
            for (Vehicle vehicle : station.getVehicleList()) {
                if (id.equals(vehicle.getId())) return vehicle;
            }
        }
        throw new UseCaseException("Vehicle with id " + id + " not found");
    }

    public Client getClientWith(String id) throws UseCaseException {
        for (Client client : scenario.getClientsInTransit()) {
            if (client.getId().equals(id)) return client;
        }
        for (Station station : scenario.getStationList()) {
            for (Client client : station.getClientWaitingToPickUpList()) {
                if (client.getId().equals(id)) return client;
            }
            for (Client client : station.getClientWaitingToDepositList()) {
                if (client.getId().equals(id)) return client;
            }
        }
        throw new UseCaseException("Client with id " + id + " not found");
    }

    public Bike getBikeWith(String id) throws UseCaseException {
        for (Station station : scenario.getStationList()) {
            for (Bike bike : station.getAvailableBikeList()) {
                if (bike.getId().equals(id)) return bike;
            }
            for (Vehicle vehicle : station.getVehicleList()) {
                for (Bike bike : vehicle.getBikeList()) {
                    if (bike.getId().equals(id)) return bike;
                }
            }
        }

        for (Vehicle vehicle : scenario.getVehiclesInTransit()) {
            for (Bike bike : vehicle.getBikeList()) {
                if (bike.getId().equals(id)) return bike;
            }
        }

        for (Client client : scenario.getClientsInTransit()){
            if(client.getBike().getId().equals(id)) return client.getBike();
        }
        throw new UseCaseException("Bike with id " + id + " not found");
    }

}