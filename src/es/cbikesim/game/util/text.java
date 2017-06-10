package es.cbikesim.game.util;

import com.sun.corba.se.impl.orbutil.graph.Node;
import com.sun.javafx.geom.Path2D;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.event.TreeModelEvent;
import java.util.List;


public class text extends Application {


    @Override
    public void start(final Stage stage) throws Exception
    {
        Pane root = new Pane();
        Scene scene = new Scene(root, 600, 400, Color.GHOSTWHITE);
        stage.setScene(scene);
        stage.setTitle("JavaFX 2 Animations");

        Circle circle = new Circle(10);
        root.getChildren().add(circle);



        Path path = new Path();
        path.getElements().add(new MoveTo(10.0,10.0));
        path.getElements().add(new LineTo(10.0,80.0));
        path.getElements().add(new LineTo(80.0,80.0));

        PathTransition transition = new PathTransition();
        transition.setNode(circle);
        transition.setDuration(Duration.seconds(3));
        transition.setPath(path);
        transition.setCycleCount(PathTransition.INDEFINITE);
        stage.show();
        transition.play();
    }

}