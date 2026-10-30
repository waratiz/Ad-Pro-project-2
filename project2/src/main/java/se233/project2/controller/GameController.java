package se233.project2.controller;

import javafx.scene.Scene;
import se233.project2.view.GameView;

public class GameController {

    private final GameView view = new GameView();
    private final Scene scene = new Scene(view);
    int score, life = 3, combo = 1, stage = 1;

    public Scene getScene() {
        return scene;
    }

    public GameView getView() {
        return view;
    }

    // GameController
    void update(double dt) {
        // 1. อ่านคีย์ → ขยับ paddle (model)
        // 2. ขยับ ball (model)
        // 3. ตรวจชน: ball กับกำแพง, paddle, brick
        //    - ชนอิฐ: state.score += 10 * state.combo; state.combo++;
        //    - ชนแพดเดิล: state.combo = 1;
        //    - ตกขอบล่าง: state.life--; state.combo = 1;
        // 4. sync ไป view
    }


}