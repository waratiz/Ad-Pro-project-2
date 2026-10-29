package se233.project2.view;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;

import java.util.ArrayList;
import java.util.List;

public class GameView extends Pane {

    public static final double W = 224, H = 240;
    public static final double SCALE = 2;

    // พิกัด (x, y) ของอิฐแต่ละแถวใน Blocks.png ขนาด 16x8
    private static final int[][] BRICK_SPRITES = {
            {0, 16},  // เงิน
            {0, 8},   // แดง
            {48, 8},  // เหลือง
            {16, 8},  // น้ำเงิน
            {32, 8},  // ชมพู
            {48, 0}   // เขียว
    };

    private final Pane world = new Pane();   // พิกัดต้นฉบับ 224x240 แล้วขยายด้วย SCALE
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

        Image bgSheet = load("Backgrounds_Fields.png");
        Image blocks = load("Blocks.png");
        Image vaus = load("Padding.png");

        world.getChildren().add(sprite(bgSheet, 0, 0, 224, 240));

        createBricks(blocks);
        world.getChildren().addAll(bricks);

        paddle = new ImageView(keyGreen(vaus, 34, 0, 28, 8));
        paddle.setSmooth(false);
        paddle.setLayoutX(112 - 14);
        paddle.setLayoutY(208);

        ball = new ImageView(keyGreen(vaus, 0, 40, 5, 4));
        ball.setSmooth(false);
        ball.setLayoutX(112 - 2);
        ball.setLayoutY(200);

        world.getChildren().addAll(paddle, ball);
    }

    public ImageView getPaddle() { return paddle; }
    public ImageView getBall() { return ball; }
    public List<ImageView> getBricks() { return bricks; }

    private void createBricks(Image blocks) {
        double startX = 8, startY = 40;
        for (int r = 0; r < BRICK_SPRITES.length; r++) {
            for (int c = 0; c < 13; c++) {
                ImageView b = sprite(blocks, BRICK_SPRITES[r][0], BRICK_SPRITES[r][1], 16, 8);
                b.setLayoutX(startX + c * 16);
                b.setLayoutY(startY + r * 8);
                bricks.add(b);
            }
        }
    }

    private Image load(String name) {
        var stream = getClass().getResourceAsStream("/se233/project2/assets/" + name);
        if (stream == null) {
            throw new IllegalStateException("หารูปไม่เจอ: " + name);
        }
        return new Image(stream);
    }

    //ตัดชิ้นส่วนจาก sprite sheet มาแสดงเป็น ImageView โดยไม่สร้างรูปใหม่
    private ImageView sprite(Image sheet, double x, double y, double w, double h) {
        ImageView iv = new ImageView(sheet);
        iv.setViewport(new Rectangle2D(x, y, w, h));
        iv.setSmooth(false);
        return iv;
    }

    /** ตัดส่วนที่ต้องการ และทำให้สีเขียวพื้นหลัง (0,143,0) โปร่งใส */
    private Image keyGreen(Image sheet, int x, int y, int w, int h) {
        PixelReader reader = sheet.getPixelReader();
        WritableImage out = new WritableImage(w, h);
        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
                int argb = reader.getArgb(x + i, y + j);
                if ((argb & 0x00FFFFFF) == 0x008F00) argb = 0;
                out.getPixelWriter().setArgb(i, j, argb);
            }
        }
        return out;
    }
}