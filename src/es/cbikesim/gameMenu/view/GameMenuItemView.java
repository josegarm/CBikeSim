package es.cbikesim.gameMenu.view;

import es.cbikesim.gameMenu.contract.GameMenu;
import javafx.beans.binding.Bindings;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GameMenuItemView extends Pane {

    private GameMenu.Presenter context;
    private Text text;

    private Effect shadow = new DropShadow(5, Color.BLACK);
    private Effect blur = new BoxBlur(1, 1, 2);


    public GameMenuItemView(String name, GameMenu.Presenter context) {
        this.context = context;

        addListeners();

        getChildren().addAll(createBackground(), createText(name));
    }

    public void setText(String text) {
        this.text.setText(text);
    }

    public void setOnAction(Runnable action) {
        setOnMouseClicked(e -> action.run());
    }

    private Polygon createBackground() {
        Polygon bg = new Polygon(
                0, 0,
                180, 0,
                195, 15,
                180, 30,
                0, 30
        );

        bg.setStroke(Color.color(0, 0.2314, 0.5333));
        bg.setEffect(new GaussianBlur());

        bg.fillProperty().bind(
                Bindings.when(pressedProperty())
                        .then(Color.gray(1, 0.1))
                        .otherwise(Color.gray(1, 0.2))
        );

        return bg;
    }

    private Text createText(String name) {
        text = new Text(name);

        text.setTranslateX(5);
        text.setTranslateY(20);
        text.setFont(Font.loadFont(getClass().getResource("/font/Penumbra-HalfSerif-Std_35114.ttf").toExternalForm(), 12));
        text.setFill(Color.WHITE);

        text.effectProperty().bind(
                Bindings.when(hoverProperty())
                        .then(shadow)
                        .otherwise(blur)
        );

        return text;
    }

    private void addListeners() {

        setOnMouseEntered(e -> {
            context.playHover();
        });

        setOnMousePressed(e -> {
            context.playSelect();
            context.setItemPressed(this);
        });

    }

}