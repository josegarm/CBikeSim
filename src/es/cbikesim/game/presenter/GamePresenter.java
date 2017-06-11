package es.cbikesim.game.presenter;

import es.cbikesim.app.CBikeSimState;
import es.cbikesim.game.command.CreateStations;
import es.cbikesim.game.command.GenerateEasyStationBikes;
import es.cbikesim.game.command.GenerateNormalStationBikes;
import es.cbikesim.game.command.GenerateVehicles;
import es.cbikesim.game.contract.Game;
import es.cbikesim.game.model.*;
import es.cbikesim.game.usecase.client.ClientDepositBikeUseCase;
import es.cbikesim.game.usecase.client.ClientPickUpBikeUseCase;
import es.cbikesim.game.usecase.vehicle.VehicleDepositBikeUseCase;
import es.cbikesim.game.usecase.vehicle.VehiclePickUpBikesUseCase;
import es.cbikesim.game.util.ClientGenerator;
import es.cbikesim.game.util.factories.PathAnimationFactory;
import es.cbikesim.game.view.*;
import es.cbikesim.lib.exception.UseCaseException;
import es.cbikesim.lib.pattern.Command;
import es.cbikesim.lib.pattern.Invoker;
import es.cbikesim.lib.util.Point;
import es.cbikesim.lib.util.Timer;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class GamePresenter implements Game.Presenter {

    public static final int EASY = 0, NORMAL = 1, HARD = 2, CUSTOM = 3;
    public static final String FEW_BIKES = "FEW", NORMAL_BIKES = "NORMAL", MANY_BIKES = "MANY";

    private Game.View view;
    private Scenario scenario;
    private Station selectedStation;
    private Vehicle selectedVehicle;
    private VehicleView selectedVehicleView;

    private MediaPlayer mp, mpSelect;
    private Timer timer;

    private Invoker invoker;
    private ClientGenerator clientGenerator;

    private int difficulty;

    public GamePresenter(){
        scenario = new Scenario();
        invoker = new Invoker();
    }

    @Override
    public void createScenario(int difficulty, int time, String numBikes, int carCapacity) {
        this.difficulty = difficulty;
        prepareTimer(time);

        Command createStations = new CreateStations(scenario);
        Command generateVehicles, generateBikes;

        switch (difficulty){
            case EASY:
                generateVehicles = new GenerateVehicles(scenario, 9);
                generateBikes = new GenerateEasyStationBikes(scenario);
                break;
            case NORMAL:
                generateVehicles = new GenerateVehicles(scenario, 6);
                generateBikes = new GenerateNormalStationBikes(scenario);
                break;
            case HARD:
                generateVehicles = new GenerateVehicles(scenario, 3);
                generateBikes = new GenerateNormalStationBikes(scenario);
                break;
            case CUSTOM:
                generateVehicles = new GenerateVehicles(scenario, carCapacity);
                generateBikes = new GenerateNormalStationBikes(scenario);
                break;
            default:
                generateVehicles = new GenerateVehicles(scenario, 6);
                generateBikes = new GenerateNormalStationBikes(scenario);
        }

        invoker.clear();
        invoker.addCommand(createStations);
        invoker.addCommand(generateVehicles);
        invoker.addCommand(generateBikes);
        try { invoker.invoke(); }
        catch (UseCaseException e) { e.printStackTrace(); }
    }

    @Override
    public void load() {
        prepareMusic();
        paintMap();
        timerStart();
        startClientGenerator();
        if(CBikeSimState.getInstance().getAudio()) mp.play();
    }

    @Override
    public void playSelect() {
        if(CBikeSimState.getInstance().getAudio()) {
            mpSelect.stop();
            mpSelect.play();
        }
    }

    @Override
    public void showDataFromStation(String id) {
        Station station = getSelectedStationWith(id);
        selectedStation = station;
        paintStationPanel(station);
    }

    @Override
    public void showDataFromVehicle(String id) {
        Vehicle vehicle = getSelectedVehicleWith(id);
        selectedVehicle = vehicle;
        selectedStation = vehicle.getFrom();
        paintVehiclePanel(vehicle);
    }

    @Override
    public void notifyNewClient(Client client) {
        ImageView stationClient = view.getClientHasArrivedIcon();
        Platform.runLater(()->{
            stationClient.setLayoutX(client.getFrom().getPosition().getX()-20);
            stationClient.setLayoutY(client.getFrom().getPosition().getY()-60);
            stationClient.setVisible(true);
            stationClient.toFront();
            try {
                wait(1000);
                stationClient.setVisible(false);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });



        //ponemos icono en la estacion client.getFrom()
        //y se elimina al pasar 1 segundo o asi
        //Como se modifica la vista desde un thread deberas usar
        // Platform.runLater(() -> {
        //  metodo que quieras usar
        // });
    }

    @Override
    public void clientPicksUpBike(String idClient, String idBike) {
        Client client = getClientWith(idClient);
        Bike bike = getBikeWith(idBike);

        Command clientPicksUpBike = new ClientPickUpBikeUseCase(client, bike, scenario);

        invoker.clear();
        invoker.addCommand(clientPicksUpBike);
        try {
            invoker.invoke();
            paintStationPanel(selectedStation);
            paintClientInTransit(client);
        }
        catch (UseCaseException e) { System.err.println(e.getMessage()); }
    }

    @Override
    public void clientDepositsBike(String idClient, ClientView clientView) {
        Client client = getClientWith(idClient);

        Command clientDepositBike = new ClientDepositBikeUseCase(client, scenario);

        invoker.clear();
        invoker.addCommand(clientDepositBike);
        try {
            invoker.invoke();
            if(client.getBike() == null) {
                Platform.runLater(() -> view.getMapPane().getChildren().remove(clientView));
                clientView.stop();
            }
            else{
                Platform.runLater(() ->
                    clientView.setCenterX(client.getTo().getPosition().getX() - 25.0)
                );
            }
        }
        catch (UseCaseException e) { System.err.println(e.getMessage()); }
    }

    @Override
    public void vehiclePicksUpBike(String idBike) {
        Vehicle vehicle = selectedVehicle;

        Command vehiclePicksUpBike = new VehiclePickUpBikesUseCase(vehicle,scenario);

        invoker.clear();
        invoker.addCommand(vehiclePicksUpBike);
        try{
            invoker.invoke();
            paintVehicleBikePanel(vehicle);
            paintStationBikePanel(selectedStation);

        } catch (UseCaseException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void vehicleDepositsBike(String idBike) {
        Vehicle vehicle = selectedVehicle;

        Command vehiclePicksUpBike = new VehicleDepositBikeUseCase(vehicle,scenario);

        invoker.clear();
        invoker.addCommand(vehiclePicksUpBike);
        try{
            invoker.invoke();
            paintVehicleBikePanel(vehicle);
            paintStationBikePanel(selectedStation);

        } catch (UseCaseException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void vehicleToAnotherStation(Station to){
        VehicleView vehicleView = selectedVehicleView;
        removeVehicleFromStation( vehicleView.getVehicle().getAt(), vehicleView.getVehicle());
        try{
            Point pointEndStation = new Point(to.getPosition().getX()+30, to.getPosition().getY());
            vehicleView.setAnimation(PathAnimationFactory.pathAnimationFactory(vehicleView.getVehicle().getAt().getPosition(), pointEndStation));
            vehicleView.setDuration(calculateDurationVehicle(vehicleView.getVehicle().getAt(), to));
            new Thread(selectedVehicleView).start();
        }catch (Exception e){
            System.out.println(e.toString());
        }
        addVehicleToStation(to, vehicleView.getVehicle());
        vehicleView.getVehicle().setAt(to);
        vehicleView.getVehicle().setFrom(to);

    }

    @Override
    public void setVehicleView(VehicleView vehicleView) {
        selectedVehicleView = vehicleView;
    }




    @Override
    public void setView(Game.View view) {
        this.view = view;
    }


    private void paintMap(){
        for(Station station : scenario.getStationList()){
            paintStation(station);
            for(Vehicle vehicle : station.getVehicleList()){
                paintVehicle(vehicle);
            }
        }
    }

    private void paintStation(Station station){
        StationView stationView = new StationView(station.getPosition(), station.getId(),this, station);
        view.getMapPane().getChildren().add(stationView);
    }

    private void paintVehicle(Vehicle vehicle){
        vehicle.setFrom(vehicle.getAt());
        Point point = new Point(vehicle.getFrom().getPosition().getX() + 40.0, vehicle.getFrom().getPosition().getY());
        VehicleView vehicleView = new VehicleView(point, vehicle.getId(), this, vehicle);
        view.getMapPane().getChildren().add(vehicleView);
    }

    private void paintClientInTransit(Client client){
        ClientView clientView = new ClientView(client.getFrom().getPosition(), client.getId(),this);
        view.getMapPane().getChildren().add(clientView);
        int seconds = calculateDuration(client);

        clientView.setAnimation(PathAnimationFactory.pathAnimationFactory(client.getFrom().getPosition(), client.getTo().getPosition()));
        clientView.setDuration(seconds);

        new Thread(clientView).start();
    }

    private void removeVehicleFromStation(Station station, Vehicle vehicle){
        try{
            getStation(station.getId()).getVehicleList().remove(vehicle);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    private void addVehicleToStation(Station station, Vehicle vehicle){
        getStation(station.getId()).getVehicleList().add(vehicle);
    }


    private int calculateDuration(Client client){
        int duration;
        double distance =
                Math.abs(client.getTo().getPosition().getX() - client.getFrom().getPosition().getX()) +
                Math.abs(client.getTo().getPosition().getY() - client.getFrom().getPosition().getY());
        duration = (int) distance/35;
        return duration;
    }

    private int calculateDurationVehicle(Station at, Station to){
        int duration;
        double distance =
                Math.abs(to.getPosition().getX() - at.getPosition().getX()) + Math.abs(to.getPosition().getY() - at.getPosition().getY());
        duration = (int) distance/45;
        return duration;
    }

    private void paintStationPanel(Station station){
        paintStationBikePanel(station);
        paintStationClientPanel(station);
    }

    private void paintVehiclePanel(Vehicle vehicle){
        paintVehicleBikePanel(vehicle);
        paintStationBikePanel(vehicle.getFrom());
    }

    private void paintStationBikePanel(Station station){
        view.getTopTitle().setText(station.getId() + " - Bikes Status");
        view.getTopPane().getChildren().clear();

        int count = 0;
        int rows = view.getTopPane().getRowConstraints().size();
        int columns = view.getTopPane().getColumnConstraints().size();
        int numBikes = station.getAvailableBikeList().size();

        Image bikeEmptyImage = new Image(getClass().getResource("/img/bike_empty.png").toExternalForm());
        Image bikeImage = new Image(getClass().getResource("/img/bike.png").toExternalForm());

        for (int row = 0; row < rows; row++){
            for(int column = 0; column < columns && count < station.getMaxCapacity(); column++){
                if(count < numBikes){
                    view.getTopPane().add(new BikeStallView(bikeImage, station.getAvailableBikeList().get(count).getId(), this, BikeStallView.STATION, true, false), column, row);
                } else {
                    view.getTopPane().add(new BikeStallView(bikeEmptyImage, this, BikeStallView.STATION, false, true), column, row);
                }
                count++;
            }
        }
    }

    private void paintStationClientPanel(Station station){
        view.getBottomTitle().setText("Clients Waiting");
        view.getBottomPane().getChildren().clear();

        int count = 0;
        int rows = view.getBottomPane().getRowConstraints().size();
        int columns = view.getBottomPane().getColumnConstraints().size();
        int numClients = station.getClientWaitingToPickUpList().size();

        Image clientEmptyImage = new Image(getClass().getResource("/img/client_empty.png").toExternalForm());
        Image clientImage = new Image(getClass().getResource("/img/client.png").toExternalForm());

        for (int row = 0; row < rows; row++){
            for(int column = 0; column < columns && count < 6; column++){
                if(count < numClients){
                    view.getBottomPane().add(new ClientInStationView(clientImage, station.getClientWaitingToPickUpList().get(count).getId(),this), column, row);
                } else {
                    view.getBottomPane().add(new ClientInStationView(clientEmptyImage), column, row);
                }
                count++;
            }
        }
    }

    private void paintVehicleBikePanel(Vehicle vehicle){
        view.getBottomTitle().setText("Vehicle Bikes");
        view.getBottomPane().getChildren().clear();

        int count = 0;
        int rows = view.getTopPane().getRowConstraints().size();
        int columns = view.getTopPane().getColumnConstraints().size();
        int numBikes = vehicle.getBikeList().size();

        Image bikeEmptyImage = new Image(getClass().getResource("/img/bike_empty.png").toExternalForm());
        Image bikeImage = new Image(getClass().getResource("/img/bike.png").toExternalForm());

        for (int row = 0; row < rows; row++){
            for(int column = 0; column < columns && count < vehicle.getMaxCapacity(); column++){
                if(count < numBikes){
                    view.getBottomPane().add(new BikeStallView(bikeImage, vehicle.getBikeList().get(count).getId(), this, BikeStallView.VEHICLE, true, false), column, row);
                } else {
                    view.getBottomPane().add(new BikeStallView(bikeEmptyImage, this, BikeStallView.VEHICLE, false, true), column, row);
                }
                count++;
            }
        }
    }

    private Station getSelectedStationWith(String id){
        for(Station station : scenario.getStationList()){
            if(station.getId().equals(id)) return station;
        }
        return null;
    }

    private Vehicle getSelectedVehicleWith(String id){
        for(Station station : scenario.getStationList()){
            for(Vehicle vehicle : station.getVehicleList()){
                if (id.equals(vehicle.getId())) return vehicle;
            }
        }
        return null;
    }

    public Client getClientWith(String id){
        for (Client client : scenario.getClientsInTransit()) {
            if(client.getId().equals(id)) return client;
        }
        for (Station station : scenario.getStationList()) {
            for (Client client : station.getClientWaitingToPickUpList()){
                if(client.getId().equals(id)) return client;
            }
            for (Client client : station.getClientWaitingToDepositList()){
                if(client.getId().equals(id)) return client;
            }
        }
        // throw error
        return null;
    }

    public Bike getBikeWith(String id){
        for (Bike bike : selectedStation.getAvailableBikeList()) {
            if(bike.getId().equals(id)) return bike;
        }
        // throw error
        return null;
    }

    public Station getStation(String id){
        for(Station station : scenario.getStationList()){
            if(station.getId().equals(id)) return station;
        }
        //throw error
        return null;
    }


    private void prepareMusic(){
        String pathSelect = getClass().getResource("/music/select.wav").toString();
        String path = getClass().getResource("/music/soundtrack_game.mp3").toString();

        Media mediaSelect = new Media(pathSelect);
        Media media = new Media(path);

        this.mpSelect = new MediaPlayer(mediaSelect);
        this.mp = new MediaPlayer(media);

        this.mpSelect.setVolume(1.0);
    }

    private void prepareTimer(int seconds){
        timer = new Timer(seconds);
    }

    private void startClientGenerator(){
        clientGenerator = new ClientGenerator(scenario,this, 3000);
        clientGenerator.start();
        CBikeSimState.getInstance().getPrimaryStage().setOnCloseRequest(event -> clientGenerator.cancel());
    }

    //DURATION in SECONDS
    private void timerStart(){
        //countdown
        timer.startTimer();
        Label timerText = timer.getTimerLabel();
        timerText.setTranslateX(180);
        view.getUtilityPane().getChildren().addAll(
                timer.getTimerTitle(),
                timerText
        );

    }

}
