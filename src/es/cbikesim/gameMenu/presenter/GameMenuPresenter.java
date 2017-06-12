package es.cbikesim.gameMenu.presenter;

import es.cbikesim.app.CBikeSimState;
import es.cbikesim.game.contract.Game;
import es.cbikesim.gameMenu.contract.GameMenu;
import es.cbikesim.gameMenu.view.GameMenuItemView;
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

public class GameMenuPresenter implements GameMenu.Presenter {

    private GameMenu.View view;

    private Game.Presenter context;

    private MediaPlayer mpSelect, mpHover;

    private GameMenuItemView itemPressed;

    public GameMenuPresenter(Game.Presenter context) {
        this.context = context;
    }

    @Override
    public void load() {
        addMenu(this.getMenuData());
        prepareMusic();
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
    public void setItemPressed(GameMenuItemView itemPressed) {
        this.itemPressed = itemPressed;
    }

    @Override
    public void setView(GameMenu.View view) {
        this.view = view;
    }



    private void addMenu(List<Pair<String, Runnable>> list) {
        view.getMenuBox().getChildren().clear();
        list.forEach(data -> {
            GameMenuItemView item = new GameMenuItemView(data.getKey(), this);
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
        ScaleTransition st = new ScaleTransition(Duration.seconds(0.25), view.getLine());
        st.setToY(1);
        st.setOnFinished(e -> {

            for (int i = 0; i < view.getMenuBox().getChildren().size(); i++) {
                Node n = view.getMenuBox().getChildren().get(i);

                TranslateTransition tt = new TranslateTransition(Duration.seconds(0.5 + i * 0.15), n);
                tt.setToX(0);
                tt.setOnFinished(e2 -> n.setClip(null));
                tt.play();
            }

        });
        st.play();
    }

    private void prepareMusic() {
        String pathSelect = getClass().getResource("/music/select.wav").toString();
        String pathHoverM = getClass().getResource("/music/hover.wav").toString();

        Media mediaSelect = new Media(pathSelect);
        Media mediaHover = new Media(pathHoverM);

        this.mpSelect = new MediaPlayer(mediaSelect);
        this.mpHover = new MediaPlayer(mediaHover);

        this.mpSelect.setVolume(1.0);
        this.mpHover.setVolume(0.1);
    }


    private List<Pair<String, Runnable>> getMenuData() {
        return Arrays.asList(
                new Pair<String, Runnable>("RESUME", () -> {
                    view.getStage().close();
                }),
                new Pair<String, Runnable>("AUDIO   " + (CBikeSimState.getInstance().getAudio() ? "ON" : "OFF"), () -> {
                    context.changeMusic();
                    this.itemPressed.setText("AUDIO   " + (CBikeSimState.getInstance().getAudio() ? "ON" : "OFF"));
                }),
                new Pair<String, Runnable>("RESTART", () -> {
                    context.initGame();
                }),
                new Pair<String, Runnable>("EXIT TO MAIN MENU", () -> {

                    CBikeSimState.getInstance().getPrimaryStage().close();
                }),
                new Pair<String, Runnable>("EXIT GAME", Platform::exit)
        );
    }




}
