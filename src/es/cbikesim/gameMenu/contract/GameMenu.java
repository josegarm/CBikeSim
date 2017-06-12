package es.cbikesim.gameMenu.contract;

import es.cbikesim.gameMenu.view.GameMenuItemView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public interface GameMenu {

    interface View {
        void start(Stage stage);

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
