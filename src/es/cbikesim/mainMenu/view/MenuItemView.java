package es.cbikesim.mainMenu.view;

import es.cbikesim.mainMenu.contract.MainMenu;
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

public class MenuItemView extends Pane {

    private MainMenu.Presenter context;
    private Text text;

    private Effect shadow = new DropShadow(5, Color.BLACK);
    private Effect blur = new BoxBlur(1, 1, 2);


    public MenuItemView(String name, MainMenu.Presenter context) {
        this.context = context;

        addListeners();

        getChildren().addAll(createBackground(), createText(name));
    }

    public void setText(String text){
        this.text.setText(text);
    }

    public void setOnAction(Runnable action) {
        setOnMouseClicked(e -> action.run());
    }

    private Polygon createBackground(){
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
                        .then(Color.color(0, 0, 0, 0.85))
                        .otherwise(Color.color(0, 0, 0, 0.55))
        );

        return bg;
    }

    private Text createText(String name){
        text = new Text(name);

        text.setTranslateX(5);
        text.setTranslateY(20);
        text.setFont(Font.loadFont(MenuItemView.class.getResource("/font/Penumbra-HalfSerif-Std_35114.ttf").toExternalForm(), 14));
        text.setFill(Color.WHITE);

        text.effectProperty().bind(
                Bindings.when(hoverProperty())
                        .then(shadow)
                        .otherwise(blur)
        );

        return text;
    }

    private void addListeners(){

        setOnMouseEntered(e -> {
            context.playHover();
        });

        setOnMousePressed(e -> {
            context.playSelect();
            context.setItemPressed(this);
        });

    }

}