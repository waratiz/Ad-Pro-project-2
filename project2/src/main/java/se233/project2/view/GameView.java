package se233.project2.view;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import se233.project2.util.SpriteSheet;

import java.util.ArrayList;
import java.util.List;

public class GameView extends Pane {

    public static final double W = 224, H = 240;
    public static final double SCALE = 2;

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
        setPrefSize(W * SCALE, H * SCALE);
        setMinSize(W * SCALE, H * SCALE);
        setMaxSize(W * SCALE, H * SCALE);

        world.setPrefSize(W, H);
        world.getTransforms().add(new Scale(SCALE, SCALE, 0, 0));
        getChildren().add(world);

        SpriteSheet bgSheet = new SpriteSheet(ASSETS + "Backgrounds_Fields.png");
        SpriteSheet blocks = new SpriteSheet(ASSETS + "Blocks.png");
        SpriteSheet vaus = new SpriteSheet(ASSETS + "Padding.png");

        world.getChildren().add(bgSheet.sprite(0, 0, 224, 240));

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
}