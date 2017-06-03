package es.cbikesim.app;

import es.cbikesim.lib.examples.Presenter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CBikeSim extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/es/cbikesim/app/view/mainMenu.fxml"));

        Scene scene = new Scene(root, 1440, 900);
        primaryStage.setScene(scene);

        primaryStage.initStyle(StageStyle.UTILITY);

        primaryStage.show();



    }


    public static void main(String[] args) {
        launch(args);
        //new Presenter().init(); // for tests
    }
}
