package se233.project2.model;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;

public class Ball {
    private double x, y;                 // Position and stuff(located at top left corner of the sprite)
    private double vx, vy;               // velocity for x and y axis
    private final double width = 5, height = 4; // still self explainatory
    private boolean launched = false; //self explainatory

    public void update(double dt) {
        if (!launched) return;
        x += vx * dt;
        y += vy * dt;
    }

    public void launch(double speed, double angleDeg) {
        //pizza slice round the circle. full circle is 2pi radian. half is pi radian
        //doesnt accept degree for whatever reason
        double a = Math.toRadians(angleDeg);
        vx = speed * Math.sin(a);
        vy = -speed * Math.cos(a);
        launched = true;
    }

    public void stickTo(double paddleCenterX, double paddleTop) {
        //sticks ball to the middle of the paddle till launch
        //only when starting levels tho
        x = paddleCenterX - width / 2;
        y = paddleTop - height;
        vx = 0;
        vy = 0;
        launched = false;
    }

    public Bounds getBounds() { return new BoundingBox(x, y, width, height); } //hitbox basically. position is calc at top left. width height self explainatory
    public boolean isLaunched() { return launched; } // self explainatory

    public double speed() { return Math.hypot(vx, vy); } // this return speed using the pythagory thingy. yknow, square root of the xv^2 + yv^2

    public void setSpeed(double s) {
        //alright, so this one is basically: you set speed to s without direction goes to shit
        double current = speed();
        if (current == 0) return;
        double k = s / current;
        vx *= k;
        vy *= k;
    }

    //settors and getters
    public double getX() { return x; }
    public void setX(double x) { this.x = x; }
    public double getY() { return y; }
    public void setY(double y) { this.y = y; }
    public double getVx() { return vx; }
    public void setVx(double vx) { this.vx = vx; }
    public double getVy() { return vy; }
    public void setVy(double vy) { this.vy = vy; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
}