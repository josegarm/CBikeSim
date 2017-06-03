package es.cbikesim.app.view.controller;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javafx.event.ActionEvent;

public class mainMenuController {

    @FXML
    private Button exitButton;

    @FXML
    private Button settingsButton;

    @FXML
    private Button newGameButton;

    @FXML
    void newGameClick(ActionEvent event) {

    }

    @FXML
    void settingsClick(ActionEvent event) {

    }

    @FXML
    void exitClick(ActionEvent event) {
        Platform.exit();

    }

}

