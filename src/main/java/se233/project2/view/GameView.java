package se233.project2.view;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Scale;
import se233.project2.model.Brick;
import se233.project2.model.PowerUp;
import se233.project2.util.SpriteSheet;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GameView extends Pane {

    public static final double WALL_LEFT = 8, WALL_RIGHT = 216, WALL_TOP = 16; // this is wall sprite offset
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
    // พิกัด (x, y) ของอิฐแต่ละแถวใน Blocks.png ขนาด 16x8
    // colors for N (NORMAL) bricks: a row of N bricks takes the next color, row 1 is red
    private static final int[][] NORMAL_SPRITES = {
            {0, 8},   // แดง
            {48, 8},  // เหลือง
            {16, 8},  // น้ำเงิน
            {32, 8},  // ชมพู
            {48, 0}   // เขียว
    };
    private static final int[] STRONG_SPRITE = {0, 16};   // S: silver
    private static final int[] BONUS_SPRITE  = {0, 0};    // B: white
    private static final int[] HM_SPRITE     = {0, 24};   // HM: gold

    // each row is 13 cells separated by spaces. N = normal, S = strong, B = bonus, HM = Hyper Muteki, . = empty
    // a row can sit anywhere, you can have more or fewer rows than 6
    private static final String[][] STAGE_LAYOUTS = {
            {   // stage 1: full rows
                    "N  S  B  HM  A  A  A  "
            },
            {   // stage 2: sum bs
                    "N"
            },
            {   // stage 3: isk anymore bro
                    "N"
            }
    };

    private final Pane world = new Pane();
    private final List<ImageView> bricks = new ArrayList<>();
    private final ImageView paddle;
    private final ImageView ball;
    private final SpriteSheet vaus; // paddle sprite
    private final SpriteSheet blocks; // brick sprite, kept so the next stage can be built later
    private final CardOverlay cards;

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
        blocks = new SpriteSheet(ASSETS + "Blocks.png");
        vaus = new SpriteSheet(ASSETS + "Padding.png"); // so it could change to longer sprite when pick power up


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

        cards = new CardOverlay(new SpriteSheet(ASSETS + "Powerups.png"));
        world.getChildren().add(cards);   // render last so its on top
    }

    public ImageView getPaddle() { return paddle; }
    public ImageView getBall() { return ball; }
    public List<ImageView> getBricks() { return bricks; }
    public int getTotalStages() { return totalStages; }

    private void createBricks(SpriteSheet blocks) {
    double startX = 8, startY = 40;
    String[] layout = STAGE_LAYOUTS[(currentStage - 1) % STAGE_LAYOUTS.length];
    for (int r = 0; r < layout.length; r++) {
        String[] cells = layout[r].trim().split("\\s+");        // "N  N  HM" -> ["N", "N", "HM"]
        for (int c = 0; c < cells.length && c < 13; c++) {
            Brick.BrickType type = parseType(cells[c]);
            if (type == null) continue;                         // if the type not match in bricks, skip
            int[] s = spriteFor(type, r);
            Brick model = new Brick(startX + c * Brick.WIDTH,
                                    startY + r * Brick.HEIGHT,
                                    type);
            ImageView node = blocks.sprite(s[0], s[1], 16, 8);
            node.setLayoutX(model.getX());
            node.setLayoutY(model.getY());
            node.setUserData(model);         // look at brick property when ball collide
            bricks.add(node);
        }
    }
    }

    // turns the text in a layout cell into a brick type, null means no brick
    private static Brick.BrickType parseType(String token) {
        return switch (token.toUpperCase()) {
            case "N"  -> Brick.BrickType.NORMAL;
            case "S"  -> Brick.BrickType.STRONG;
            case "B"  -> Brick.BrickType.BONUS;
            case "HM" -> Brick.BrickType.HM;
            default   -> null;
        };
    }

    // which sprite to draw for a type, row is only used to color the N bricks
    private static int[] spriteFor(Brick.BrickType type, int row) {
        return switch (type) {
            case NORMAL -> NORMAL_SPRITES[(row + NORMAL_SPRITES.length - 1) % NORMAL_SPRITES.length];
            case STRONG -> STRONG_SPRITE;
            case BONUS  -> BONUS_SPRITE;
            case HM     -> HM_SPRITE;
        };
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
    public void removeBrickNode(ImageView b) {
    world.getChildren().remove(b);
    }

    public void setPaddleSprite(int x, int y, int w, int h) {
    ImageView tmp = vaus.spriteKeyed(x, y, w, h);
    paddle.setImage(tmp.getImage());
    paddle.setViewport(tmp.getViewport());
    }

    public void loadStage(int stage) {
        world.getChildren().removeAll(bricks);          // tore off previous stage
        bricks.clear();
        setLevel(stage);                                // read stage and make bricks
        createBricks(blocks);
        world.getChildren().addAll(1, bricks);          // above the background, below paddle and ball
    }

    public void showCards(List<PowerUp> choices, Consumer<PowerUp> onPick) { cards.showCards(choices, onPick); }
    public void hideCards() { cards.hide(); }
    public void showMessage(String text) { cards.showMessage(text); }
}