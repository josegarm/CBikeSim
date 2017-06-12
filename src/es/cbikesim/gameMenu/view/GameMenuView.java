package es.cbikesim.gameMenu.view;

import es.cbikesim.gameMenu.contract.GameMenu;
import es.cbikesim.mainMenu.view.MenuTitleView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GameMenuView implements GameMenu.View {

    private static final int WIDTH = 250, HEIGHT = 350;

    private GameMenu.Presenter presenter;

    private int lineX = WIDTH / 2 - 100;
    private int lineY = HEIGHT / 6 + 50;

    private Pane root;
    private VBox menuBox;
    private Line line;

    private Stage stage;

    public GameMenuView(GameMenu.Presenter presenter) {
        this.presenter = presenter;
        this.presenter.setView(this);
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(createContent());

        stage.setAlwaysOnTop(true);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public VBox getMenuBox() {
        return this.menuBox;
    }

    @Override
    public Line getLine() {
        return this.line;
    }

    private Parent createContent() {
        initComponents();
        addBackground();
        addTitle();
        addMenuBox();
        addLine();
        return root;
    }

    private void initComponents() {
        root = new Pane();
        menuBox = new VBox(-5);
    }

    private void addBackground() {
        String path = "/img/ingameMenu.jpg";
        ImageView imageView = new ImageView(new Image(getClass().getResource(path).toExternalForm()));
        imageView.setFitWidth(WIDTH);
        imageView.setFitHeight(HEIGHT);
        root.getChildren().add(imageView);
    }

    private void addTitle() {
        MenuTitleView title = new MenuTitleView("MENU");
        title.setTranslateX(WIDTH / 2 - title.getTitleWidth() / 2);
        title.setTranslateY(HEIGHT / 6);

        root.getChildren().add(title);
    }

    private void addLine() {
        line = new Line(lineX, lineY, lineX, lineY + 200);
        line.setStrokeWidth(3);
        line.setStroke(Color.color(1, 1, 1, 0.75));
        line.setEffect(new DropShadow(5, Color.BLACK));
        line.setScaleY(0);

        root.getChildren().add(line);
    }

    private void addMenuBox() {
        menuBox.getChildren().clear();
        menuBox.setTranslateX(lineX + 5);
        menuBox.setTranslateY(lineY + 5);

        root.getChildren().add(menuBox);
    }

}
