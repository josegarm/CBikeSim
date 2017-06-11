package es.cbikesim.lib.util;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Timer {

    private Integer START_TIME;
    private Timeline timeline;
    private Label timerLabel = new Label();
    private Label timerTitle = new Label();
    private IntegerProperty timeSeconds;


    public Timer(int seconds) {
        START_TIME = seconds;
        timeSeconds = new SimpleIntegerProperty(START_TIME);
        // Bind the timerLabel text property to the timeSeconds property
        timerLabel.textProperty().bind(timeSeconds.asString());
        timerTitle.setText("Time: ");

        //FORMAT
        timerLabel.setTextFill(Color.WHITE);
        timerLabel.setStyle("-fx-font-size: 2em;");

        timerTitle.setTextFill(Color.WHITE);
        timerTitle.setStyle("-fx-font-size: 2em;");
    }

    public void startTimer() {
        if (timeline != null) {
            timeline.stop();
        }
        timeSeconds.set(START_TIME);
        timeline = new Timeline();
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(START_TIME + 1),
                        new KeyValue(timeSeconds, 0)));
        timeline.playFromStart();
    }

    public Label getTimerLabel() {
        return timerLabel;
    }

    public Label getTimerTitle() {
        return timerTitle;
    }

}
