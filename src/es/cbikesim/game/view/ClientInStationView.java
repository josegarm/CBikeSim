package es.cbikesim.game.view;

import es.cbikesim.game.contract.Game;
import javafx.geometry.HPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;

public class ClientInStationView extends ImageView {

    public ClientInStationView(Image image, String id, Game.Presenter context) {
        this(image);

        super.setId(id);

        super.setOnMouseClicked(e -> {
            context.playSelect();
        });

        super.setOnDragEntered(event -> {
            super.setImage(new Image(getClass().getResource("/img/client_highlight.png").toExternalForm()));
            event.acceptTransferModes(TransferMode.ANY);
        });

        super.setOnDragExited(event -> {
            super.setImage(new Image(getClass().getResource("/img/client.png").toExternalForm()));
        });

        super.setOnDragOver(event -> {
            if (event.getGestureSource() != super.getClass() && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.ANY);
                event.consume();
            }
        });

        super.setOnDragDropped(event -> {
            //call method use case for client pick up bike
            context.clientPicksUpBike(super.getId(), event.getDragboard().getString());
        });
    }

    public ClientInStationView(Image image) {
        super(image);
        super.setFitWidth(50.0);
        super.setFitHeight(50.0);
        super.setOpacity(0.75);

        GridPane.setHalignment(this, HPos.CENTER);
    }

}
