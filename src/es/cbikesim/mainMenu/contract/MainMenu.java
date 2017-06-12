package es.cbikesim.mainMenu.contract;

import es.cbikesim.mainMenu.view.MenuItemView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public interface MainMenu {

    interface View {
        void start(Stage primaryStage);

        VBox getMenuBox();

        Line getLine();
    }

    interface Presenter {
        void initMenu();

        void playHover();

        void playSelect();

        void setItemPressed(MenuItemView menuItemView);

        void setView(MainMenu.View view);
    }

}
