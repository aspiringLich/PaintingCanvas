package paintingcanvas.drawable;

import paintingcanvas.misc.Anchor;
import paintingcanvas.misc.Misc;

import java.awt.*;

/**
 * <p>
 *     A Text element, used for drawing text on the canvas.
 * </p>
 * <p>
 *     Anchors work a bit different for text elements. {@link Anchor#CENTER} centers the text at the baseline. This
 *     requires {@link Anchor#TOP_CENTER} to differ by the ascent of the text, and {@link Anchor#BOTTOM_CENTER} to differ
 *     by the descent.
 * </p>
 *
 * <pre>{@code
 * Text text = new Text(100, 100, "Hello World");
 * }</pre>
 */
@SuppressWarnings("unused")
public class Text extends DrawableBase<Text> implements Anchorable<Text> {
    /**
     * The font of the text, you can change this if you want a different font (and have it installed)
     */
    public Font font = new Font("Comic Sans MS", Font.BOLD, 12);
    /**
     * The text to draw
     */
    public String text;
    Anchor anchor = Anchor.CENTER;

    /**
     * <p>
     * Create a new Text element.
     * The default font size is 30, and the default font is comic sans :)
     * </p><p>
     * It probably won't work on replit however. Comic sans is not installed on
     * the replit servers by default.
     * </p>
     * <pre>{@code
     * Text text = new Text(100, 100, "Hello World")
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


    /**
     * <p>
     * Create a new Text element.
     * The default font size is 30, and the default font is comic sans :)
     * </p><p>
     * It probably won't work on replit however. Comic sans is not installed on
     * the replit servers by default.
     * </p>
     * <pre>{@code
     * Text text = new Text(100, 100, "Hello World", new Color(255, 0, 0))
     * }</pre>
     *
     * @param x     The X-position of the text
     * @param y     The Y-position of the text
     * @param text  The text
     * @param color the color of the text
     */
    public Text(int x, int y, String text, Color color) {
        super(x, y, color);
        this.text = text;
    }

    /**
     * <p>
     * Create a new Text element with a hue name or hex code
     *
     * @param x     The X-position of the text
     * @param y     The Y-position of the text
     * @param text  The text
     * @param color The name of the color of the text (case-insensitive)
     * @see Misc#stringToColor(String)
     * <p>
     * The default font size is 30, and the default font is comic sans :)
     * </p><p>
     * It probably won't work on replit however. Comic sans is not installed on
     * the replit servers by default.
     * </p>
     * <pre>{@code
     * Text text = new Text(100, 100, "Hello World", "red")
     * }</pre>
     */
    public Text(int x, int y, String text, String color) {
        this(x, y, text, Misc.stringToColor(color));
    }

    private int width, height;
    private FontMetrics metrics;

    @Override
    void draw(Graphics2D g) {
        metrics = g.getFontMetrics(font);

        width = metrics.stringWidth(text);
        height = metrics.getHeight();
        var ascent = metrics.getAscent();
        var descent = metrics.getDescent();


        var dy = 0.0;
        if (anchor.y > 0) dy = descent * -anchor.y;
        else if (anchor.y < 0) dy = ascent * -anchor.y;

        g.setColor(color);
        g.setFont(font);
        g.drawString(
                text,
                (int) (width * (-0.5 - anchor.x)),
                (int) dy
        );
    }


    @Override
    public Point center(Graphics2D g) {
        return new Point(x, y);
    }

    /**
     * <p>
     *     Note that the computed width of the text only gets updated when the text is drawn. (i.e. it does not get
     *     updated immediately)
     * </p>
     *
     * @return The width of the text in pixels.
     */
    public int getWidth() {
        return width;
    }

    /**
     * <p>
     *      Note that the computed height of the text only gets updated when the text is drawn. (i.e. it does not get
     *      updated immediately)
     * </p>
     *
     * @return The height of the text in pixels.
     */
    public int getHeight() {
        return height;
    }

    /**
     * If you need more information about the text, such as ascent, descent, leading, etc. the information can be
     * retrieved from the {@link FontMetrics} object.
     * <p>
     *     Again, note that the metrics are only updated when the text is drawn. (i.e. it does not get
     *     updated immediately)
     * </p>
     *
     * @return The FontMetrics object containing information about the text
     */
    public FontMetrics getFontMetrics() {
       return metrics;
    }

    @Override
    public Text getThis() {
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
     * @see #setFontSize(double)
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
    public Text setFontSize(double size) {
        this.font = font.deriveFont((float) size);
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

    @Override
    public Anchor getAnchor() {
        return this.anchor;
    }

    @Override
    public void internalSetAnchor(Anchor anchor) {
        this.anchor = anchor;
    }
}