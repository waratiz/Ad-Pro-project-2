package se233.project2.model;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;

public class Paddle {
    private double x;                    // position. measured top left as always
    private final double y = 208;        // same. but it only slide sideway so we dont touch it. fyi, finetune later
    private double width = 28;      
    private final double height = 8;
    //its width and height

    //settors and getters
    public double getX() { return x; }
    public void setX(double x) { this.x = x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public void setWidth(double w) { this.width = w; }
    public double getHeight() { return height; }
    public double getCenterX() { return x + width / 2; }
    public Bounds getBounds() { return new BoundingBox(x, y, width, height); }
}