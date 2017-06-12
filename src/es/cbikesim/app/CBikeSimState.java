package es.cbikesim.app;

import es.cbikesim.lib.util.StopRun;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class CBikeSimState {

    private static CBikeSimState instance;

    private Stage primaryStage;
    private boolean audio;

    private List<StopRun> threads;

    private CBikeSimState() {
        this.audio = true;
        threads = new ArrayList<>();
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
        primaryStage.setOnCloseRequest(event -> {
            for(StopRun sr : threads) sr.stopRun();
        });
    }

    public boolean getAudio() {
        return audio;
    }

    public void setAudio(boolean audio) {
        this.audio = audio;
    }

    public void addThread(StopRun sr){
        threads.add(sr);
    }
}
