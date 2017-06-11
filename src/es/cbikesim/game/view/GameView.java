package es.cbikesim.game.view;

import es.cbikesim.app.CBikeSimState;
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
    private Pane root;

    //ELEMENTS PASSIVE
    private VBox sideBar;
    private Pane utilityPane;
    private StackPane topPane;
    private Text topTitle;
    private StackPane bottomPane;
    private Text bottomTitle;
    private GridPane topGridPane;
    private GridPane bottomGridPane;
    private ColumnConstraints columnConstraints;
    private RowConstraints rowConstraints;

    private HBox hBox;
    private ImageView ui;
    private Pane mapPane;
    private ImageView map;

    //STATIONS
    private List<Circle> stations;


    public GameView(Game.Presenter presenter) {
        this.presenter = presenter;
        this.presenter.setView(this);
    }

    @Override
    public void start() {
        Stage primaryStage = CBikeSimState.getInstance().getPrimaryStage();
        Scene scene = new Scene(createContent());

        primaryStage.setTitle("CBikeSim Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public GridPane getTopPane() {
        return topGridPane;
    }

    @Override
    public GridPane getBottomPane() {
        return bottomGridPane;
    }

    @Override
    public Pane getUtilityPane() {
        return utilityPane;
    }

    @Override
    public Text getTopTitle() {
        return topTitle;
    }

    @Override
    public Text getBottomTitle() {
        return bottomTitle;
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
        utilityPane = new Pane();
        topPane = new StackPane();
        topGridPane = new GridPane();
        columnConstraints = new ColumnConstraints();
        rowConstraints = new RowConstraints();
        bottomPane = new StackPane();
        bottomGridPane = new GridPane();
        hBox = new HBox();
        ui = new ImageView();
        mapPane = new Pane();
        topTitle = new Text();
        bottomTitle = new Text();
    }

    private void setSizes(){
        root.setPrefHeight(720.0);
        root.setPrefWidth(1280.0);

        sideBar.setLayoutX(1018.0);
        sideBar.setLayoutY(33.0);
        sideBar.setPrefHeight(654.0);
        sideBar.setPrefWidth(232.0);

        utilityPane.setPrefHeight(101.0);
        utilityPane.setPrefWidth(232.0);

        topPane.setPrefHeight(44.0);
        topPane.setPrefWidth(232.0);
        topPane.setAlignment(Pos.CENTER);

        topGridPane.setPrefHeight(186.0);
        topGridPane.setPrefWidth(232.0);
        topGridPane.setAlignment(Pos.CENTER);

        columnConstraints.setHgrow(Priority.SOMETIMES);
        columnConstraints.setMinWidth(10.0);
        columnConstraints.setPrefWidth(100.0);

        rowConstraints.setMinHeight(10.0);
        rowConstraints.setPrefHeight(30.0);
        rowConstraints.setVgrow(Priority.SOMETIMES);

        bottomPane.setPrefHeight(44.0);
        bottomPane.setPrefWidth(232.0);

        bottomGridPane.setPrefHeight(175.0);
        bottomGridPane.setPrefWidth(232.0);
        bottomGridPane.setAlignment(Pos.CENTER);

        hBox.setPrefHeight(105.0);
        hBox.setPrefWidth(232.0);

        ui.setFitHeight(720.0);
        ui.setFitWidth(1280.0);

        mapPane.setLayoutX(23.0);
        mapPane.setLayoutY(27.0);
        mapPane.setPrefHeight(667.0);
        mapPane.setPrefWidth(980.0);

        topTitle.setFont(Font.font ("Verdana", 12));
        topTitle.setFill(Color.WHITE);

        bottomTitle.setFont(Font.font ("Verdana", 12));
        bottomTitle.setFill(Color.WHITE);
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

        topGridPane.getColumnConstraints().addAll(
                columnConstraints,
                columnConstraints,
                columnConstraints
        );
        topGridPane.getRowConstraints().addAll(
                rowConstraints,
                rowConstraints,
                rowConstraints
        );

        bottomGridPane.getColumnConstraints().addAll(
                columnConstraints,
                columnConstraints,
                columnConstraints
        );
        bottomGridPane.getRowConstraints().addAll(
                rowConstraints,
                rowConstraints,
                rowConstraints
        );

        sideBar.getChildren().addAll(
                utilityPane,
                topPane,
                topGridPane,
                bottomPane,
                bottomGridPane,
                hBox
        );
        topPane.getChildren().add(topTitle);
        bottomPane.getChildren().add(bottomTitle);


        mapPane.getChildren().addAll(map);

        root.getChildren().addAll(sideBar, ui, mapPane);
    }

}
