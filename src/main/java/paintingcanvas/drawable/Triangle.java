package paintingcanvas.drawable;

import paintingcanvas.misc.Hue;

import java.awt.*;

/**
 * A Triangle element.
 * <pre>{@code
 * // Create a new Triangle at (100, 100) that is 20px wide and 30px tall
 * Triangle triangle = new Triangle(100, 100, 20, 30);
 * }</pre>
 */
public class Triangle extends Drawable<Triangle> {
    /**
     * The width of the triangle.
     */
    public int width;
    /**
     * The height of the triangle.
     */
    public int height;

    private Polygon poly;

    /**
     * Create a new Triangle element.
     * <pre>{@code
     * // Create a new Triangle centered at (100, 100) that is 20px wide and 30px tall
     * Triangle triangle = new Triangle(100, 100, 20, 30);
     * }</pre>
     *
     * @param centerX The X-position of the triangle
     * @param centerY The Y-position of the triangle
     * @param width   The width of the triangle
     * @param height  The height of the triangle
     */
    public Triangle(int centerX, int centerY, int width, int height) {
        super(centerX, centerY, Color.BLACK);
        this.width = width;
        this.height = height;
    }

    /**
     * Create a new Triangle element.
     * <pre>{@code
     * // Create a new Triangle centered at (100, 100) that is 20px wide and 30px tall
     * Triangle triangle = new Triangle(100, 100, 20, 30, new Color(255, 0, 0));
     * }</pre>
     *
     * @param centerX The X-position of the triangle
     * @param centerY The Y-position of the triangle
     * @param width   The width of the triangle
     * @param height  The height of the triangle
     * @param color   The color of the triangle
     */
    public Triangle(int centerX, int centerY, int width, int height, Color color) {
        super(centerX, centerY, color);
        this.width = width;
        this.height = height;
    }

    /**
     * Create a new Triangle element with a certain color by name
     * (see {@link Hue} for list of all valid names)
     * <pre>{@code
     * // Create a new Triangle centered at (100, 100) that is 20px wide and 30px tall
     * Triangle triangle = new Triangle(100, 100, 20, 30, new Color(255, 0, 0));
     * }</pre>
     *
     * @param centerX The X-position of the triangle
     * @param centerY The Y-position of the triangle
     * @param width   The width of the triangle
     * @param height  The height of the triangle
     * @param color   The name of the color of the triangle
     */
    public Triangle(int centerX, int centerY, int width, int height, String color) {
        this(centerX, centerY, width, height, Hue.getColor(color));
    }

    private java.awt.Polygon getPolygon() {
        return new java.awt.Polygon(
                new int[]{x - width / 2, x + width / 2, x},
                new int[]{y + height / 2, y + height / 2, y - height / 2},
                3
        );
    }

    /**
     * Gets the width of the triangle.
     *
     * @return The width of the triangle in pixels
     * @see #setWidth(int)
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Sets the width of the triangle.
     *
     * @param w The new width of the triangle in pixels
     * @return The original object to allow method chaining
     * @see #getWidth()
     */
    public Triangle setWidth(int w) {
        this.width = w;
        return this;
    }

    /**
     * Gets the height of the triangle.
     *
     * @return The height of the triangle in pixels
     * @see #setHeight(int)
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Sets the height of the triangle.
     *
     * @param h The new height of the triangle in pixels
     * @return The original object to allow method chaining
     * @see #getHeight()
     */
    public Triangle setHeight(int h) {
        this.height = h;
        return this;
    }

    @Override
    protected void drawFilled(Graphics2D gc) {
        gc.fillPolygon(this.getPolygon());
    }

    @Override
    protected void drawOutline(Graphics2D gc) {
        gc.drawPolygon(this.getPolygon());
    }

    @Override
    protected Triangle getThis() {
        return this;
    }
}