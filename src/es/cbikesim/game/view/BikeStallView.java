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
    private int containerType;

    public static final Image EMPTY = new Image(BikeStallView.class.getResource("/img/bike_empty.png").toExternalForm());
    public static final Image NORMAL = new Image(BikeStallView.class.getResource("/img/bike.png").toExternalForm());
    public static final Image ELECTRIC = new Image(BikeStallView.class.getResource("/img/bike_elec.png").toExternalForm());

    public BikeStallView(Image image, String id, Game.Presenter context, int containerType, boolean dragEvents, boolean dropEvents) {
        this(image, context, containerType, dragEvents, dropEvents);
        super.setId(id);
    }

    public BikeStallView(Image image, Game.Presenter context, int containerType, boolean dragEvents, boolean dropEvents) {
        super(image);
        super.setFitWidth(60.0);
        super.setFitHeight(50.0);
        super.setOpacity(0.75);

        GridPane.setHalignment(this, HPos.CENTER);

        this.context = context;
        this.containerType = containerType;

        if (dragEvents) startDragEvents();
        if (dropEvents) startDropEvents();
    }

    private void startDragEvents() {
        super.setOnDragDetected(event -> {
            Dragboard db = BikeStallView.super.startDragAndDrop(TransferMode.ANY);
            db.setDragView(new Image(getClass().getResource("/img/bike_drag.png").toExternalForm()));
            db.setDragViewOffsetX(40);
            db.setDragViewOffsetY(40);

            ClipboardContent content = new ClipboardContent();
            content.putString(BikeStallView.super.getId());
            db.setContent(content);

            event.consume();
        });
    }

    private void startDropEvents() {
        super.setOnDragOver(event -> {
            if (event.getGestureSource() != super.getClass() && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.ANY);
            }
            event.consume();
        });

        super.setOnDragDropped(event -> {
            if (containerType == VEHICLE) {
                context.vehiclePicksUpBike(event.getDragboard().getString());
            } else {
                context.vehicleDepositsBike(event.getDragboard().getString());
            }
        });
    }

}
