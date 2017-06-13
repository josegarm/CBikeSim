package es.cbikesim.gameMenu.presenter;

import es.cbikesim.app.CBikeSimState;
import es.cbikesim.game.contract.Game;
import es.cbikesim.gameMenu.contract.GameMenu;
import es.cbikesim.gameMenu.view.GameMenuItemView;
import es.cbikesim.lib.util.Score;
import es.cbikesim.mainMenu.view.MenuTitleView;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.List;

public class GameMenuPresenter implements GameMenu.Presenter {

    private static final int GAME_MENU = 0, FINISH_GAME_MENU = 1;

    private GameMenu.View view;

    private Game.Presenter context;

    private MediaPlayer mpSelect, mpHover;

    private GameMenuItemView itemPressed;

    private Score score;

    private int type;

    public GameMenuPresenter(Game.Presenter context) {
        this.context = context;
        this.type = GAME_MENU;
    }

    public GameMenuPresenter(Game.Presenter context, Score score) {
        this.context = context;
        this.score = score;
        this.type = FINISH_GAME_MENU;
    }

    @Override
    public void initMenu() {
        prepareMusic();

        Stage stage = new Stage(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);

        view.start(stage);

        if (type == GAME_MENU) load();
        else if (type == FINISH_GAME_MENU) prepareFinishGameMenu();
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


    public void load() {
        addMenu(this.getMenuData());
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
                    view.getStage().close();
                    context.stopGame();
                    context.initGame();
                }),
                new Pair<String, Runnable>("EXIT TO MAIN MENU", () -> {
                    view.getStage().close();
                    context.backToMainMenu();
                }),
                new Pair<String, Runnable>("EXIT GAME", () -> {
                    context.stopGame();
                    Platform.exit();
                })
        );
    }

    private List<Pair<String, Runnable>> getGameDoneData() {
        return Arrays.asList(
                new Pair<String, Runnable>("RESTART", () -> {
                    view.getStage().close();
                    CBikeSimState.getInstance().getPrimaryStage().show();
                    context.stopGame();
                    context.initGame();
                }),
                new Pair<String, Runnable>("EXIT TO MAIN MENU", () -> {
                    view.getStage().close();
                    CBikeSimState.getInstance().getPrimaryStage().show();
                    context.backToMainMenu();
                }),
                new Pair<String, Runnable>("EXIT GAME", () -> {
                    context.stopGame();
                    Platform.exit();
                })
        );
    }

    private void prepareFinishGameMenu() {
        MenuTitleView finalScoreTitle = new MenuTitleView("Final Score");
        MenuTitleView finalScore = new MenuTitleView("0");

        finalScore.setText(score.getScore().getText());

        finalScoreTitle.setLayoutX(50);
        finalScoreTitle.setLayoutY(100);

        finalScore.setLayoutX(115);
        finalScore.setLayoutY(130);

        finalScoreTitle.setSizeText(15);
        finalScore.setSizeText(15);

        view.getMenuBox().setTranslateY(150);

        view.getLine().setStartY(150);
        view.getLine().setEndY(320);
        view.getRoot().getChildren().addAll(finalScore, finalScoreTitle);

        addMenu(getGameDoneData());

        CBikeSimState.getInstance().getPrimaryStage().hide();
    }


}
