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
    /**
     * The width of the rectangle.
     */
    public int width;
    /**
     * The height of the rectangle.
     */
    public int height;

    /**
     * Create a new Rectangle element.
     * <pre>{@code
     * // Create a new Rectangle centered at (100, 100) with a width of 20 and a height of 30
     * Rectangle rectangle = new Rectangle(100, 100, 20, 30);
     * }</pre>
     *
     * @param centerX The X-position of the rectangle
     * @param centerY The Y-position of the rectangle
     * @param width   The width of the rectangle
     * @param height  The height of the rectangle
     */
    public Rectangle(int centerX, int centerY, int width, int height) {
        super(centerX, centerY, Color.BLACK);
        this.width = width;
        this.height = height;
    }


    /**
     * Create a new Rectangle element.
     * <pre>{@code
     * // Create a new red Rectangle centered at (100, 100) with a width of 20 and a height of 30
     * Rectangle rectangle = new Rectangle(100, 100, 20, 30, new Color(255, 0, 0));
     * }</pre>
     *
     * @param centerX The X-position of the rectangle
     * @param centerY The Y-position of the rectangle
     * @param width   The width of the rectangle
     * @param height  The height of the rectangle
     * @param color   The color of the rectangle
     */
    public Rectangle(int centerX, int centerY, int width, int height, Color color) {
        super(centerX, centerY, color);
        this.width = width;
        this.height = height;
    }

    @Override
    protected void drawOutline(Graphics2D gc) {
        gc.drawRect(x - width / 2, y - height / 2, width, height);
    }

    @Override
    protected void drawFilled(Graphics2D gc) {
        gc.fillRect(x - width / 2, y - height / 2, width, height);
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