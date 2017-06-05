package es.cbikesim.app;


import es.cbikesim.mainMenu.view.MainMenu;
import es.cbikesim.mainMenu.view.MenuItem;
import es.cbikesim.mainMenu.view.MenuTitle;
import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.List;

public class CBikeSim extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        new MainMenu(primaryStage).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
