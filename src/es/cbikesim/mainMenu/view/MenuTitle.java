package es.cbikesim.mainMenu.view;

import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class MenuTitle extends Pane {
    private Text text;

    public MenuTitle(String name) {
        String spread = "";
        for (char c : name.toCharArray()) {
            spread += c + " ";
        }

        text = new Text(spread);
        text.setStyle("-fx-font: 40 \"res/Penumbra-HalfSerif-Std_35114.ttf\";");
        //text.setFont(Font.loadFont(CBikeSim.class.getResource("res/Penumbra-HalfSerif-Std_35114.ttf").toExternalForm(), 50));
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
