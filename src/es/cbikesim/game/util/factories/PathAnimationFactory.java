package es.cbikesim.game.util.factories;


import es.cbikesim.lib.util.Point;
import javafx.animation.PathTransition;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class PathAnimationFactory {


    public static PathTransition pathAnimationFactory(Point startPosition, Point endPosition){
        Path path = new Path();

        addPathSection(path, startPosition, endPosition);

        PathTransition animation = new PathTransition();
        animation.setPath(path);
        animation.setCycleCount(1);

        return animation;
    }

    public static PathTransition pathAnimationFactory(Point startPosition, Point secondPosition, Point thirdPosition, Point endPosition){
        Path path = new Path();

        addPathSection(path, startPosition, secondPosition);
        addPathSection(path, secondPosition, thirdPosition);
        addPathSection(path, thirdPosition, endPosition);

        return generateTransition(path);
    }

    private static Path addPathSection(Path path, Point startPosition, Point endPosition){
        double xStart = startPosition.getX();
        double yStart = startPosition.getY();
        double xEnd = endPosition.getX();
        double yEnd = endPosition.getY();

        if(xStart == xEnd || yStart == yEnd){
            //Vertical or Horizontal line
            path.getElements().add(new MoveTo(xStart, yStart));
            path.getElements().add(new LineTo(xEnd,yEnd));
        }  else {
            //Multiple movements needed
            path.getElements().add(new MoveTo(xStart, yStart));
            path.getElements().add(new LineTo(xStart, yEnd));
            path.getElements().add(new LineTo(xEnd, yEnd));
        }

        return path;
    }

    private static PathTransition generateTransition(Path path){
        PathTransition animation = new PathTransition();
        animation.setPath(path);
        animation.setCycleCount(1);
        return animation;
    }


}
