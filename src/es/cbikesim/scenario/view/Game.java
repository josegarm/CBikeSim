package es.cbikesim.scenario.view;


import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.*;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.Color;

public class Game extends Application{

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

    private Pane root = new Pane();
    private Canvas canvas = new Canvas();
    private VBox rightSideBar = new VBox(-5);
    private Line line;


    private Parent createContent(){
        //addBackground();
        addLine(WIDTH - 400, 0);

        return root;
    }

    private void addBackground() {
        ImageView imageView = new ImageView(new javafx.scene.image.Image(getClass().getResource("/img/bicycle_wallpaper.jpg").toExternalForm()));
        imageView.setFitWidth(WIDTH);
        imageView.setFitHeight(HEIGHT);

        root.getChildren().add(imageView);
    }

    private void addLine(double x, double y) {
        line = new Line(x, y, x, y + HEIGHT);
        line.setStrokeWidth(3);
        line.setStroke(javafx.scene.paint.Color.color(1, 1, 1, 0.75));
        line.setEffect(new DropShadow(5, javafx.scene.paint.Color.BLACK));
        line.setScaleY(0);

        root.getChildren().add(line);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());
        primaryStage.setTitle("CBike Sim Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
