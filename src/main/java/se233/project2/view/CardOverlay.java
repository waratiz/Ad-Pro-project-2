package se233.project2.view;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import se233.project2.model.PowerUp;
import se233.project2.util.SpriteSheet;

import java.util.List;
import java.util.function.Consumer;

public class CardOverlay extends Pane {

    private static final double CARD_W = 60, CARD_H = 96, GAP = 8, CARD_TOP = 72;
    private static final int ICON_W = 16, ICON_H = 8;
    private static final Font TITLE = Font.font("Monospaced", FontWeight.BOLD, 8);
    private static final Font BODY  = Font.font("Monospaced", FontWeight.BOLD, 6);

    private final SpriteSheet icons;

    public CardOverlay(SpriteSheet icons) {
        this.icons = icons;
        setPrefSize(GameView.W, GameView.H);
        setVisible(false);
    }

    /** Shows the choice cards. onPick is called with the card the player selects. */
    public void showCards(List<PowerUp> choices, Consumer<PowerUp> onPick) {
        reset();
        addText("STAGE CLEAR!", Color.RED, 40);
        addText("CHOOSE A POWER-UP", Color.WHITE, 54);

        double totalW = choices.size() * CARD_W + (choices.size() - 1) * GAP;
        double startX = (GameView.W - totalW) / 2;
        for (int i = 0; i < choices.size(); i++) {
            getChildren().add(buildCard(i, choices.get(i), startX + i * (CARD_W + GAP), onPick));
        }
        setVisible(true);
    }

    /** Shows one centered line of text (game over / victory). */
    public void showMessage(String text) {
        reset();
        addText(text, Color.WHITE, GameView.H / 2);
        setVisible(true);
    }

    public void hide() {
        setVisible(false);
        getChildren().clear();
    }

    private void reset() {
        getChildren().clear();
        getChildren().add(new Rectangle(GameView.W, GameView.H, Color.rgb(0, 0, 0, 0.8)));
    }

    private Pane buildCard(int index, PowerUp p, double x, Consumer<PowerUp> onPick) {
        Pane card = new Pane();
        card.setLayoutX(x);
        card.setLayoutY(CARD_TOP);
        card.setPrefSize(CARD_W, CARD_H);

        Rectangle bg = new Rectangle(CARD_W, CARD_H);
        bg.setFill(Color.web("#10104a"));
        bg.setStroke(Color.WHITE);

        // first frame only: x = 0, y = row * 8
        ImageView icon = icons.sprite(0, p.getSpriteRow() * ICON_H, ICON_W, ICON_H);
        icon.setFitWidth(ICON_W * 2);
        icon.setFitHeight(ICON_H * 2);
        icon.setLayoutX((CARD_W - ICON_W * 2) / 2);
        icon.setLayoutY(12);

        Text title = new Text(p.getTitle());
        title.setFont(TITLE);
        title.setFill(Color.YELLOW);
        title.setLayoutX((CARD_W - title.getLayoutBounds().getWidth()) / 2);
        title.setLayoutY(48);

        Text desc = new Text(p.getDescription());
        desc.setFont(BODY);
        desc.setFill(Color.WHITE);
        desc.setWrappingWidth(CARD_W - 8);
        desc.setTextAlignment(TextAlignment.CENTER);
        desc.setLayoutX(4);
        desc.setLayoutY(62);

        Text key = new Text("[" + (index + 1) + "]");
        key.setFont(BODY);
        key.setFill(Color.LIGHTGRAY);
        key.setLayoutX((CARD_W - key.getLayoutBounds().getWidth()) / 2);
        key.setLayoutY(CARD_H - 6);

        card.getChildren().addAll(bg, icon, title, desc, key);

        card.setOnMouseEntered(e -> bg.setStroke(Color.YELLOW));
        card.setOnMouseExited(e -> bg.setStroke(Color.WHITE));
        card.setOnMouseClicked(e -> onPick.accept(p));
        return card;
    }

    private void addText(String s, Color color, double baselineY) {
        Text t = new Text(s);
        t.setFont(TITLE);
        t.setFill(color);
        t.setLayoutX((GameView.W - t.getLayoutBounds().getWidth()) / 2);
        t.setLayoutY(baselineY);
        getChildren().add(t);
    }
}