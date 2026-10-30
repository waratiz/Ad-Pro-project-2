package se233.project2.util;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.*;

public class SpriteSheet {
    private final Image image;

    public SpriteSheet(String path) {
        var stream = getClass().getResourceAsStream(path);
        if (stream == null) throw new IllegalStateException("can't find img: " + path);
        image = new Image(stream);
    }

    //ตัดชิ้นส่วนจาก sprite sheet มาแสดงเป็น ImageView โดยไม่สร้างรูปใหม่
    public ImageView sprite(double x, double y, double w, double h) {
        ImageView iv = new ImageView(image);
        iv.setViewport(new Rectangle2D(x, y, w, h));
        iv.setSmooth(false);
        return iv;
    }

    /** ตัดส่วนที่ต้องการ และทำให้สีเขียวพื้นหลังโปร่งใส */
    public ImageView spriteKeyed(int x, int y, int w, int h) {
        PixelReader reader = image.getPixelReader();
        WritableImage out = new WritableImage(w, h);
        PixelWriter writer = out.getPixelWriter();
        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
                int argb = reader.getArgb(x + i, y + j);
                if ((argb & 0x00FFFFFF) == 0x008F00) argb = 0;
                writer.setArgb(i, j, argb);
            }
        }
        ImageView iv = new ImageView(out);
        iv.setSmooth(false);
        return iv;
    }
}