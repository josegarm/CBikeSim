package es.cbikesim.game.view;

import es.cbikesim.game.contract.Game;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;

public class BikeStallView extends ImageView {

    public static final int STATION = 0, VEHICLE = 1;

    private Game.Presenter context;
    private int type;

    public BikeStallView(Image image, String id, Game.Presenter context, int type, boolean dragEvents, boolean dropEvents) {
        this(image, context, type, dragEvents, dropEvents);
        super.setId(id);
    }

    public BikeStallView(Image image, Game.Presenter context, int type, boolean dragEvents, boolean dropEvents) {
        super(image);
        super.setFitWidth(60.0);
        super.setFitHeight(50.0);
        super.setOpacity(0.75);

        GridPane.setHalignment(this, HPos.CENTER);

        this.context = context;
        this.type = type;

        if (dragEvents) startDragEvents();
        if (dropEvents) startDropEvents();
    }

    public void startDragEvents() {
        super.setOnDragDetected(
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        Dragboard db = BikeStallView.super.startDragAndDrop(TransferMode.ANY);
                        db.setDragView(new Image(getClass().getResource("/img/bike_drag.png").toExternalForm()));
                        db.setDragViewOffsetX(40);
                        db.setDragViewOffsetY(40);

                        ClipboardContent content = new ClipboardContent();
                        content.putString(BikeStallView.super.getId());
                        db.setContent(content);

                        event.consume();
                    }
                }
        );
    }

    public void startDropEvents() {
        super.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (event.getGestureSource() != super.getClass() && event.getDragboard().hasString()) {
                    event.acceptTransferModes(TransferMode.ANY);
                }

                event.consume();
            }
        });

        super.setOnDragDropped(event -> {
            if (type == VEHICLE) {
                context.vehiclePicksUpBike(event.getDragboard().getString());
            } else {
                context.vehicleDepositsBike(event.getDragboard().getString());
            }
        });
    }

}
