package se233.project2.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Summary {

    @FXML
    private Text resultText;

    @FXML
    private Text scoreText;

    @FXML
    private Button retryButton;

    @FXML
    private Button quitButton;

    //got called when game is over
    public void setResult(String result) {
        resultText.setText(result);
    }

    public void setScore(int score) {
        scoreText.setText("SCORE: " + score);
    }

    @FXML
    private void onRetryClicked(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new GameController().getScene());   // new instance of game
        stage.sizeToScene();
        stage.centerOnScreen();
    }

    @FXML
    private void onQuitClicked() {
        Platform.exit();
    }
}