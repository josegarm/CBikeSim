package es.cbikesim.game.view;

import es.cbikesim.game.contract.Game;
import es.cbikesim.game.model.Client;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;

public class ClientListView extends ImageView{

    public ClientListView(Image image, String id, Game.Presenter context){
        this(image);

        super.setId(id);

        super.setOnMouseClicked(e -> {
            context.playSelect();
            System.out.println(id);
        });

        super.setOnMouseEntered(e -> {
            //method to show path client would take in between stations
        });

        super.setOnDragEntered(e -> {
            super.setImage(new Image(getClass().getResource("/img/client_highlight.png").toExternalForm()));
            e.acceptTransferModes(TransferMode.ANY);
        });

        super.setOnDragExited(e -> {
            super.setImage(new Image(getClass().getResource("/img/client.png").toExternalForm()));
        });

        super.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (event.getGestureSource() != super.getClass() &&
                        event.getDragboard().hasString()) {
                    /* allow for both copying and moving, whatever user chooses */
                    event.acceptTransferModes(TransferMode.ANY);
                    System.out.println("Drag source different from own, accept events");
                }

                event.consume();
            }
        });

        super.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                System.out.println("Bike dropped on client");
                //call method use case for client pick up bike
            }
        });
    }

    public ClientListView(Image image){
        super(image);
        super.setFitWidth(50.0);
        super.setFitHeight(50.0);
        super.setOpacity(0.75);

        GridPane.setHalignment(this, HPos.CENTER);
    }

}
