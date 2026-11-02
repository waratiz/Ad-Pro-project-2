package se233.project2.model;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BallTest {
    @Test void updateDoesNothingBeforeLaunch() {
        Ball ball = new Ball();
        ball.stickTo(100, 200);
        double x0 = ball.getX(), y0 = ball.getY();
        ball.update(1.0);
        assertEquals(x0, ball.getX(), 1e-9);
        assertEquals(y0, ball.getY(), 1e-9);
    }

    @Test void launchAtPositiveAngleGoesRight() {
        Ball ball = new Ball();
        ball.launch(100, 90);
        assertEquals(100, ball.getVx(), 1e-9);
        assertEquals(0, ball.getVy(), 1e-9);
    }

    @Test void setSpeedKeepsDirection() {
        Ball ball = new Ball();
        ball.launch(100, 30);
        double ratio = ball.getVx() / ball.getVy();
        ball.setSpeed(200);
        assertEquals(200, ball.speed(), 1e-9);
        assertEquals(ratio, ball.getVx() / ball.getVy(), 1e-9);
    }

    @Test void setSpeedOnStationaryBallDoesNothing() {
        Ball ball = new Ball();
        ball.setSpeed(50);
        assertEquals(0, ball.speed(), 1e-9);
    }

    @Test void stickToCentersBallOnPaddle() {
        Ball ball = new Ball();
        ball.stickTo(100, 200);
        assertEquals(100 - ball.getWidth() / 2, ball.getX(), 1e-9);
        assertEquals(200 - ball.getHeight(), ball.getY(), 1e-9);
    }

    @Test void launchStraightUpHasNegativeVy() {
        Ball ball = new Ball();
        ball.launch(180, 0);
        assertEquals(0, ball.getVx(), 1e-9);
        assertEquals(-180, ball.getVy(), 1e-9);
        assertTrue(ball.isLaunched());
    }
    @Test void updateMovesByVelocityTimesDt() {
        Ball ball = new Ball();
        ball.stickTo(100, 200);
        ball.launch(100, 0);
        double y0 = ball.getY();
        ball.update(0.5);
        assertEquals(y0 - 50, ball.getY(), 1e-9);
    }
    @Test void stickToResetsVelocity() {
        Ball ball = new Ball();
        ball.launch(180, 15);
        ball.stickTo(50, 100);
        assertFalse(ball.isLaunched());
        assertEquals(0, ball.speed(), 1e-9);
    }
}
