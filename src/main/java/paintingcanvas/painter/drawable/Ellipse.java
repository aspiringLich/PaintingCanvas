package paintingcanvas.painter.drawable;

import java.awt.*;

/**
 * An <a href="https://en.wikipedia.org/wiki/Ellipse">ellipse</a> element.
 * <pre>{@code
 * // Create a new ellipse at (100, 100) with width 20 and height 30
 * Ellipse ellipse = new Ellipse(100, 100, 20, 30);
 * }</pre>
 */
public class Ellipse extends Drawable<Ellipse> {
    public int width;
    public int height;

    /**
     * Create a new Ellipse element.
     * <pre>{@code
     * // Create a new ellipse at (100, 100) with width 20 and height 30
     * Ellipse ellipse = new Ellipse(100, 100, 20, 30);
     * }</pre>
     *
     * @param x The X-position of the ellipse
     * @param y The Y-position of the ellipse
     * @param w The width of the ellipse
     * @param h The height of the ellipse
     */
    public Ellipse(int x, int y, int w, int h) {
        super(x, y, Color.BLACK);
        width = w;
        height = h;
    }
    
    @Override
    public void draw(Graphics2D gc) {
        gc.setColor(color);
        if (this.filled) gc.fillOval(x, y, width, height);
        else gc.drawOval(x, y, width, height);
    }

    @Override
    public Point center(Graphics g) {
        return new Point(x + width / 2, y + height / 2);
    }

    @Override
    protected Ellipse getThis() {
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
