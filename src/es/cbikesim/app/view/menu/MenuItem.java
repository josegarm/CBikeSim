package es.cbikesim.app.view.menu;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.lang.reflect.*;

public class MenuItem extends Pane {
    private Text text;
    private CBikeSim context;

    private Effect shadow = new DropShadow(5, Color.BLACK);
    private Effect blur = new BoxBlur(1, 1, 2);


    String pathSelect = CBikeSim.class.getResource("select.wav").toString();
    String pathHoverM = CBikeSim.class.getResource("hover.wav").toString();
    Media mediaSelect = new Media(pathSelect);
    Media mediaHover = new Media(pathHoverM);
    MediaPlayer mpSelect = new MediaPlayer(mediaSelect);
    MediaPlayer mpHover = new MediaPlayer(mediaHover);



    public MenuItem(String name, CBikeSim obj) {
        context = obj;
        Polygon bg = new Polygon(
                0, 0,
                200, 0,
                215, 15,
                200, 30,
                0, 30
        );
        bg.setStroke(Color.color(1, 1, 1, 0.75));
        bg.setEffect(new GaussianBlur());

        bg.fillProperty().bind(
                Bindings.when(pressedProperty())
                        .then(Color.color(0, 0, 0, 0.75))
                        .otherwise(Color.color(0, 0, 0, 0.25))
        );

        text = new Text(name);
        text.setTranslateX(5);
        text.setTranslateY(20);
        text.setFont(Font.loadFont(CBikeSim.class.getResource("res/Penumbra-HalfSerif-Std_35114.ttf").toExternalForm(), 22));
        text.setFill(Color.WHITE);

        text.effectProperty().bind(
                Bindings.when(hoverProperty())
                        .then(shadow)
                        .otherwise(blur)
        );

        setOnMouseEntered(e -> {
            mpHover.play();
        });

        setOnMouseExited(e -> {
            if(!text.getText().equals("Exit to Desktop")){
                mpHover = new MediaPlayer(mediaHover);
            }
        });

        setOnMousePressed(e -> {
            mpSelect.play();
            switch(text.getText()){
                case "Game Options" : changeToSettings(); break;
                case "Back" : changeToHome(); break;
                case "Audio   ON" : changeAudio(context.audioState); break;
                case "Audio   OFF" : changeAudio(context.audioState); break;
            }

        });

        setOnMouseReleased(e -> {
            mpSelect = new MediaPlayer(mediaSelect);
        });


        getChildren().addAll(bg, text);
    }

    private void changeAudio(boolean audio) {
        context.audioState = !audio;
        context.changeMusic();
        text.setText("Audio   " + (context.audioState ? "ON" : "OFF"));
    }

    public void setOnAction(Runnable action) {
        setOnMouseClicked(e -> action.run());
    }

    public void changeToSettings(){
        context.changeSettings();
    }

    public void changeToHome(){
        context.backToHome();
    }
}