package es.cbikesim.game.view;

import es.cbikesim.game.contract.Game;
import es.cbikesim.lib.util.Point;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class ClientView extends Circle{

    public ClientView(Point position, String id, Game.Presenter context){
        super(position.getX(), position.getY(),10.0);

        super.setId(id);
        super.setFill(Color.rgb(255, 170, 0));
        super.setStroke(Color.rgb(150, 90, 0));
        super.setStrokeWidth(3);

        super.setOnMouseClicked(e -> {
            context.playSelect();
            context.showDataFromStation(id);
        });

        super.setOnMouseEntered(e -> {
            //method to show path client would take in between stations
            System.out.println("Hello");
        });
    }

}
