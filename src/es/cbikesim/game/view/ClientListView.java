package es.cbikesim.game.view;

import es.cbikesim.game.contract.Game;
import javafx.geometry.HPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class ClientListView extends ImageView{

    public ClientListView(Image image, String id, Game.Presenter context){
        this(image);

        super.setId(id);

        super.setOnMouseClicked(e -> {
            context.playSelect();
            System.out.println(id);
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
