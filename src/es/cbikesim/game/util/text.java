package es.cbikesim.game.util;

import com.sun.corba.se.impl.orbutil.graph.Node;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.event.TreeModelEvent;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class text implements Initializable {


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Circle circle = new Circle(100);
        Rectangle rectangle = new Rectangle(100, 100);


        PathTransition transition = new PathTransition();
        transition.setDuration(Duration.seconds(3));
        transition.setPath(rectangle);
        transition.setCycleCount(PathTransition.INDEFINITE);
        transition.play();
    }
}