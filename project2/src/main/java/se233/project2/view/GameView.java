package se233.project2.view;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Scale;
import se233.project2.util.SpriteSheet;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GameView extends Pane {

    public static final double W = 224, H = 240;
    public static final double SCALE = 2;
    public static final double HUD_H = 36;          // ความสูงแถบข้อความ (พิกัดต้นฉบับ)
    private final Pane hud = new Pane();
    private final Text levelText = new Text();
    private final Text scoreText = new Text();
    private final Text lifeText = new Text();
    private final Text comboText = new Text();
    private int totalStages = 3;
    private int currentStage = 1;

    Font font = Font.font("Monospaced", FontWeight.BOLD, 8);

    private static final String ASSETS = "/se233/project2/assets/";

    // พิกัด (x, y) ของอิฐแต่ละแถวใน Blocks.png ขนาด 16x8
    private static final int[][] BRICK_SPRITES = {
            {0, 16},  // เงิน
            {0, 8},   // แดง
            {48, 8},  // เหลือง
            {16, 8},  // น้ำเงิน
            {32, 8},  // ชมพู
            {48, 0}   // เขียว
    };

    private final Pane world = new Pane();
    private final List<ImageView> bricks = new ArrayList<>();
    private final ImageView paddle;
    private final ImageView ball;

    public GameView() {
        double totalH = (H + HUD_H) * SCALE;
        setPrefSize(W * SCALE, totalH);
        setMinSize(W * SCALE, totalH);
        setMaxSize(W * SCALE, totalH);
        setStyle("-fx-background-color: black;");

        hud.setPrefSize(W, HUD_H);
        hud.getTransforms().add(new Scale(SCALE, SCALE, 0, 0));

        world.setPrefSize(W, H);
        world.setLayoutY(HUD_H * SCALE);
        world.getTransforms().add(new Scale(SCALE, SCALE, 0, 0));

        getChildren().addAll(hud, world);

        SpriteSheet bgSheet = new SpriteSheet(ASSETS + "Backgrounds_Fields.png");
        SpriteSheet blocks = new SpriteSheet(ASSETS + "Blocks.png");
        SpriteSheet vaus = new SpriteSheet(ASSETS + "Padding.png");


        world.getChildren().add(bgSheet.sprite(0, 0, 224, 240));

        levelText.setFont(font);
        levelText.setFill(Color.RED);
        scoreText.setFont(font);
        scoreText.setFill(Color.WHITE);
        lifeText.setFont(font);
        lifeText.setFill(Color.RED);
        comboText.setFont(font);
        comboText.setFill(Color.YELLOW);
        hud.getChildren().addAll(levelText, scoreText, lifeText, comboText);

        setLevel(1);
        setScore(0);
        setLife(3);
        setCombo(1);

        createBricks(blocks);
        world.getChildren().addAll(bricks);

        paddle = vaus.spriteKeyed(34, 0, 28, 8);
        paddle.setLayoutX(112 - 14);
        paddle.setLayoutY(208);

        ball = vaus.spriteKeyed(0, 40, 5, 4);
        ball.setLayoutX(112 - 2);
        ball.setLayoutY(200);

        world.getChildren().addAll(paddle, ball);
    }

    public ImageView getPaddle() { return paddle; }
    public ImageView getBall() { return ball; }
    public List<ImageView> getBricks() { return bricks; }

    private void createBricks(SpriteSheet blocks) {
        double startX = 8, startY = 40;
        for (int r = 0; r < BRICK_SPRITES.length; r++) {
            for (int c = 0; c < 13; c++) {
                ImageView b = blocks.sprite(BRICK_SPRITES[r][0], BRICK_SPRITES[r][1], 16, 8);
                b.setLayoutX(startX + c * 16);
                b.setLayoutY(startY + r * 8);
                bricks.add(b);
            }
        }
    }

    public void setLevel(int level) {
        currentStage = level;
        levelText.setText("Stage " + level + " / " + totalStages);
        centerText(levelText, 16);                 // กลางจอ
    }

    public void setTotalStages(int total) {
        totalStages = total;
        setLevel(currentStage);                    // วาดข้อความใหม่
    }

    public void setScore(int score) {
        scoreText.setText("SCORE: " + score);
        scoreText.setLayoutX(8);                   // ซ้าย
        scoreText.setLayoutY(16);
        placeCombo();
    }

    public void setLife(int life) {
        lifeText.setText("LIFE: " + life);
        alignRight(lifeText, 16);                  // ขวา
    }

    public void setCombo(int combo) {
        comboText.setText("x" + combo);
        alignRight(comboText, 28);                 // ขวา บรรทัดล่าง
        placeCombo();
    }

    private void centerText(Text t, double baselineY) {
        t.setLayoutX((W - t.getLayoutBounds().getWidth()) / 2);
        t.setLayoutY(baselineY);
    }

    private void alignRight(Text t, double baselineY) {
        t.setLayoutX(W - 8 - t.getLayoutBounds().getWidth());
        t.setLayoutY(baselineY);
    }

    private void placeCombo() {
        comboText.setLayoutX(scoreText.getLayoutX() + scoreText.getLayoutBounds().getWidth() + 6);
        comboText.setLayoutY(16);
    }
}