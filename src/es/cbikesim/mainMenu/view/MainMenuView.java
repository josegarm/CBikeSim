package es.cbikesim.mainMenu.view;

import es.cbikesim.mainMenu.contract.MainMenu;
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

import java.util.Random;

public class MainMenuView implements MainMenu.View {

    private static final int WIDTH = 1280, HEIGHT = 720;

    private MainMenu.Presenter presenter;
    private Stage primaryStage;

    private int lineX = WIDTH / 2 - 100;
    private int lineY = HEIGHT / 3 + 50;

    private Pane root;
    private VBox menuBox;
    private Line line;

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
    }

    @Override
    public Stage getPrimaryStage() {
        return this.primaryStage;
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
        addLine();
        addMenuBox();
        presenter.load();
        return root;
    }

    private void initComponents(){
        root = new Pane();
        menuBox = new VBox(-5);
    }

    private void addBackground() {
        String path = "/img/bicycle_wallpaper_"+ (new Random().nextInt((5 - 1) + 1) + 1) + ".jpg";
        ImageView imageView = new ImageView(new Image(getClass().getResource(path).toExternalForm()));
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

    private void addLine() {
        line = new Line(lineX, lineY, lineX, lineY + 300);
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
