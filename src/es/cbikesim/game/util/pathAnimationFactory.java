package es.cbikesim.game.util;


import javafx.animation.PathTransition;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

public class pathAnimationFactory {


    public static PathTransition pathAnimationFactory(int xStart, int yStart, int xEnd, int yEnd, int duration){
        Path calculatedPath = new Path();
        if(xStart == xEnd || yStart == yEnd){
            //Vertical or Horizontal line
            calculatedPath.getElements().add(new MoveTo(xStart, yStart));
            calculatedPath.getElements().add(new LineTo(xEnd,yEnd));
        }else{
            //Multiple movements needed
            calculatedPath.getElements().add(new MoveTo(xStart, yStart));
            calculatedPath.getElements().add(new LineTo(xStart, yEnd));
            calculatedPath.getElements().add(new LineTo(xEnd, yEnd));
        }
        PathTransition animation = new PathTransition();
        animation.setDuration(Duration.seconds(duration));
        animation.setPath(calculatedPath);
        animation.setCycleCount(1);

        return animation;
    }


}
