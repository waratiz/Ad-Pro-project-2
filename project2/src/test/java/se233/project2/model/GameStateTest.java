package se233.project2.model;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameStateTest {
    @Test void comboMultipliesScoreAndIncrements() {
        GameState gs = new GameState();
        assertEquals(10, gs.addBrickScore(10, 1.0));   // x1combo
        assertEquals(20, gs.addBrickScore(10, 1.0));   // x2combo
        assertEquals(30, gs.getScore());
        assertEquals(3, gs.getCombo());
    }

    @Test void bonusPowerUpAdds50Percent() {
        assertEquals(15, new GameState().addBrickScore(10, 1.5));
    }

    @Test void losingLifeResetsComboAndSignalsGameOver() {
        GameState gs = new GameState();
        gs.addBrickScore(10, 1.0);            // combo -> 2
        assertFalse(gs.loseLife());           // life 2
        assertEquals(1, gs.getCombo());       // combo reset
        assertFalse(gs.loseLife());           // life 1
        assertTrue(gs.loseLife());            // life 0 -> game over
    }

    @Test void extraLifeAddsOne() {
        GameState gs = new GameState();
        gs.gainLife();
        assertEquals(4, gs.getLife());
    }

    @Test void resetCombo() {
        GameState gs = new GameState();
        gs.addBrickScore(10, 1.0);
        gs.addBrickScore(10, 1.0);
        gs.resetCombo();
        assertEquals(1, gs.getCombo());
        assertEquals(30, gs.getScore());     // คะแนนไม่หาย
    }

    @Test void startingValues() {
        GameState gs = new GameState();
        assertEquals(0, gs.getScore());
        assertEquals(3, gs.getLife());
        assertEquals(1, gs.getCombo());      // กันบั๊ก combo = 0 ที่เคยเจอ
    }

    @Test void bonusAndComboStack() {
        GameState gs = new GameState();
        gs.addBrickScore(10, 1.5);           // 15, combo -> 2
        assertEquals(30, gs.addBrickScore(10, 1.5));   // 10 x 1.5 x 2
    }

    @Test void bonusRoundsToNearestInt() {
        // 30 x 1.5 = 45 ตรงๆ; ลองค่าที่เป็นทศนิยม .5 เพื่อดู Math.round
        assertEquals(8, new GameState().addBrickScore(5, 1.5));   // 7.5 -> 8
    }
}
