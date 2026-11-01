package se233.project2.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.util.Duration;
import se233.project2.model.Ball;
import se233.project2.model.Brick;
import se233.project2.model.Paddle;
import se233.project2.model.PowerUp;
import se233.project2.view.GameView;


public class GameController {

    private enum State { PLAYING, SELECTING, FINISHED }   // what the game is doing right now

    private static final double BALL_SPEED = 180;      
    private static final double PADDLE_SPEED = 180;    
    private static final double BONUS_MULTIPLIER = 1.5;   // score mult
    private static final double WALL_LEFT = GameView.WALL_LEFT,
                            WALL_RIGHT = GameView.WALL_RIGHT,
                            WALL_TOP = GameView.WALL_TOP;
    private AnimationTimer loop;

    // list of power up thats finished rn
    private static final List<PowerUp> POOL =
            List.of(PowerUp.EXPAND, PowerUp.EXTRA_LIFE, PowerUp.BONUS_POINTS);

    private final GameView view = new GameView();
    private final Scene scene = new Scene(view);

    private final Ball ball = new Ball();
    private final Paddle paddle = new Paddle();

    private int score, life = 3, stage = 1, combo = 0;//put initial combo here


    private final Set<KeyCode> keys = new HashSet<>();
    private final Set<PowerUp> active = EnumSet.noneOf(PowerUp.class);   // make a set specifically for powerups. starting with 0 active
    private State state = State.PLAYING;
    private List<PowerUp> choices = List.of();
    private long last = 0;

    public GameController() {
        paddle.setX(GameView.W / 2 - paddle.getWidth() / 2);
        ball.stickTo(paddle.getCenterX(), paddle.getY());

        scene.setOnKeyPressed(e -> {
        keys.add(e.getCode());
        //make it start a lil bent so it doesnt ping pong infinitely
        if (e.getCode() == KeyCode.SPACE && state == State.PLAYING && !ball.isLaunched()) ball.launch(BALL_SPEED, 15);
        if (state == State.SELECTING) {                   // picking cards with 1 2 3
            int i = switch (e.getCode()) {
                case DIGIT1 -> 0;
                case DIGIT2 -> 1;
                case DIGIT3 -> 2;
                default -> -1;
            };
            if (i >= 0 && i < choices.size()) pick(choices.get(i));
        }
    });
        scene.setOnKeyReleased(e -> keys.remove(e.getCode()));

            loop = new AnimationTimer() {
            @Override public void handle(long now) {
                if (last == 0) { last = now; return; }
                //calc in nanosecond for accuracy
                double dt = Math.min((now - last) / 1e9, 0.05);
                last = now;
                update(dt);
            }
        };
        loop.start();
    }
    public Scene getScene() {
        return scene;
    }

    public GameView getView() {
        return view;
    }

    void update(double dt) {
        if (state != State.PLAYING) {                     // cards or win/lose, paused the game
            syncView();
            return;
        }

        // 1. input -> paddle
        double dir = 0;
        if (keys.contains(KeyCode.LEFT))  dir -= 1;
        if (keys.contains(KeyCode.RIGHT)) dir += 1;
        double x = paddle.getX() + dir * PADDLE_SPEED * dt;
        paddle.setX(Math.max(0, Math.min(x, GameView.W - paddle.getWidth())));
        paddle.setX(clamp(x, WALL_LEFT, WALL_RIGHT - paddle.getWidth()));

        // 2. ball
        if (!ball.isLaunched()) {
            ball.stickTo(paddle.getCenterX(), paddle.getY());
        } else {
            ball.update(dt);
            // 3. collisions
            collideWalls();
            collidePaddle();
            collideBricks();
            if (isStageCleared()) onStageCleared();
            else checkBallLost();
        }

        // 4. sync to view
        syncView();
    }
    private void collideWalls() {
        //left wall make vx positive so it goes right always
    if (ball.getX() < WALL_LEFT) {
        ball.setX(WALL_LEFT);
        ball.setVx(Math.abs(ball.getVx()));
    }
    //right wall makes it negative to go left
    if (ball.getX() + ball.getWidth() > WALL_RIGHT) {
        ball.setX(WALL_RIGHT - ball.getWidth());
        ball.setVx(-Math.abs(ball.getVx()));
    }
    //top wall makes vy positive to go down
    if (ball.getY() < WALL_TOP) {
        ball.setY(WALL_TOP);
        ball.setVy(Math.abs(ball.getVy()));
    }
    }

    private void collidePaddle() {
        Bounds b = ball.getBounds(), p = paddle.getBounds();
        if (ball.getVy() > 0 && b.intersects(p)) {
            double hit = (b.getCenterX() - p.getCenterX()) / (p.getWidth() / 2);
            hit = Math.max(-1, Math.min(1, hit));
            double angle = hit * Math.toRadians(60);
            double s = ball.speed();
            ball.setVx(s * Math.sin(angle));
            ball.setVy(-s * Math.cos(angle));
            ball.setY(p.getMinY() - ball.getHeight());
            //reset them combo and make it reflect in view
            combo = 1 ;
            view.setCombo(combo);
        }
    }

    private void collideBricks() {
    Bounds b = ball.getBounds();
    Iterator<ImageView> it = view.getBricks().iterator();
    while (it.hasNext()) {
        // loop over every brick
        ImageView node = it.next();
        // get the property from node
        Brick brick = (Brick) node.getUserData();
        Bounds r = brick.getBounds();
        //check if ball hit or nah
        if (!b.intersects(r)) continue;

        // check where hit
        double ox = Math.min(b.getMaxX(), r.getMaxX()) - Math.max(b.getMinX(), r.getMinX());
        double oy = Math.min(b.getMaxY(), r.getMaxY()) - Math.max(b.getMinY(), r.getMinY());
        if (ox < oy) {                                   // sides
            if (b.getCenterX() < r.getCenterX()) ball.setVx(-Math.abs(ball.getVx()));
            else                                  ball.setVx( Math.abs(ball.getVx()));
        } else {                                         // top/bottom
            if (b.getCenterY() < r.getCenterY()) ball.setVy(-Math.abs(ball.getVy()));
            else                                  ball.setVy( Math.abs(ball.getVy()));
        }

        if (brick.hit()) {                               // check if it die or not
            it.remove();
            view.removeBrickNode(node);
            double bonus = active.contains(PowerUp.BONUS_POINTS) ? BONUS_MULTIPLIER : 1.0;
            //score calc
            score += (int) Math.round(brick.getScore() * bonus * combo); // put combo in the fucking formula as a mult
            //make combo go up by 1, reflect in view
            combo++;
            //why? cuz the fucking combo goes up by 1 a brivk die thats why
            view.setScore(score);
            view.setCombo(combo);
        }
        break;                                           // one brick each time
    }
    }

    private boolean isStageCleared() {
        // check for bricks
    for (ImageView n : view.getBricks()) {
        //if breakable brick still left, not win yet
        if (((Brick) n.getUserData()).isRequired()) return false;
    }
    return true;
    }

    private void checkBallLost() {
    if (ball.getY() > GameView.H) {
        life--;
        //reset the damn combo and reflect in view
        combo = 1;
        view.setLife(life);
        view.setCombo(combo);
        if (!active.contains(PowerUp.EXPAND)) resetPaddle();   // power-ups end when you lose a life
        ball.stickTo(paddle.getCenterX(), paddle.getY());
        //if life is 0, game over. show message. paused game
        if (life <= 0) {
            state = State.FINISHED;
            view.showMessage("GAME OVER   SCORE " + score);
            showSummary("GAME OVER");
        }
    }
    }

    private void onStageCleared() {
        ball.stickTo(paddle.getCenterX(), paddle.getY());      // get balls back on them paddle

        if (stage >= view.getTotalStages()) {                  // if its last stage, show win. pause game
            state = State.FINISHED;
            view.showMessage("YOU WIN!   SCORE " + score);
            showSummary("YOU WIN!");
            return;
        }

        // perma power up, if its not active. it got added in. except extra life, thats a one off ones
        choices = new ArrayList<>();
        for (PowerUp p : POOL) {
            if (!active.contains(p)) choices.add(p);
        }
        state = State.SELECTING;
        view.showCards(choices, this::pick);
    }

        private void showSummary(String result) {
        PauseTransition wait = new PauseTransition(Duration.seconds(1.5));   // wait for them to process their loss
        wait.setOnFinished(e -> {
            loop.stop();                                      // when game is finished, stop the game loop so no more process
            try {
                //go to summary screen
                FXMLLoader fxml = new FXMLLoader(getClass().getResource("/Summary.fxml"));
                Parent root = fxml.load();
                Summary summary = fxml.getController();
                summary.setResult(result);
                summary.setScore(score);

                Stage stage = (Stage) scene.getWindow();
                stage.setScene(new Scene(root));
                stage.sizeToScene();
                stage.centerOnScreen();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        wait.play();
    }

    private void pick(PowerUp p) {
        if (state != State.SELECTING) return;                  // make sure it doesnt bug and apply shit more than once
        view.hideCards();
        applyPowerUp(p);

        stage++;
        view.loadStage(stage);
        //as i said, reset the damn combo and set it in view
        //theres a fucking set combo function for fuck sake
        combo = 1;
        view.setCombo(combo);
        paddle.setX(clamp(GameView.W / 2 - paddle.getWidth() / 2, WALL_LEFT, WALL_RIGHT - paddle.getWidth()));
        ball.stickTo(paddle.getCenterX(), paddle.getY());
        state = State.PLAYING;
    }

    private void applyPowerUp(PowerUp p) {
        switch (p) {
            case EXPAND -> { active.add(p); expandPaddle(); }
            case BONUS_POINTS -> active.add(p);
            case EXTRA_LIFE -> { life++; view.setLife(life); }
        }
    }

    private void setPaddleWidth(double newW, int spriteX) {
    double cx = paddle.getCenterX();                     // kept the center pos
    paddle.setWidth(newW);                               // well, the thing got a wider hitbox now
    paddle.setX(clamp(cx - newW / 2, WALL_LEFT, WALL_RIGHT - newW));
    view.setPaddleSprite(spriteX, 0, (int) newW, 8);     // change to a wide sprite
    }

    private void expandPaddle() { setPaddleWidth(44, 66); } // thats where the sprite located. top left as always
    private void resetPaddle()  { setPaddleWidth(28, 34); } // normal Vaus

    private static double clamp(double v, double lo, double hi) {
        //ok so, here we have some bullshit
        //v is the pos thats on the top left because of course its there
        //lo is minimum pos so it doesnt clip into left wall
        //hi is just that but for right wall
        //thats why its low and high respectively
        //so v get force min and max if it doesnt fit
    return Math.max(lo, Math.min(v, hi));
    }

    private void syncView() {
        view.getPaddle().setLayoutX(Math.round(paddle.getX()));
        view.getBall().setLayoutX(Math.round(ball.getX()));
        view.getBall().setLayoutY(Math.round(ball.getY()));
    }

    
}