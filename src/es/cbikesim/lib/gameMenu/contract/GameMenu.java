package es.cbikesim.lib.gameMenu.contract;

import es.cbikesim.lib.gameMenu.view.GameMenuItemView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public interface GameMenu {

    interface View {
        void start();

        Stage getStage();

        VBox getMenuBox();

        Line getLine();
    }

    interface Presenter {
        void load();

        void playHover();

        void playSelect();

        void setItemPressed(GameMenuItemView gameMenuItemView);

        void setView(GameMenu.View view);
    }

}
