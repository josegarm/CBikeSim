package es.cbikesim.mainMenu.presenter;

import es.cbikesim.game.contract.Game;
import es.cbikesim.game.presenter.GamePresenter;
import es.cbikesim.game.view.GameView;
import es.cbikesim.mainMenu.contract.MainMenu;
import es.cbikesim.mainMenu.view.MenuItemView;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.List;

public class MainMenuPresenter implements MainMenu.Presenter{

    private static final int FEW_BIKES = 0, NORMAL_BIKES = 1;

    private MainMenu.View view;

    private boolean audioState = true;
    private int numBikes, carCapacity;
    private MediaPlayer mp, mpSelect, mpHover;

    private MenuItemView itemPressed;

    public MainMenuPresenter(){
        numBikes = FEW_BIKES;
        carCapacity = 3;
    }

    @Override
    public void load() {
        addMenu(this.getMenuData());
        prepareMusic();
        mp.play();
    }

    @Override
    public void playHover() {
        mpHover.stop();
        mpHover.play();
    }

    @Override
    public void playSelect() {
        mpSelect.stop();
        mpSelect.play();
    }

    @Override
    public void setItemPressed(MenuItemView itemPressed){
        this.itemPressed = itemPressed;
    }

    @Override
    public void setView(MainMenu.View view) {
        this.view = view;
    }

    private void initGame(int difficulty) {
        mp.stop();
        Game.Presenter gamePresenter = new GamePresenter();
        Game.View gameView = new GameView(view.getPrimaryStage(), gamePresenter);
        gamePresenter.createScenario(difficulty, numBikes, carCapacity);
        gameView.start();
    }

    private void addMenu(List<Pair<String, Runnable>> list) {
        view.getMenuBox().getChildren().clear();
        list.forEach(data -> {
            MenuItemView item = new MenuItemView(data.getKey(), this);
            item.setOnAction(data.getValue());
            item.setTranslateX(-300);

            Rectangle clip = new Rectangle(300, 30);
            clip.translateXProperty().bind(item.translateXProperty().negate());

            item.setClip(clip);

            view.getMenuBox().getChildren().addAll(item);
        });

        startAnimation();
    }

    private void startAnimation() {
        ScaleTransition st = new ScaleTransition(Duration.seconds(1), view.getLine());
        st.setToY(1);
        st.setOnFinished(e -> {

            for (int i = 0; i < view.getMenuBox().getChildren().size(); i++) {
                Node n = view.getMenuBox().getChildren().get(i);

                TranslateTransition tt = new TranslateTransition(Duration.seconds(1 + i * 0.15), n);
                tt.setToX(0);
                tt.setOnFinished(e2 -> n.setClip(null));
                tt.play();
            }

        });
        st.play();
    }

    private void prepareMusic(){
        String pathSelect = getClass().getResource("/music/select.wav").toString();
        String pathHoverM = getClass().getResource("/music/hover.wav").toString();
        String path = getClass().getResource("/music/funny_arcade.mp3").toString();

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

    private List<Pair<String, Runnable>> getSettingsData() {
        return Arrays.asList(
                new Pair<String, Runnable>("AUDIO   " + (audioState ? "ON" : "OFF"), () -> {
                    this.changeMusic();
                    this.itemPressed.setText("AUDIO   " + (audioState ? "ON" : "OFF"));
                }),
                new Pair<String, Runnable>("BACK", () -> addMenu(getMenuData()))
        );
    }

    private List<Pair<String, Runnable>> getMenuData() {
        return Arrays.asList(
                new Pair<String, Runnable>("SINGLE PLAYER", () -> addMenu(getDifficultyData())),
                new Pair<String, Runnable>("GAME OPTIONS", () -> addMenu(getSettingsData())),
                new Pair<String, Runnable>("ADDITIONAL CONTENT", () -> {} ),
                new Pair<String, Runnable>("TUTORIAL", () -> {}),
                new Pair<String, Runnable>("CREDITS", () -> {}),
                new Pair<String, Runnable>("EXIT TO DESKTOP", Platform::exit)
        );
    }

    private List<Pair<String, Runnable>> getDifficultyData() {
        return Arrays.asList(
                new Pair<String, Runnable>("EASY", () -> initGame(GamePresenter.EASY)),
                new Pair<String, Runnable>("NORMAL", () -> initGame(GamePresenter.NORMAL)),
                new Pair<String, Runnable>("HARD", () -> initGame(GamePresenter.HARD)),
                new Pair<String, Runnable>("CUSTOM", () -> addMenu(getCustomDifficultyData())),
                new Pair<String, Runnable>("BACK", () -> addMenu(getMenuData()))
        );
    }

    private List<Pair<String, Runnable>> getCustomDifficultyData() {
        return Arrays.asList(
                new Pair<String, Runnable>("NUMBER OF BIKES    " + (numBikes == FEW_BIKES ? "FEW" : "NORMAL"), () -> {
                    this.changeNumBikes();
                    this.itemPressed.setText("NUMBER OF BIKES    " + (numBikes == FEW_BIKES ? "FEW" : "NORMAL"));
                }),
                new Pair<String, Runnable>("BIKE CAPACITY CAR  " + carCapacity, () -> {
                    this.changeCarCapacity();
                    this.itemPressed.setText("BIKE CAPACITY CAR  " + carCapacity);
                }),
                new Pair<String, Runnable>("PLAY", () -> initGame(GamePresenter.CUSTOM)),
                new Pair<String, Runnable>("BACK", () -> addMenu(getDifficultyData()))
        );
    }

    private void changeMusic(){
        audioState = !audioState;
        if(audioState) mp.play();
        else mp.stop();
    }

    private void changeNumBikes(){
        if(numBikes == FEW_BIKES) numBikes = NORMAL_BIKES;
        else numBikes = FEW_BIKES;
    }

    private void changeCarCapacity(){
        if(carCapacity >= 9) carCapacity = 3;
        else carCapacity = carCapacity + 3;
    }


}
