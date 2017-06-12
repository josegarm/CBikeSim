package es.cbikesim.lib.util;


import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Score {

    private final Integer CLIENTASSIGNEDBIKE = 10;
    private final Integer CLIENTARRIVEDTODESTINATION = 20;
    private final Integer CARARRIVESTOSTATION = 5;
    private final Integer CLIENTDOESNTFITINSTATION = -5;

    private Label scoreTitle;
    private Label scoreLabel;

    private Timeline timeline;

    private IntegerProperty scoreUpdater;

    private int score;
    private int multiplier;

    public Score(){
        scoreLabel = new Label();
        scoreTitle = new Label();

        scoreUpdater = new SimpleIntegerProperty(0);

        scoreLabel.textProperty().bind(scoreUpdater.asString());
        scoreTitle.setText("Score: ");

        //FORMAT
        scoreLabel.setTextFill(Color.WHITE);
        scoreLabel.setStyle("-fx-font-size: 2em;");
        scoreLabel.setAlignment(Pos.CENTER_RIGHT);
        scoreLabel.setPrefWidth(85.0);

        scoreTitle.setTextFill(Color.WHITE);
        scoreTitle.setStyle("-fx-font-size: 2em;");

    }

    public void startScore(){
        scoreUpdater.setValue(0);
    }

    public Label getScore() {
        return scoreLabel;
    }

    public Label getScoreTitle(){ return scoreTitle; }


    public void changeScore(Integer score){
        Integer currentScore = scoreUpdater.getValue();
        scoreUpdater.set(((currentScore + score) >= 0) ? scoreUpdater.getValue() + score : 0);
    }




}
