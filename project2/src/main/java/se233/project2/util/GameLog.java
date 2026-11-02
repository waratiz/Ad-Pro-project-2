package se233.project2.util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public final class GameLog {
    private static final Logger ROOT = Logger.getLogger("se233.project2");
    static {
        try {
            FileHandler fh = new FileHandler("arkanoid.log", true);  // get all them levels into file
            fh.setFormatter(new SimpleFormatter());
            ROOT.addHandler(fh);
            ROOT.setLevel(Level.ALL);                                // console only get them INFO
        } catch (IOException e) {
            System.err.println("File logging disabled: " + e.getMessage());
        }
    }
    public static Logger get(Class<?> c) { return Logger.getLogger(c.getName()); }
}
