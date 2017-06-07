package es.cbikesim.mainMenu.view;

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
    private static final int FEW = 0, NORMAL = 1;

    private int lineX = WIDTH / 2 - 100;
    private int lineY = HEIGHT / 3 + 50;

    private Pane root = new Pane();
    private VBox menuBox = new VBox(-5);
    private Line line;

    private MainMenu.Presenter presenter;
    private Stage primaryStage;

    private boolean firstLoad = false, audioState = true;
    private int numBikes = FEW, carCapacity = 3;

    private MediaPlayer mp, mpSelect, mpHover;


    public MainMenuView(Stage primaryStage, MainMenu.Presenter presenter){
        this.primaryStage = primaryStage;
        this.presenter = presenter;
        this.presenter.setView(this);
    }

    @Override
    public void start() {
        Scene scene = new Scene(createContent());

        this.primaryStage.setTitle("CBike Sim GameView");
        this.primaryStage.setScene(scene);
        this.primaryStage.show();

        mp.play();
    }

    public void initGame(){
        //method to set difficulty
        presenter.initGame(this.primaryStage);
        mp.stop();
        }

    public void changeToSettings(){
        addMenu(lineX + 5, lineY + 5, this.getSettingsData());
    }

    public void changeToHome(){
        addMenu(lineX + 5, lineY + 5, this.getMenuData());
    }

    public void changeToDifficulty(){
        addMenu(lineX + 5, lineY + 5, this.getDifficultyData());
    }

    public void changeToCustomDifficulty(){
        addMenu(lineX + 5, lineY + 5, this.getCustomDifficultyData());
    }

    public void changeMusic(){
        audioState = !audioState;
        if(audioState) mp.play();
        else mp.stop();
    }

    public void changeNumBikes(){
        if(numBikes == FEW) numBikes = NORMAL;
        else numBikes = FEW;
    }

    public void changeCarCapacity(){
        if(carCapacity >= 9) carCapacity = 3;
        else carCapacity = carCapacity + 3;
    }

    public boolean isAudioState(){
        return this.audioState;
    }

    public void setAudioState(boolean audioState){
        this.audioState = audioState;
    }

    public int getNumBikes(){ return this.numBikes; }

    public int getCarCapacity(){ return this.carCapacity; }

    public void setNumBikes(int fewBikes){ this.numBikes = fewBikes;}

    public void setCarCapacity(int carCapacity){ this.carCapacity = carCapacity;}

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
        ImageView imageView = new ImageView(new Image(getClass().getResource("/img/bicycle_wallpaper_1.jpg").toExternalForm()));
        imageView.setFitWidth(WIDTH);
        imageView.setFitHeight(HEIGHT);

        root.getChildren().add(imageView);
    }

    private void addTitle() {
        MenuTitleView title = new MenuTitleView("CBikeSim");
        title.setTranslateX(WIDTH / 2 - title.getTitleWidth() / 2);
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
                //new Pair<String, Runnable>("DISPLAY FULLSCREEN ", () -> {}),
                new Pair<String, Runnable>("AUDIO   " + (audioState ? "ON" : "OFF"), () -> {
                    this.changeMusic();
                    updateMenuWith(getSettingsData());
                }),
                new Pair<String, Runnable>("BACK", this::changeToHome)
        );
    }


    private List<Pair<String, Runnable>> getMenuData() {
        return Arrays.asList(
                new Pair<String, Runnable>("SINGLE PLAYER", this::changeToDifficulty),
                new Pair<String, Runnable>("GAME OPTIONS", this::changeToSettings),
                //new Pair<String, Runnable>("ADDITIONAL CONTENT", this:: ),
                //new Pair<String, Runnable>("TUTORIAL", () -> {}),
                //new Pair<String, Runnable>("CREDITS", () -> {}),
                new Pair<String, Runnable>("EXIT TO DESKTOP", Platform::exit)
        );
    }

    private List<Pair<String, Runnable>> getDifficultyData() {
        return Arrays.asList(
                new Pair<String, Runnable>("EASY", this::initGame),
                new Pair<String, Runnable>("NORMAL", this::initGame),
                new Pair<String, Runnable>("HARD", this::initGame),
                new Pair<String, Runnable>("CUSTOM", this::changeToCustomDifficulty),
                new Pair<String, Runnable>("BACK", this::changeToHome)
        );
    }

    private List<Pair<String, Runnable>> getCustomDifficultyData() {
        return Arrays.asList(
                new Pair<String, Runnable>("NUMBER OF BIKES    " + (numBikes == FEW ? "FEW" : "NORMAL"), () -> {
                    this.changeNumBikes();
                    updateMenuWith(getCustomDifficultyData());
                }),
                new Pair<String, Runnable>("BIKE CAPACITY CAR  " + carCapacity, () -> {
                    this.changeCarCapacity();
                    updateMenuWith(getCustomDifficultyData());
                }),
                new Pair<String, Runnable>("PLAY", this::initGame),
                new Pair<String, Runnable>("BACK", this::changeToDifficulty)
        );
    }

    private void addMenu(double x, double y, List<Pair<String, Runnable>> l) {
        menuBox.getChildren().clear();
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

    private void updateMenuWith(List<Pair<String, Runnable>> list) {
        menuBox.getChildren().clear();
        list.forEach(data -> {
            MenuItemView item = new MenuItemView(data.getKey(), this);
            item.setOnAction(data.getValue());
            menuBox.getChildren().addAll(item);
        });
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

        this.mp.setVolume(0.3);
        this.mpSelect.setVolume(1.0);
        this.mpHover.setVolume(0.1);
    }

}
