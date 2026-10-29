package se233.project2.util;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SpriteSheet {
    private final Image image;
    public SpriteSheet(String path) {
        // ปิด smoothing ภายหลังที่ ImageView เพื่อให้ pixel art คมชัด
        image = new Image(getClass().getResourceAsStream(path));
    }

    public ImageView sprite(double x, double y, double w, double h) {
        ImageView iv = new ImageView(image);
        iv.setViewport(new Rectangle2D(x, y, w, h));
        iv.setSmooth(false);          // สำคัญมากสำหรับ pixel art
        return iv;
    }
}