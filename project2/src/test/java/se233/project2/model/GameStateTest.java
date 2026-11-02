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
        gs.addBrickScore(10, 1.0);
        assertFalse(gs.loseLife());
        assertEquals(1, gs.getCombo());
        gs.loseLife();
        assertTrue(gs.loseLife());                     // 3rd loss = game over
    }

    @Test void extraLifeAddsOne() {
        GameState gs = new GameState();
        gs.gainLife();
        assertEquals(4, gs.getLife());
    }
}
