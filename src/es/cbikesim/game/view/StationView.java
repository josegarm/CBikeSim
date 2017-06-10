package es.cbikesim.game.view;

import es.cbikesim.game.contract.Game;
import es.cbikesim.lib.util.Point;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class StationView extends Circle{

    public StationView(Point position, String id, Game.Presenter context){
        super(position.getX(), position.getY(),20.0);

        super.setId(id);
        super.setFill(Color.rgb(0,128,255));
        super.setStroke(Color.rgb(0,76,153));
        super.setStrokeWidth(3);

        super.setOnMouseClicked(e -> {
            context.playSelect();
            context.showDataFromStation(id);
        });
    }

}
