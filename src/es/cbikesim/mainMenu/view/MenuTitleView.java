package es.cbikesim.mainMenu.view;

import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class MenuTitleView extends Pane {
    private Text text;

    public MenuTitleView(String name) {
        String spread = "";
        for (char c : name.toCharArray()) {
            spread += c + " ";
        }

        text = new Text(spread);
        text.setStyle("-fx-font: 40 'res/Penumbra-HalfSerif-Std_35114.ttf';");
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
