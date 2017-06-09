package es.cbikesim.game.view;

import es.cbikesim.game.contract.Game;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;

public class GameView implements Game.View {

    private static final int WIDTH = 1280, HEIGHT = 720;

    private Game.Presenter presenter;
    private Stage primaryStage;
    private Pane root;

    //ELEMENTS PASSIVE
    private VBox sideBar;
    private Pane topUtilityPane;
    private AnchorPane topPane;
    private GridPane bikeGridPane;
    private Pane titlePaneClient;
    private GridPane clientGridPane;
    private ColumnConstraints columnConstraints;
    private RowConstraints rowConstraints;
    private HBox hBox;
    private ImageView ui;
    private Pane mapPane;
    private ImageView map;
    private Text topTitle;

    //STATIONS
    private List<Circle> stations;


    public GameView(Stage primaryStage, Game.Presenter presenter) {
        this.primaryStage = primaryStage;
        this.presenter = presenter;
        this.presenter.setView(this);
    }

    @Override
    public void start() {
        Scene scene = new Scene(createContent());
        this.primaryStage.setTitle("CBike Sim GameView");
        this.primaryStage.setScene(scene);
        this.primaryStage.show();
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
    public Text getTopTitle() {
        return topTitle;
    }

    @Override
    public Pane getTitlePaneClient() {
        return titlePaneClient;
    }

    @Override
    public Pane getMapPane(){ return mapPane; }


    private Parent createContent(){
        initComponents();
        setSizes();
        addBackground();
        addMap();
        addComponents();
        presenter.load();
        return root;
    }

    private void initComponents(){
        root = new Pane();
        sideBar = new VBox();
        topUtilityPane = new Pane();
        topPane = new AnchorPane();
        bikeGridPane = new GridPane();
        columnConstraints = new ColumnConstraints();
        rowConstraints = new RowConstraints();
        titlePaneClient = new Pane();
        clientGridPane = new GridPane();
        hBox = new HBox();
        ui = new ImageView();
        mapPane = new Pane();
        topTitle = new Text();

    }

    private void setSizes(){
        root.setPrefHeight(720.0);
        root.setPrefWidth(1280.0);

        sideBar.setLayoutX(1018.0);
        sideBar.setLayoutY(33.0);
        sideBar.setPrefHeight(654.0);
        sideBar.setPrefWidth(232.0);

        topUtilityPane.setPrefHeight(101.0);
        topUtilityPane.setPrefWidth(232.0);

        topPane.setPrefHeight(44.0);
        topPane.setPrefWidth(232.0);


        bikeGridPane.setPrefHeight(186.0);
        bikeGridPane.setPrefWidth(232.0);
        bikeGridPane.setAlignment(Pos.CENTER);

        columnConstraints.setHgrow(Priority.SOMETIMES);
        columnConstraints.setMinWidth(10.0);
        columnConstraints.setPrefWidth(100.0);

        rowConstraints.setMinHeight(10.0);
        rowConstraints.setPrefHeight(30.0);
        rowConstraints.setVgrow(Priority.SOMETIMES);

        titlePaneClient.setPrefHeight(44.0);
        titlePaneClient.setPrefWidth(232.0);

        clientGridPane.setPrefHeight(175.0);
        clientGridPane.setPrefWidth(232.0);
        clientGridPane.setAlignment(Pos.CENTER);

        hBox.setPrefHeight(105.0);
        hBox.setPrefWidth(232.0);

        ui.setFitHeight(720.0);
        ui.setFitWidth(1280.0);

        mapPane.setLayoutX(23.0);
        mapPane.setLayoutY(27.0);
        mapPane.setPrefHeight(667.0);
        mapPane.setPrefWidth(980.0);

        topTitle.setTranslateX(45);
        topTitle.setTranslateY(20);
        topTitle.setFont(Font.loadFont(getClass().getResource("/font/Penumbra-HalfSerif-Std_35114.ttf").toExternalForm(), 14));
        topTitle.setFill(Color.WHITE);
    }

    private void addBackground() {
        ImageView imageView = new ImageView(new Image(getClass().getResource("/img/ui.jpg").toExternalForm()));
        imageView.setFitWidth(WIDTH);
        imageView.setFitHeight(HEIGHT);
        root.getChildren().add(imageView);
    }

    private void addMap(){
        map = new ImageView(new Image(getClass().getResource("/img/map.png").toExternalForm()));
        map.setFitHeight(667.0);
        map.setFitWidth(980.0);
    }

    private void addComponents(){

        bikeGridPane.getColumnConstraints().addAll(
                columnConstraints,
                columnConstraints,
                columnConstraints
        );
        bikeGridPane.getRowConstraints().addAll(
                rowConstraints,
                rowConstraints,
                rowConstraints
        );

        clientGridPane.getColumnConstraints().addAll(
                columnConstraints,
                columnConstraints,
                columnConstraints
        );
        clientGridPane.getRowConstraints().addAll(
                rowConstraints,
                rowConstraints,
                rowConstraints
        );

        sideBar.getChildren().addAll(
                topUtilityPane,
                topPane,
                bikeGridPane,
                titlePaneClient,
                clientGridPane,
                hBox
        );
        topPane.getChildren().add(topTitle);

        mapPane.getChildren().addAll(map);

        root.getChildren().addAll(sideBar, ui, mapPane);
    }

}
