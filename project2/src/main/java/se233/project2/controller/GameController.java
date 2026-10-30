package se233.project2.controller;

import javafx.scene.Scene;
import se233.project2.view.GameView;

public class GameController {

    private final GameView view = new GameView();
    private final Scene scene = new Scene(view);

    public Scene getScene() {
        return scene;
    }

    public GameView getView() {
        return view;
    }

    //ใน update(dt) ทำตามลำดับ: อ่านคีย์ที่กดอยู่ → ขยับ model → ตรวจชน → sync ตำแหน่งไปที่ view

    //check collide function

    //check keyboard input


}