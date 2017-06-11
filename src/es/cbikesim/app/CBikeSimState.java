package es.cbikesim.app;

import javafx.stage.Stage;

public class CBikeSimState {

    private static CBikeSimState instance;

    private Stage primaryStage;
    private boolean audio;

    private CBikeSimState() {
        this.audio = true;

    }

    public static CBikeSimState getInstance() {
        if (instance == null) instance = new CBikeSimState();
        return instance;
    }

    public void turnAudio() {
        this.audio = !audio;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public boolean getAudio() {
        return audio;
    }

    public void setAudio(boolean audio) {
        this.audio = audio;
    }
}
