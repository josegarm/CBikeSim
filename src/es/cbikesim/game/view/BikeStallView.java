package es.cbikesim.game.view;

import es.cbikesim.game.contract.Game;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;

public class BikeStallView extends ImageView{

    private Game.Presenter context;

    public BikeStallView(Image image, String id, Game.Presenter context){
        this(image);

        this.context = context;
        super.setId(id);

        super.setOnMouseClicked(e -> {
            context.playSelect();
            System.out.println(id);
        });

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

                        //Change cursor
                        //getScene().getCursor().DragDropEffects.None;
                        event.consume();
                    }
                }
        );

    }

    public BikeStallView(Image image){
        super(image);
        super.setFitWidth(60.0);
        super.setFitHeight(50.0);
        super.setOpacity(0.75);

        GridPane.setHalignment(this, HPos.CENTER);
    }


}
