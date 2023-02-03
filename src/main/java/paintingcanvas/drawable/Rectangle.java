package paintingcanvas.drawable;

import java.awt.*;

/**
 * A Rectangle element.
 * <pre>{@code
 * // Create a new Rectangle at (100, 100) with a width of 20 and a height of 30
 * Rectangle rectangle = new Rectangle(100, 100, 20, 30);
 * }</pre>
 */
public class Rectangle extends Drawable<Rectangle> {
    public int width;
    public int height;

    /**
     * Create a new Rectangle element.
     * <pre>{@code
     * // Create a new Rectangle at (100, 100) with a width of 20 and a height of 30
     * Rectangle rectangle = new Rectangle(100, 100, 20, 30);
     * }</pre>
     *
     * @param x The X-position of the rectangle
     * @param y The Y-position of the rectangle
     * @param w The width of the rectangle
     * @param h The height of the rectangle
     */
    public Rectangle(int x, int y, int w, int h) {
        super(x, y, Color.BLACK);
        this.width = w;
        this.height = h;
    }


    /**
     * Create a new Rectangle element.
     * <pre>{@code
     * // Create a new Rectangle at (100, 100) with a width of 20 and a height of 30
     * Rectangle rectangle = new Rectangle(100, 100, 20, 30);
     * }</pre>
     *
     * @param x     The X-position of the rectangle
     * @param y     The Y-position of the rectangle
     * @param w     The width of the rectangle
     * @param h     The height of the rectangle
     * @param color The color of the rectangle
     */
    public Rectangle(int x, int y, int w, int h, Color color) {
        super(x, y, color);
        this.width = w;
        this.height = h;
    }

    @Override
    public void draw(Graphics2D gc) {
        gc.setColor(color);
        if (this.filled) gc.fillRect(x, y, width, height);
        else gc.drawRect(x, y, width, height);
    }

    @Override
    public Point center(Graphics g) {
        return new Point(x + width / 2, y + height / 2);
    }

    @Override
    protected Rectangle getThis() {
        return this;
    }

    /**
     * Gets the width of the rectangle.
     *
     * @return The width of the rectangle in pixels
     * @see #setWidth(int)
     * @see #getHeight()
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Sets the width of the rectangle.
     *
     * @param w The new width of the rectangle in pixels
     * @return The original object to allow method chaining
     * @see #getWidth()
     * @see #setHeight(int)
     */
    public Rectangle setWidth(int w) {
        this.width = w;
        return this;
    }

    /**
     * Gets the height of the rectangle.
     *
     * @return The height of the rectangle in pixels
     * @see #getWidth()
     * @see #setHeight(int)
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Sets the height of the rectangle.
     *
     * @param h The new height of the rectangle in pixels
     * @return The original object to allow method chaining
     * @see #getHeight()
     * @see #setWidth(int)
     */
    public Rectangle setHeight(int h) {
        this.height = h;
        return this;
    }
}