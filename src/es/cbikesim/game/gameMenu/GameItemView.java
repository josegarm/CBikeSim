package es.cbikesim.game.gameMenu;

import es.cbikesim.app.CBikeSimState;
import es.cbikesim.game.contract.Game;
import es.cbikesim.game.presenter.GamePresenter;
import es.cbikesim.mainMenu.contract.MainMenu;
import javafx.beans.binding.Bindings;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GameItemView extends Pane {

    private Text text;

    private Effect shadow = new DropShadow(5, Color.BLACK);
    private Effect blur = new BoxBlur(1, 1, 3);
    private MediaPlayer mpSelect, mpHover;
    private Game.Presenter context;

    public GameItemView(String name, Game.Presenter context) {
        this.context = context;
        prepareMusic();
        Polygon bg = new Polygon(
                0, 0,
                200, 0,
                215, 15,
                200, 30,
                0, 30
        );
        bg.setStroke(Color.color(1, 1, 1, 0.75));
        bg.setEffect(new GaussianBlur());

        bg.fillProperty().bind(
                Bindings.when(pressedProperty())
                        .then(Color.color(0, 0, 0, 0.75))
                        .otherwise(Color.color(0, 0, 0, 0.25))
        );

        text = new Text(name);
        text.setTranslateX(5);
        text.setTranslateY(20);
        text.setFont(Font.loadFont(GameMenuView.class.getResource("/font/Penumbra-HalfSerif-Std_35114.ttf").toExternalForm(), 13));
        text.setFill(Color.WHITE);

        text.effectProperty().bind(
                Bindings.when(hoverProperty())
                        .then(shadow)
                        .otherwise(blur)
        );

        setOnMouseEntered(e -> {
            playHover();
        });

        setOnMousePressed(e -> {
            playSelect();
        });

        getChildren().addAll(bg, text);
    }

    public void setOnAction(Runnable action) {
        setOnMouseClicked(e -> action.run());
    }

    public void playHover() {
        if (CBikeSimState.getInstance().getAudio()){
            mpHover.stop();
            mpHover.play();
        }
    }

    public void playSelect() {
        if (CBikeSimState.getInstance().getAudio()){
            mpSelect.stop();
            mpSelect.play();
        }
    }

    private void prepareMusic(){
        String pathSelect = getClass().getResource("/music/select.wav").toString();
        String pathHoverM = getClass().getResource("/music/hover.wav").toString();

        Media mediaSelect = new Media(pathSelect);
        Media mediaHover = new Media(pathHoverM);

        this.mpSelect = new MediaPlayer(mediaSelect);
        this.mpHover = new MediaPlayer(mediaHover);

        this.mpSelect.setVolume(1.0);
        this.mpHover.setVolume(0.1);
    }

    private void changeMusic(){
        CBikeSimState.getInstance().turnAudio();
    }

}