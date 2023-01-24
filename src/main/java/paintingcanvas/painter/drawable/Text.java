package paintingcanvas.painter.drawable;

import paintingcanvas.painter.App;

import java.awt.*;

/**
 * A Text element, used for drawing text on the canvas.
 * <pre>{@code
 * Text text = new Text(100, 100, "Hello World");
 * }</pre>
 */
public class Text extends Drawable<Text> {
    // you can change this is you would like a different font
    // *comic sans ms* oh god, latest its not papyrus
    // hey coner i think you meant "at least"
    public Font font = new Font("Comic Sans MS", Font.BOLD, 1);

    public String text;
    @Override
    public void draw(Graphics2D gc) {
        gc.setColor(color);
        gc.setFont(font);
        gc.drawString(text, x, y);
    }


    @Override
    public Point center(Graphics g) {
        var metrics = g.getFontMetrics(font);
        return new Point(x + metrics.stringWidth(text) / 2, y - metrics.getAscent() / 4);
    }

    /**
     * Create a new Text element.
     * The default font size is 30, and the default font is <u>comic sans</u> <em>(not sorry)</em>.
     * <pre>{@code
     * Text text = new Text(100, 100, "Hello World");
     * }</pre>
     *
     * @param x    The X-position of the text
     * @param y    The Y-position of the text
     * @param text The text
     */
    public Text(int x, int y, String text) {
        super(x, y, Color.BLACK);
        this.text = text;
    }

    @Override
    protected Text getThis() {
        return this;
    }
    
    /**
     * Get the current font size of the text.
     * <pre>{@code
     * Text text = new Text(100, 100, "Hello World");
     * text.setFontSize(40); // Set font size to 40 points
     * System.out.println(text.getFontSize()); // Prints 40
     * }</pre>
     *
     * @return The font size in points
     * @see #setFontSize(float)
     */
    public int getFontSize() {
        return this.font.getSize();
    }
    
    /**
     * Set the current font size of the text.
     * <pre>{@code
     * Text text = new Text(100, 100, "Hello World");
     * text.setFontSize(40); // Set font size to 40 points
     * System.out.println(text.getFontSize()); // Prints 40
     * }</pre>
     *
     * @param size the new font size of the text
     * @return The original object to allow method chaining
     * @see #getFontSize()
     */
    public Text setFontSize(float size) {
        this.font = font.deriveFont(size);
        return this;
    }

    /**
     * Gets the current text of the element.
     * <pre>{@code
     * Text text = new Text(100, 100, "Hello World");
     * System.out.println(text.getText()); // => Hello World
     * }</pre>
     *
     * @return The text as a {@link String}
     * @see #setText(String)
     */
    public String getText() {
        return this.text;
    }

    /**
     * Sets the text of the element.
     * <pre>{@code
     * Text text = new Text(100, 100, "Hello World");
     * text.setText("Go Go Mango");
     * }</pre>
     *
     * @param text The new text for the element as a {@link String}
     * @return The original object to allow method chaining
     */
    public Text setText(String text) {
        this.text = text;
        return this;
    }
}