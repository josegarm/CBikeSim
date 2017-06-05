package es.cbikesim.mainMenu.view;

import javafx.beans.binding.Bindings;
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

public class MenuItemView extends Pane {

    private MainMenuView context;
    private Text text;

    private Effect shadow = new DropShadow(5, Color.BLACK);
    private Effect blur = new BoxBlur(1, 1, 2);


    public MenuItemView(String name, MainMenuView obj) {
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
        text.setFont(Font.loadFont(MenuItemView.class.getResource("/font/Penumbra-HalfSerif-Std_35114.ttf").toExternalForm(), 22));
        text.setFill(Color.WHITE);

        text.effectProperty().bind(
                Bindings.when(hoverProperty())
                        .then(shadow)
                        .otherwise(blur)
        );

        setOnMouseEntered(e -> {
            context.getMpHover().stop();
            context.getMpHover().play();
        });


        setOnMousePressed(e -> {
            context.getMpSelect().stop();
            context.getMpSelect().play();

            switch(text.getText()){
                case "Single Player" : context.initGame(); break;
                case "GameView Options" : context.changeToSettings(); break;
                case "Back" : context.changeToHome(); break;
                case "Audio   ON" : changeAudio(); break;
                case "Audio   OFF" : changeAudio(); break;
            }
        });

        getChildren().addAll(bg, text);
    }

    private void changeAudio() {
        context.changeMusic();
        text.setText("Audio   " + (context.isAudioState() ? "ON" : "OFF"));
    }

    public void setOnAction(Runnable action) {
        setOnMouseClicked(e -> action.run());
    }

}