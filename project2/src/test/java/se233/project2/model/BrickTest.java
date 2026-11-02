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
}
