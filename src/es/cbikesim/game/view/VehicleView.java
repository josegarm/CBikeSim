package es.cbikesim.game.view;

import es.cbikesim.game.contract.Game;
import es.cbikesim.lib.util.Point;
import es.cbikesim.lib.util.StopRun;
import javafx.animation.PathTransition;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class VehicleView extends Rectangle implements Runnable, StopRun {

    Game.Presenter context;

    private boolean alive;
    private int waitSecond;
    private PathTransition animation;

    public VehicleView(Point position, String id, Game.Presenter context) {
        super(25.0, 25.0);

        super.setX(position.getX());
        super.setY(position.getY());
        setCenterX(position.getX());
        setCenterY(position.getY());

        super.setId(id);
        super.setFill(Color.rgb(1, 255, 171));
        super.setStroke(Color.rgb(0, 150, 73));
        super.setStrokeWidth(3);

        this.context = context;

        super.setOnMouseClicked(event -> {
            context.playSelect();
            context.showDataFromVehicle(id);
            context.setVehicleView(this);
        });

        super.setOnDragDetected(event -> {
            context.showDataFromVehicle(id);
            context.setVehicleView(this);

            this.setStroke(Color.rgb(255, 255, 255));
            Dragboard db = VehicleView.super.startDragAndDrop(TransferMode.ANY);
            db.setDragView(new Image(getClass().getResource("/img/pointer.png").toExternalForm()));
            db.setDragViewOffsetY(40);
            db.setDragViewOffsetX(15);

            ClipboardContent content = new ClipboardContent();
            content.putString(VehicleView.super.getId());
            db.setContent(content);

            event.consume();
        });

        super.setOnDragDone(event -> {
            super.setStroke(Color.rgb(0, 150, 73));
        });
    }

    @Override
    public void run() {
        alive = true;
        move();
        context.vehicleArriveStation(super.getId(), this);
    }

    public void setCenterX(double x) {
        super.setX(x - super.getWidth() / 2);
    }

    public void setCenterY(double y) {
        super.setY(y - super.getHeight() / 2);
    }

    public void setAnimation(PathTransition animation) {
        this.animation = animation;
        this.animation.setNode(this);
    }

    public void setDuration(int seconds) {
        this.waitSecond = seconds;
        this.animation.setDuration(Duration.seconds(waitSecond));
    }

    @Override
    public void stopRun() {
        this.alive = false;
    }

    private void move() {
        animation.play();
        try {
            Thread.sleep((long) Duration.seconds(waitSecond).toMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
