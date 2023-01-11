package painter.drawable;

import java.awt.*;

/**
 * Text. Pretty self-explanatory I hope.
 */
public class Text extends Drawable {
    // you can change this is you would like a different font
    // *comic sans ms* oh god, latest its not papyrus
    // hey coner i think you meant "at least"
    static Font font = new Font("Comic Sans MS", Font.BOLD, 1);

    public String text;

    /**
     * <code>new Text(100, 100, "Some text.", 30)</code>
     *
     * @param x    the x-position of the text
     * @param y    the y-position of the text
     * @param text a string, for the text to write itself
     * @param size the size of the text
     */
    public Text(int x, int y, String text, float size) {
        super(x, y, Color.BLACK);
        this.text = text;
        font = font.deriveFont(size);
    }

    /**
     * Set the font size of the text
     *
     * @param size the size in points
     * @return The original object to allow method chaining
     */
    public Drawable setSize(float size) {
        font = font.deriveFont(size);
        return this;
    }

    @Override
    public void draw(Graphics g) {
        var gc = (Graphics2D) g;
        gc.setColor(color);
        gc.setFont(font);
        gc.drawString(text, x, y);
    }

    @Override
    public int centerX(Graphics g) {
        var metrics = g.getFontMetrics(font);
        return x + metrics.stringWidth(text) / 2;
    }

    @Override
    public int centerY(Graphics g) {
        var metrics = g.getFontMetrics(font);
        return y - metrics.getAscent() / 4;
    }
}