package es.cbikesim.game.view;

import es.cbikesim.game.contract.Game;
import es.cbikesim.lib.util.Point;
import javafx.animation.PathTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class ClientView extends Circle implements Runnable {

    Game.Presenter context;

    private boolean alive;
    private int waitSecond;
    private PathTransition animation;

    public ClientView(Point position, String id, Game.Presenter context) {
        super(position.getX(), position.getY(), 10.0);

        super.setId(id);
        super.setFill(Color.rgb(255, 170, 0));
        super.setStroke(Color.rgb(150, 90, 0));
        super.setStrokeWidth(3);

        this.context = context;

        super.setOnMouseClicked(e -> {
            context.playSelect();
        });

        super.setOnMouseEntered(e -> {
            //method to show path client would take in between stations

        });
    }

    @Override
    public void run() {
        alive = true;
        move();
        while (alive) {
            context.clientDepositsBike(super.getId(), this);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setAnimation(PathTransition animation) {
        this.animation = animation;
        this.animation.setNode(this);
    }

    public void setDuration(int seconds) {
        this.waitSecond = seconds;
        this.animation.setDuration(Duration.seconds(waitSecond));
    }

    public void stop() {
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
