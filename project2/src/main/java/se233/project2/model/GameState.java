package se233.project2.model;

public class GameState {
    private int score, life = 3, combo = 1;   // combo starts at 1, not 0, or the first brick is worth nothing

    // returns the points gained, then combo goes up 1
    public int addBrickScore(int base, double bonusMultiplier) {
        int gained = (int) Math.round(base * bonusMultiplier * combo); // point calc
        score += gained;
        combo++;
        return gained;
    }

    public void resetCombo() { combo = 1; }
    public void gainLife()   { life++; }

    // returns true when no lives are left (game over). also resets the combo
    public boolean loseLife() {
        life--;
        combo = 1;
        return life <= 0;
    }

    //settor and getter
    public int getScore() { return score; }
    public int getLife()  { return life; }
    public int getCombo() { return combo; }
}

