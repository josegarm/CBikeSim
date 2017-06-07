package es.cbikesim.mainMenu.contract;

import javafx.stage.Stage;

public interface MainMenu {

    interface View{
        void start();
    }

    interface Presenter{
        void initGame(Stage primaryStage);
        void setView(MainMenu.View view);
    }

}
