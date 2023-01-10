package painter.drawable;

import java.awt.*;

/**
 * Text.
 */
public class Text extends Drawable {
    // you can change this is you would like a different font
    // *comic sans ms* oh god, latest its not papyrus
    static Font font = new Font("Comic Sans MS", Font.BOLD, 1);

    public String text;
    public float size;

    /**
     * <code>new Text(100, 100, "Some text.", 30)</code>
     *
     * @param x    the x-position of the text
     * @param y    the y-position of the text
     * @param text a string, for the text to write itself
     * @param size the size (height in px) of the text
     */
    public Text(int x, int y, String text, float size) {
        super(x, y, Color.BLACK);
        this.text = text;
        this.size = size;
    }

    /**
     * Set the font size of the text
     *
     * @param size the size in points
     * @return The original object to allow method chaining
     */
    public Drawable setSize(float size) {
        this.size = size;
        return this;
    }

    @Override
    public void draw(Graphics g) {
        var gc = (Graphics2D)g;
        gc.setColor(color);
        gc.setFont(font.deriveFont(size));
        gc.drawString(text, x, y);
    }
}