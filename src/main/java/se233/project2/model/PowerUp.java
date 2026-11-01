package se233.project2.model;

public enum PowerUp {
    //             title                     description                              row in Powerups.png
    EXPAND      ("EXPAND",     "Wider paddle for the whole run",   3),   // blue E
    EXTRA_LIFE  ("EXTRA LIFE", "Gain one life now",                2),   // red  L
    BONUS_POINTS("BONUS PTS",  "+50% points on every brick",       6);   // gray P

    private final String title;
    private final String description;
    private final int spriteRow;

    PowerUp(String title, String description, int spriteRow) {
        this.title = title;
        this.description = description;
        this.spriteRow = spriteRow;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getSpriteRow() { return spriteRow; }
}