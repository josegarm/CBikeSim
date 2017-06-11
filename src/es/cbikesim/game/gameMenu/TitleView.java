package es.cbikesim.game.gameMenu;

import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class TitleView extends Pane {
    private Text text;

    public TitleView(String name) {
        String spread = "";
        for (char c : name.toCharArray()) {
            spread += c + " ";
        }

        text = new Text(spread);
        text.setFont(Font.loadFont(getClass().getResource("/font/Penumbra-HalfSerif-Std_35114.ttf").toExternalForm(), 34));
        text.setFill(Color.WHITE);
        text.setEffect(new DropShadow(30, Color.BLACK));


        getChildren().addAll(text);
    }

    public double getTitleWidth() {
        return text.getLayoutBounds().getWidth();
    }

    public double getTitleHeight() {
        return text.getLayoutBounds().getHeight();
    }
}
