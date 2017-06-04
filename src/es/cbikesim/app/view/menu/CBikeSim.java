package es.cbikesim.app.view.menu;


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

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

    boolean firstLoad = false;
    boolean audioState = true;

    String pathSelect = CBikeSim.class.getResource("select.wav").toString();
    String pathHoverM = CBikeSim.class.getResource("hover.wav").toString();
    String path = CBikeSim.class.getResource("funny_arcade.mp3").toString();
    Media media = new Media(path);
    Media mediaSelect = new Media(pathSelect);
    Media mediaHover = new Media(pathSelect);
    MediaPlayer mp = new MediaPlayer(media);
    MediaPlayer mpSelect = new MediaPlayer(mediaSelect);
    MediaPlayer mpHover = new MediaPlayer(mediaSelect);



    private List<Pair<String, Runnable>> getSettingsData(){
        return Arrays.asList(
                new Pair<String, Runnable>("Display FULLSCREEN ", () -> {}),
                new Pair<String, Runnable>("Audio   " + (audioState ? "ON" : "OFF"), () -> {}),
                new Pair<String, Runnable>("Back", () -> {})
        );
    }


    private List<Pair<String, Runnable>> menuData = Arrays.asList(
            new Pair<String, Runnable>("Single Player", () -> {}),
            new Pair<String, Runnable>("Game Options", ()-> {}),
            new Pair<String, Runnable>("Additional Content", () -> {}),
            new Pair<String, Runnable>("Tutorial", () -> {}),
            new Pair<String, Runnable>("Credits", () -> {}),
            new Pair<String, Runnable>("Exit to Desktop", Platform::exit)
    );


    private Pane root = new Pane();
    private VBox menuBox = new VBox(-5);
    private Line line;

    double lineX = WIDTH / 2 - 100;
    double lineY = HEIGHT / 3 + 50;

    private Parent createContent() {
        addBackground();
        addTitle();

        addLine(lineX, lineY);
        addMenu(lineX + 5, lineY + 5,menuData);

        startAnimation();

        return root;
    }

    private void addBackground() {
        ImageView imageView = new ImageView(new Image(getClass().getResource("res/bicycle_wallpaper.jpg").toExternalForm()));
        imageView.setFitWidth(WIDTH);
        imageView.setFitHeight(HEIGHT);

        root.getChildren().add(imageView);
    }

    private void addTitle() {
        MenuTitle title = new MenuTitle("CBikeSim");
        title.setTranslateX(WIDTH / 2.25 - title.getTitleWidth() / 2);
        title.setTranslateY(HEIGHT /3);

        root.getChildren().add(title);
    }

    private void addLine(double x, double y) {
        line = new Line(x, y, x, y + 300);
        line.setStrokeWidth(3);
        line.setStroke(Color.color(1, 1, 1, 0.75));
        line.setEffect(new DropShadow(5, Color.BLACK));
        line.setScaleY(0);

        root.getChildren().add(line);
    }

    private void startAnimation() {
        ScaleTransition st = new ScaleTransition(Duration.seconds(1), line);
        st.setToY(1);
        st.setOnFinished(e -> {

            for (int i = 0; i < menuBox.getChildren().size(); i++) {
                Node n = menuBox.getChildren().get(i);

                TranslateTransition tt = new TranslateTransition(Duration.seconds(1 + i * 0.15), n);
                tt.setToX(0);
                tt.setOnFinished(e2 -> n.setClip(null));
                tt.play();
            }
        });
        st.play();
    }

    public void changeSettings(){
        menuBox.getChildren().clear();
        addMenu(lineX + 5, lineY + 5,getSettingsData());
    }

    public void backToHome(){
        menuBox.getChildren().clear();
        addMenu(lineX + 5, lineY + 5, menuData);
    }

    public void changeMusic(){
        if(audioState) mp.play();
        else mp.stop();
    }

    private void addMenu(double x, double y, List<Pair<String, Runnable>> l) {
        menuBox.setTranslateX(x);
        menuBox.setTranslateY(y);
        l.forEach(data -> {
            MenuItem item = new MenuItem(data.getKey(),this);
            item.setOnAction(data.getValue());
            item.setTranslateX(-300);

            Rectangle clip = new Rectangle(300, 30);
            clip.translateXProperty().bind(item.translateXProperty().negate());

            item.setClip(clip);

            menuBox.getChildren().addAll(item);
        });
        if(!firstLoad){
            root.getChildren().add(menuBox);
            firstLoad = true;
        }else{
            startAnimation();
        }

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());
        primaryStage.setTitle("CBike Sim Game");
        primaryStage.setScene(scene);
        mp.play();

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
