package es.cbikesim.game.view;

import es.cbikesim.game.contract.Game;
import es.cbikesim.mainMenu.view.MainMenuView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.List;

public class GameView implements Game.View {

    private static final int WIDTH = 1280, HEIGHT = 720;

    private Game.Presenter presenter;
    private Stage primaryStage;
    private MediaPlayer mp;
    private Pane root = new Pane();

    //ELEMENTS PASSIVE
    private VBox sideBar;
    private Pane topUtilityPane;
    private Pane titlePaneBike;
    private GridPane bikeGridPane;
    private ColumnConstraints columnConstraints;
    private ColumnConstraints columnConstraints0;
    private ColumnConstraints columnConstraints1;
    private RowConstraints rowConstraints;
    private RowConstraints rowConstraints0;
    private RowConstraints rowConstraints1;
    private Pane titlePaneClient;
    private GridPane clientGridPane;
    private ColumnConstraints columnConstraints2;
    private ColumnConstraints columnConstraints3;
    private ColumnConstraints columnConstraints4;
    private RowConstraints rowConstraints2;
    private RowConstraints rowConstraints3;
    private RowConstraints rowConstraints4;
    private HBox hBox;
    private ImageView ui;
    private Pane mapPane;
    private ImageView map;

    //STATIONS
    private List<Circle> stations;


    public GameView(Stage primaryStage, Game.Presenter presenter) {
        this.primaryStage = primaryStage;
        this.presenter = presenter;
        this.presenter.setView(this);

        sideBar = new VBox();
        topUtilityPane = new Pane();
        titlePaneBike = new Pane();
        bikeGridPane = new GridPane();
        columnConstraints = new ColumnConstraints();
        columnConstraints0 = new ColumnConstraints();
        columnConstraints1 = new ColumnConstraints();
        rowConstraints = new RowConstraints();
        rowConstraints0 = new RowConstraints();
        rowConstraints1 = new RowConstraints();
        titlePaneClient = new Pane();
        clientGridPane = new GridPane();
        columnConstraints2 = new ColumnConstraints();
        columnConstraints3 = new ColumnConstraints();
        columnConstraints4 = new ColumnConstraints();
        rowConstraints2 = new RowConstraints();
        rowConstraints3 = new RowConstraints();
        rowConstraints4 = new RowConstraints();
        hBox = new HBox();
        ui = new ImageView();
        mapPane = new Pane();


        root.setMaxHeight(720.0);
        root.setMaxWidth(1280.0);
        root.setMinHeight(720.0);
        root.setMinWidth(1280.0);
        root.setPrefHeight(720.0);
        root.setPrefWidth(1280.0);

        sideBar.setLayoutX(1018.0);
        sideBar.setLayoutY(33.0);
        sideBar.setPrefHeight(654.0);
        sideBar.setPrefWidth(232.0);

        topUtilityPane.setPrefHeight(101.0);
        topUtilityPane.setPrefWidth(232.0);

        titlePaneBike.setPrefHeight(44.0);
        titlePaneBike.setPrefWidth(232.0);

        bikeGridPane.setPrefHeight(186.0);
        bikeGridPane.setPrefWidth(232.0);

        columnConstraints.setHgrow(javafx.scene.layout.Priority.SOMETIMES);
        columnConstraints.setMinWidth(10.0);
        columnConstraints.setPrefWidth(100.0);

        columnConstraints0.setHgrow(javafx.scene.layout.Priority.SOMETIMES);
        columnConstraints0.setMinWidth(10.0);
        columnConstraints0.setPrefWidth(100.0);

        columnConstraints1.setHgrow(javafx.scene.layout.Priority.SOMETIMES);
        columnConstraints1.setMinWidth(10.0);
        columnConstraints1.setPrefWidth(100.0);

        rowConstraints.setMinHeight(10.0);
        rowConstraints.setPrefHeight(30.0);
        rowConstraints.setVgrow(javafx.scene.layout.Priority.SOMETIMES);

        rowConstraints0.setMinHeight(10.0);
        rowConstraints0.setPrefHeight(30.0);
        rowConstraints0.setVgrow(javafx.scene.layout.Priority.SOMETIMES);

        rowConstraints1.setMinHeight(10.0);
        rowConstraints1.setPrefHeight(30.0);
        rowConstraints1.setVgrow(javafx.scene.layout.Priority.SOMETIMES);

        titlePaneClient.setPrefHeight(44.0);
        titlePaneClient.setPrefWidth(232.0);

        clientGridPane.setPrefHeight(175.0);
        clientGridPane.setPrefWidth(232.0);

        columnConstraints2.setHgrow(javafx.scene.layout.Priority.SOMETIMES);
        columnConstraints2.setMinWidth(10.0);
        columnConstraints2.setPrefWidth(100.0);

        columnConstraints3.setHgrow(javafx.scene.layout.Priority.SOMETIMES);
        columnConstraints3.setMinWidth(10.0);
        columnConstraints3.setPrefWidth(100.0);

        columnConstraints4.setHgrow(javafx.scene.layout.Priority.SOMETIMES);
        columnConstraints4.setMinWidth(10.0);
        columnConstraints4.setPrefWidth(100.0);

        rowConstraints2.setMinHeight(10.0);
        rowConstraints2.setPrefHeight(30.0);
        rowConstraints2.setVgrow(javafx.scene.layout.Priority.SOMETIMES);

        rowConstraints3.setMinHeight(10.0);
        rowConstraints3.setPrefHeight(30.0);
        rowConstraints3.setVgrow(javafx.scene.layout.Priority.SOMETIMES);

        rowConstraints4.setMinHeight(10.0);
        rowConstraints4.setPrefHeight(30.0);
        rowConstraints4.setVgrow(javafx.scene.layout.Priority.SOMETIMES);

        hBox.setPrefHeight(105.0);
        hBox.setPrefWidth(232.0);

        ui.setFitHeight(720.0);
        ui.setFitWidth(1280.0);

        mapPane.setLayoutX(23.0);
        mapPane.setLayoutY(27.0);
        mapPane.setPrefHeight(667.0);
        mapPane.setPrefWidth(980.0);

        map = new ImageView(new Image(getClass().getResource("/img/map.png").toExternalForm()));
        map.setFitHeight(667.0);
        map.setFitWidth(980.0);


        sideBar.getChildren().add(topUtilityPane);
        sideBar.getChildren().add(titlePaneBike);
        bikeGridPane.getColumnConstraints().add(columnConstraints);
        bikeGridPane.getColumnConstraints().add(columnConstraints0);
        bikeGridPane.getColumnConstraints().add(columnConstraints1);
        bikeGridPane.getRowConstraints().add(rowConstraints);
        bikeGridPane.getRowConstraints().add(rowConstraints0);
        bikeGridPane.getRowConstraints().add(rowConstraints1);
        sideBar.getChildren().add(bikeGridPane);
        sideBar.getChildren().add(titlePaneClient);
        clientGridPane.getColumnConstraints().add(columnConstraints2);
        clientGridPane.getColumnConstraints().add(columnConstraints3);
        clientGridPane.getColumnConstraints().add(columnConstraints4);
        clientGridPane.getRowConstraints().add(rowConstraints2);
        clientGridPane.getRowConstraints().add(rowConstraints3);
        clientGridPane.getRowConstraints().add(rowConstraints4);
        sideBar.getChildren().add(clientGridPane);
        sideBar.getChildren().add(hBox);
        mapPane.getChildren().add(map);


        
    }

    @Override
    public void start() {
        Scene scene = new Scene(createContent());
        prepareMusic();

        this.primaryStage.setTitle("CBike Sim GameView");
        this.primaryStage.setScene(scene);
        this.primaryStage.show();
        mp.play();
    }

    @Override
    public GridPane getBikePane() {
        return bikeGridPane;
    }


    @Override
    public GridPane getClientPane() {
        return clientGridPane;
    }

    @Override
    public Pane getUtilityPane() {
        return topUtilityPane;
    }

    @Override
    public Pane getTitlePaneBike() {
        return titlePaneBike;
    }

    @Override
    public Pane getTitlePaneClient() {
        return titlePaneClient;
    }

    @Override
    public Pane getMapPane(){ return mapPane; }


    private Parent createContent(){
        addBackground();
        root.getChildren().add(sideBar);
        root.getChildren().add(ui);
        root.getChildren().add(mapPane);


        return root;
    }

    private void addBackground() {
        ImageView imageView = new ImageView(new Image(getClass().getResource("/img/ui.jpg").toExternalForm()));
        imageView.setFitWidth(WIDTH);
        imageView.setFitHeight(HEIGHT);

        root.getChildren().add(imageView);
    }

    private void prepareMusic(){
        String path = MainMenuView.class.getResource("/music/soundtrack_game.mp3").toString();
        Media media = new Media(path);
        this.mp = new MediaPlayer(media);
    }



}
