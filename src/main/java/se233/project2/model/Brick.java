package se233.project2.model;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;

public class Brick {

    public enum BrickType {
        NORMAL(1, 10, true),
        STRONG(3, 30, true),
        BONUS(1, 50, true),
        HM(Integer.MAX_VALUE, 0, false);
        //hah, hyper muteki goes brrrr

        private final int hitPoints;
        private final int score;
        private final boolean required;

        BrickType(int hitPoints, int score, boolean required) {
            //so uh, how much hp. how much base point. does it need to break to clear stage. in that order
            this.hitPoints = hitPoints;
            this.score = score;
            this.required = required;
        }

        //settors and getters
        public int getHitPoints() { return hitPoints; }
        public int getScore() { return score; }
        public boolean isRequired() { return required; }
    }

    public static final double WIDTH = 16, HEIGHT = 8; //i shouldnt need to say this one

    private final double x, y; //position. measured top left as always
    private final BrickType type; //yeah, brick type.
    private int hitsLeft; //hp remaining

    public Brick(double x, double y, BrickType type) {
        //this is for individual bricks just so you know
        this.x = x;
        this.y = y;
        this.type = type;
        this.hitsLeft = type.getHitPoints();
    }

    public boolean hit() {
        //yeah we only do 1 damage
        if (type == BrickType.HM) return false;   // indestructible
        hitsLeft--;
        return hitsLeft <= 0;
    }

    //settors and getters
    public Bounds getBounds() { return new BoundingBox(x, y, WIDTH, HEIGHT); }
    public double getX() { return x; }
    public double getY() { return y; }
    public BrickType getType() { return type; }
    public int getScore() { return type.getScore(); }
    public boolean isRequired() { return type.isRequired(); }
}