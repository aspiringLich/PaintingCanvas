package painter.drawable;

import java.awt.*;

/**
 * Text.
 */
public class Text extends Drawable {
    // you can change this is you would like a different font
    static Font font = new Font("Comic Sans MS", Font.BOLD, 1);

    public String text;
    public float size;

    /**
     * <code>new Text(100, 100, "Some text.", 30)</code>
     *
     * @param x the x-position of the text
     * @param y the y-position of the text
     * @param text a string, for the text to write itself
     * @param size the size (height in px) of the text
     */
    public Text(int x, int y, String text, float size) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.size = size;
    }

    /**
     * Set the size of the text object (basically height, 20 would mean the text is 20 px high)
     *
     * @param size the size in pixels
     * @return The original object to allow method chaining
     */
    public Drawable setSize(float size) {
        this.size = size;
        return this;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.setFont(font.deriveFont(size));

        g.drawChars(
                text.toCharArray(),
                0,
                text.length(),
                x,
                y
        );
    }
}