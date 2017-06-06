package es.cbikesim.mainMenu.view;


import es.cbikesim.game.contract.Game;
import es.cbikesim.game.presenter.GamePresenter;
import es.cbikesim.game.view.GameView;
import es.cbikesim.mainMenu.contract.MainMenu;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
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

public class MainMenuView implements MainMenu.View {

    private static final int WIDTH = 1280, HEIGHT = 720;

    private boolean firstLoad = false, audioState = true;
    private double lineX = WIDTH / 2 - 100, lineY = HEIGHT / 3 + 50;

    private MainMenu.Presenter presenter;
    private Stage primaryStage;

    private Pane root = new Pane();
    private VBox menuBox = new VBox(-5);
    private Line line;
    private MediaPlayer mp, mpSelect, mpHover;


    public MainMenuView(Stage primaryStage, MainMenu.Presenter presenter){
        this.primaryStage = primaryStage;
        this.presenter = presenter;
        this.presenter.setView(this);
    }

    public void start() throws Exception {
        Scene scene = new Scene(createContent());

        this.primaryStage.setTitle("CBike Sim GameView");
        this.primaryStage.setScene(scene);
        this.primaryStage.show();

        mp.play();
    }

    public void initGame(){
        try {
            Game.Presenter gp = new GamePresenter();
            new GameView(this.primaryStage, gp).start();
        } catch (Exception e){

        }
    }

    public void changeToSettings(){
        menuBox.getChildren().clear();
        addMenu(lineX + 5, lineY + 5, this.getSettingsData());
    }

    public void changeToHome(){
        menuBox.getChildren().clear();
        addMenu(lineX + 5, lineY + 5, this.getMenuData());
    }

    public void changeMusic(){
        audioState = !audioState;
        if(audioState) mp.play();
        else mp.stop();
    }

    public boolean isAudioState(){
        return this.audioState;
    }

    public void setAudioState(boolean audioState){
        this.audioState = audioState;
    }

    public MediaPlayer getMp() {
        return mp;
    }

    public MediaPlayer getMpSelect() {
        return mpSelect;
    }

    public MediaPlayer getMpHover() {
        return mpHover;
    }

    private Parent createContent() {
        addBackground();
        addTitle();

        addLine(lineX, lineY);
        addMenu(lineX + 5, lineY + 5, this.getMenuData());
        prepareMusic();

        return root;
    }

    private void addBackground() {
        ImageView imageView = new ImageView(new Image(getClass().getResource("/img/bicycle_wallpaper.jpg").toExternalForm()));
        imageView.setFitWidth(WIDTH);
        imageView.setFitHeight(HEIGHT);

        root.getChildren().add(imageView);
    }

    private void addTitle() {
        MenuTitleView title = new MenuTitleView("CBikeSim");
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

    private List<Pair<String, Runnable>> getSettingsData() {
        return Arrays.asList(
                new Pair<String, Runnable>("Display FULLSCREEN ", () -> {}),
                new Pair<String, Runnable>("Audio   " + (audioState ? "ON" : "OFF"), () -> {}),
                new Pair<String, Runnable>("Back", () -> {})
        );
    }


    private List<Pair<String, Runnable>> getMenuData() {
        return Arrays.asList(
                new Pair<String, Runnable>("Single Player", () -> {}),
                new Pair<String, Runnable>("GameView Options", ()-> {}),
                new Pair<String, Runnable>("Additional Content", () -> {}),
                new Pair<String, Runnable>("Tutorial", () -> {}),
                new Pair<String, Runnable>("Credits", () -> {}),
                new Pair<String, Runnable>("Exit to Desktop", Platform::exit)
        );
    }

    private void addMenu(double x, double y, List<Pair<String, Runnable>> l) {
        menuBox.setTranslateX(x);
        menuBox.setTranslateY(y);
        l.forEach(data -> {
            MenuItemView item = new MenuItemView(data.getKey(), this);
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
        }

        startAnimation();
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

    private void prepareMusic(){
        String pathSelect = MainMenuView.class.getResource("/music/select.wav").toString();
        String pathHoverM = MainMenuView.class.getResource("/music/hover.wav").toString();
        String path = MainMenuView.class.getResource("/music/funny_arcade.mp3").toString();

        Media media = new Media(path);
        Media mediaSelect = new Media(pathSelect);
        Media mediaHover = new Media(pathHoverM);

        this.mp = new MediaPlayer(media);
        this.mpSelect = new MediaPlayer(mediaSelect);
        this.mpHover = new MediaPlayer(mediaHover);
    }

}
