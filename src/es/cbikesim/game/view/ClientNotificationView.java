package es.cbikesim.game.view;


import es.cbikesim.lib.util.Point;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ClientNotificationView extends ImageView {

    public ClientNotificationView(Point position) {
        super();
        super.setImage(new Image(getClass().getResource("/img/client_arrived.png").toExternalForm()));
        super.setX(position.getX() - 20);
        super.setY(position.getY() - 60);
    }
}
