package se233.project2.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class StartMenu {

    @FXML
    private Button startButton;

    @FXML
    private Button exitButton;

    @FXML
    private void onStartClicked(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new GameController().getScene());
        stage.sizeToScene();
        stage.centerOnScreen();
    }

    @FXML
    private void onExitClicked() {
        Platform.exit();
    }
}