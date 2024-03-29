package paintingcanvas.drawable;

import paintingcanvas.misc.Misc;

import java.awt.*;

/**
 * An <a href="https://en.wikipedia.org/wiki/Ellipse">ellipse</a> element.
 * <pre>{@code
 * // Create a new ellipse at (100, 100) with width 20 and height 30
 * Ellipse ellipse = new Ellipse(100, 100, 20, 30);
 * }</pre>
 */
public class Ellipse extends Shape<Ellipse> {
    int width;
    int height;

    /**
     * Create a new Ellipse element.
     * <pre>{@code
     * // Create a new ellipse centered at (100, 100) with width 20 and height 30
     * Ellipse ellipse = new Ellipse(100, 100, 20, 30);
     * }</pre>
     *
     * @param centerX The X-position of the ellipse
     * @param centerY The Y-position of the ellipse
     * @param width   The width of the ellipse
     * @param height  The height of the ellipse
     */
    public Ellipse(int centerX, int centerY, int width, int height) {
        super(centerX, centerY, Color.BLACK);
        this.width = width;
        this.height = height;
    }

    /**
     * Create a new Ellipse element.
     * <pre>{@code
     * // Create a new red ellipse centered at (100, 100) with width 20 and height 30
     * Ellipse ellipse = new Ellipse(100, 100, 20, 30, new Color(255, 0, 0));
     * }</pre>
     *
     * @param centerX The X-position of the ellipse
     * @param centerY The Y-position of the ellipse
     * @param width   The width of the ellipse
     * @param height  The height of the ellipse
     * @param color   The color of the ellipse
     */
    public Ellipse(int centerX, int centerY, int width, int height, Color color) {
        super(centerX, centerY, color);
        this.width = width;
        this.height = height;
    }

    /**
     * Create a new Ellipse element with a hue name or hex code
     *
     * @param centerX The X-position of the ellipse
     * @param centerY The Y-position of the ellipse
     * @param width   The width of the ellipse
     * @param height  The height of the ellipse
     * @param color   The name of the color (case-insensitive)
     * @see Misc#stringToColor(String)
     * <pre>{@code
     * // Create a new red ellipse centered at (100, 100) with width 20 and height 30
     * Ellipse ellipse = new Ellipse(100, 100, 20, 30, "red");
     * }</pre>
     */
    public Ellipse(int centerX, int centerY, int width, int height, String color) {
        this(centerX, centerY, width, height, Misc.stringToColor(color));
    }

    @Override
    protected void drawOutline(Graphics2D gc) {
        gc.drawOval(-width / 2, -height / 2, width, height);
    }

    @Override
    protected void drawFill(Graphics2D gc) {
        gc.fillOval(-width / 2, -height / 2, width, height);
    }

    @Override
    public Point center(Graphics2D g) {
        return getPos();
    }

    @Override
    public Ellipse getThis() {
        return this;
    }

    /**
     * Gets the width of the ellipse.
     *
     * @return The width of the ellipse in pixels
     * @see #getHeight()
     * @see #setWidth(int)
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the width of the ellipse.
     *
     * @param w The new width of the ellipse in pixels
     * @return The original object to allow method chaining
     * @see #setHeight(int)
     * @see #getWidth()
     */
    public Ellipse setWidth(int w) {
        width = w;
        return this;
    }

    /**
     * Gets the height of the ellipse.
     *
     * @return The height of the ellipse in pixels
     * @see #getWidth()
     * @see #setHeight(int)
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the height of the ellipse.
     *
     * @param h The new height of the ellipse in pixels
     * @return The original object to allow method chaining
     * @see #setWidth(int)
     * @see #getHeight()
     */
    public Ellipse setHeight(int h) {
        height = h;
        return this;
    }
}
