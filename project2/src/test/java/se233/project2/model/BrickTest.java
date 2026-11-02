package se233.project2.model;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BrickTest {
    @Test void normalBrickDiesInOneHit() {
        assertTrue(new Brick(0, 0, Brick.BrickType.NORMAL).hit());
    }
    @Test void strongBrickNeedsThreeHits() {
        Brick b = new Brick(0, 0, Brick.BrickType.STRONG);
        assertFalse(b.hit());
        assertFalse(b.hit());
        assertTrue(b.hit());
    }
    @Test void hmBrickIsIndestructible() {
        Brick b = new Brick(0, 0, Brick.BrickType.HM);
        for (int i = 0; i < 1000; i++) assertFalse(b.hit());
        assertFalse(b.isRequired());
    }
    @Test void bonusBrickDiesInOneHitAndGivesFiftyPoints() {
        Brick b = new Brick(0, 0, Brick.BrickType.BONUS);
        assertEquals(50, b.getScore());
        assertTrue(b.hit());
    }

    @Test void scoresMatchType() {
        assertEquals(10, new Brick(0, 0, Brick.BrickType.NORMAL).getScore());
        assertEquals(30, new Brick(0, 0, Brick.BrickType.STRONG).getScore());
        assertEquals(0,  new Brick(0, 0, Brick.BrickType.HM).getScore());
    }

    @Test void requiredFlagsMatchType() {
        assertTrue(new Brick(0, 0, Brick.BrickType.NORMAL).isRequired());
        assertTrue(new Brick(0, 0, Brick.BrickType.STRONG).isRequired());
        assertTrue(new Brick(0, 0, Brick.BrickType.BONUS).isRequired());
        assertFalse(new Brick(0, 0, Brick.BrickType.HM).isRequired());
    }

    @Test void boundsUseFixedBrickSize() {
        Brick b = new Brick(32, 16, Brick.BrickType.NORMAL);
        assertEquals(32, b.getBounds().getMinX(), 1e-9);
        assertEquals(16, b.getBounds().getMinY(), 1e-9);
        assertEquals(Brick.WIDTH, b.getBounds().getWidth(), 1e-9);
        assertEquals(Brick.HEIGHT, b.getBounds().getHeight(), 1e-9);
    }
}
