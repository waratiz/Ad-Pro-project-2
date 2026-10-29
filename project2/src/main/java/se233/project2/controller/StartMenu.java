package se233.project2.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class StartMenu {

    private static final double SCALE = 2;

    @FXML
    private Button startButton;

    @FXML
    private Button exitButton;

    @FXML
    public void initialize() {
        System.out.println("Menu loaded");
    }

    @FXML
    private void onStartClicked(ActionEvent event) {
        Image sheet = new Image(getClass().getResourceAsStream("/se233/project2/assets/Backgrounds_Fields.png"));

        ImageView background = new ImageView(sheet);
        // ตัดเฉพาะพื้นหลังแรก (x, y, กว้าง, สูง)
        background.setViewport(new Rectangle2D(0, 0, 224, 240));
        background.setFitWidth(224 * SCALE);
        background.setFitHeight(240 * SCALE);
        background.setSmooth(false); // pixel art จะได้ไม่เบลอ

        Pane root = new Pane(background);
        Scene gameScene = new Scene(root, 224 * SCALE, 240 * SCALE);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(gameScene);
        stage.sizeToScene();
        stage.centerOnScreen();
    }

    @FXML
    private void onExitClicked() {
        Platform.exit();
    }
}