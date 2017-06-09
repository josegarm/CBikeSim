package es.cbikesim.game.view;

import es.cbikesim.game.contract.Game;
import javafx.geometry.HPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    }

    public BikeStallView(Image image){
        super(image);
        super.setFitWidth(60.0);
        super.setFitHeight(50.0);

        GridPane.setHalignment(this, HPos.CENTER);
    }

}
