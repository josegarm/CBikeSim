package es.cbikesim.presentation.app;

import es.cbikesim.lib.examples.Presenter;
import javafx.application.Application;
import javafx.stage.Stage;

public class CBikeSim extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        /*Parent root = FXMLLoader.load(getClass().getResource("../main/sample.fxml"));

        Scene scene = new Scene(root, 300, 275);

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(scene);
        primaryStage.show();*/
    }


    public static void main(String[] args) {
        //launch(args);
        new Presenter().init();
    }
}
