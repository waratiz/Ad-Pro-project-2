package se233.project2.model;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BallTest {
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
