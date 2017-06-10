package es.cbikesim.game.util.factories;


import es.cbikesim.lib.util.Point;
import javafx.animation.PathTransition;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class PathAnimationFactory {


    public static PathTransition pathAnimationFactory(Point startPosition, Point endPosition){

        int xStart = startPosition.getX();
        int yStart = startPosition.getY();
        int xEnd = endPosition.getX();
        int yEnd = endPosition.getY();

        Path calculatedPath = new Path();

        if(xStart == xEnd || yStart == yEnd){
            //Vertical or Horizontal line
            calculatedPath.getElements().add(new MoveTo(xStart, yStart));
            calculatedPath.getElements().add(new LineTo(xEnd,yEnd));
        }  else {
            //Multiple movements needed
            calculatedPath.getElements().add(new MoveTo(xStart, yStart));
            calculatedPath.getElements().add(new LineTo(xStart, yEnd));
            calculatedPath.getElements().add(new LineTo(xEnd, yEnd));
        }

        PathTransition animation = new PathTransition();
        animation.setPath(calculatedPath);
        animation.setCycleCount(1);

        return animation;
    }


}
