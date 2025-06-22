package paintingcanvas.drawable;

import paintingcanvas.misc.Misc;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * A Triangle element.
 * <pre>{@code
 * // Create a new Triangle at (100, 100) that is 20px wide and 30px tall
 * Triangle triangle = new Triangle(100, 100, 20, 30);
 * }</pre>
 */
public class Triangle extends DrawableBase.InteractableShape<Triangle> {
    int width;
    int height;

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
     * Create a new Triangle element with a hue name or hex code
     *
     * @param centerX The X-position of the triangle
     * @param centerY The Y-position of the triangle
     * @param width   The width of the triangle
     * @param height  The height of the triangle
     * @param color   The name of the color of the triangle
     * @see Misc#stringToColor(String)
     * <pre>{@code
     * // Create a new Triangle centered at (100, 100) that is 20px wide and 30px tall
     * Triangle triangle = new Triangle(100, 100, 20, 30, new Color(255, 0, 0));
     * }</pre>
     */
    public Triangle(int centerX, int centerY, int width, int height, String color) {
        this(centerX, centerY, width, height, Misc.stringToColor(color));
    }

    private java.awt.Polygon getPolygon() {
        return new java.awt.Polygon(
                new int[]{-width / 2, width / 2, 0},
                new int[]{height / 2, height / 2, -height / 2},
                3
        );
    }

    @Override
    void postTransform(AffineTransform transform) {
        transform.translate(-width * anchor.x, -height * anchor.y);
    }

    @Override
    boolean intersectsInDrawSpace(Point pos) {
        var y = ((double)pos.y) / height;
        var x = ((double)pos.x) / width;
        return (Math.abs(2.0 * x) - 0.5) <= y && y <= 0.5;
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
    protected void drawFill(Graphics2D g) {
        g.fillPolygon(this.getPolygon());
    }

    @Override
    protected void drawOutline(Graphics2D g) {
        g.drawPolygon(this.getPolygon());
    }

    @Override
    public Point center(Graphics2D g) {
        return getPos();
    }

    @Override
    public Triangle getThis() {
        return this;
    }
}
