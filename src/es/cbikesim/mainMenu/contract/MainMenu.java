package es.cbikesim.mainMenu.contract;

import es.cbikesim.mainMenu.view.MenuItemView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

public interface MainMenu {

    interface View {
        void start();

        VBox getMenuBox();

        Line getLine();
    }

    interface Presenter {
        void load();

        void playHover();

        void playSelect();

        void setItemPressed(MenuItemView menuItemView);

        void setView(MainMenu.View view);
    }

}
