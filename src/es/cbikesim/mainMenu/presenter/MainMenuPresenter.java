package es.cbikesim.mainMenu.presenter;

import es.cbikesim.app.CBikeSimState;
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

public class MainMenuPresenter implements MainMenu.Presenter {

    private MainMenu.View view;

    private int carCapacity, time;

    private String numBikes;
    private MediaPlayer mp, mpSelect, mpHover;

    private MenuItemView itemPressed;

    public MainMenuPresenter() {
        time = 180;
        numBikes = GamePresenter.FEW_BIKES;
        carCapacity = 3;
    }

    @Override
    public void initMenu() {
        prepareMusic();

        view.start(CBikeSimState.getInstance().getPrimaryStage());

        load();
    }

    @Override
    public void playHover() {
        if (CBikeSimState.getInstance().getAudio()) {
            mpHover.stop();
            mpHover.play();
        }
    }

    @Override
    public void playSelect() {
        if (CBikeSimState.getInstance().getAudio()) {
            mpSelect.stop();
            mpSelect.play();
        }
    }

    @Override
    public void setItemPressed(MenuItemView itemPressed) {
        this.itemPressed = itemPressed;
    }

    @Override
    public void setView(MainMenu.View view) {
        this.view = view;
    }

    private void initGame(int difficulty) {
        mp.stop();
        Game.Presenter gamePresenter = new GamePresenter(difficulty, time, numBikes, carCapacity);
        Game.View gameView = new GameView(gamePresenter);
        gamePresenter.initGame();
    }

    private void prepareMusic() {
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

    private void load() {
        addMenu(this.getMenuData());
        if (CBikeSimState.getInstance().getAudio()) mp.play();
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

    private List<Pair<String, Runnable>> getSettingsData() {
        return Arrays.asList(
                new Pair<String, Runnable>("AUDIO   " + (CBikeSimState.getInstance().getAudio() ? "ON" : "OFF"), () -> {
                    this.changeMusic();
                    this.itemPressed.setText("AUDIO   " + (CBikeSimState.getInstance().getAudio() ? "ON" : "OFF"));
                }),
                new Pair<String, Runnable>("BACK", () -> addMenu(getMenuData()))
        );
    }

    private List<Pair<String, Runnable>> getMenuData() {
        return Arrays.asList(
                new Pair<String, Runnable>("SINGLE PLAYER", () -> addMenu(getDifficultyData())),
                new Pair<String, Runnable>("GAME OPTIONS", () -> addMenu(getSettingsData())),
                //new Pair<String, Runnable>("ADDITIONAL CONTENT", () -> {} ),
                //new Pair<String, Runnable>("TUTORIAL", () -> {}),
                //new Pair<String, Runnable>("CREDITS", () -> {}),
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
                new Pair<String, Runnable>("TIME                          " + time + " sec", () -> {
                    this.changeTime();
                    this.itemPressed.setText("TIME                          " + time + " sec");
                }),
                new Pair<String, Runnable>("NUMBER OF BIKES    " + numBikes, () -> {
                    this.changeNumBikes();
                    this.itemPressed.setText("NUMBER OF BIKES    " + numBikes);
                }),
                new Pair<String, Runnable>("BIKE CAPACITY CAR  " + carCapacity, () -> {
                    this.changeCarCapacity();
                    this.itemPressed.setText("BIKE CAPACITY CAR  " + carCapacity);
                }),
                new Pair<String, Runnable>("PLAY", () -> initGame(GamePresenter.CUSTOM)),
                new Pair<String, Runnable>("BACK", () -> addMenu(getDifficultyData()))
        );
    }

    private void changeMusic() {
        CBikeSimState.getInstance().turnAudio();
        if (CBikeSimState.getInstance().getAudio()) mp.play();
        else mp.stop();
    }

    private void changeTime() {
        if (time >= 300) time = 120;
        else time = time + 30;
    }

    private void changeNumBikes() {
        if (numBikes.equals(GamePresenter.FEW_BIKES)) numBikes = GamePresenter.NORMAL_BIKES;
        else if (numBikes.equals(GamePresenter.NORMAL_BIKES)) numBikes = GamePresenter.MANY_BIKES;
        else if (numBikes.equals(GamePresenter.MANY_BIKES)) numBikes = GamePresenter.FEW_BIKES;
    }

    private void changeCarCapacity() {
        if (carCapacity >= 9) carCapacity = 3;
        else carCapacity = carCapacity + 3;
    }


}
