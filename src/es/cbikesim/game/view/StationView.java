package es.cbikesim.game.view;

import es.cbikesim.game.contract.Game;
import es.cbikesim.game.model.Station;
import es.cbikesim.lib.util.Point;
import javafx.event.EventHandler;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class StationView extends Circle{

    Station station;

    public StationView(Point position, String id, Game.Presenter context, Station station){
        super(position.getX(), position.getY(),20.0);

        super.setId(id);
        super.setFill(Color.rgb(0,128,255));
        super.setStroke(Color.rgb(0,76,153));
        super.setStrokeWidth(3);

        super.setOnMouseClicked(e -> {
            context.playSelect();
            context.showDataFromStation(id);
        });

        super.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (event.getGestureSource() != super.getClass() && event.getDragboard().hasString()) {
                    /* allow for both copying and moving, whatever user chooses */
                    event.acceptTransferModes(TransferMode.ANY);
                }

                event.consume();
            }
        });

        super.setOnDragDropped(event -> {
            context.vehicleToAnotherStation(station);
        });

        this.station = station;
    }

}
