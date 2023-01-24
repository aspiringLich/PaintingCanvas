package paintingcanvas.painter.drawable;

import java.awt.*;

/**
 * A Triangle element.
 * <pre>{@code
 * // Create a new Triangle at (100, 100) that is 20px wide and 30px tall
 * Triangle triangle = new Triangle(100, 100, 20, 30);
 * }</pre>
 */
public class Triangle extends Drawable<Triangle> {
    int width;
    int height;
    
    /**
     * Create a new Triangle element.
     * <pre>{@code
     * // Create a new Triangle at (100, 100) that is 20px wide and 30px tall
     * Triangle triangle = new Triangle(100, 100, 20, 30);
     * }</pre>
     *
     * @param x The X-position of the triangle
     * @param y The Y-position of the triangle
     * @param w The width of the triangle
     * @param h The height of the triangle
     */
    public Triangle(int x, int y, int w, int h) {
        super(x, y, Color.BLACK);
        this.width = w;
        this.height = h;
    }
    
    @Override
    public void draw(Graphics2D gc) {
        java.awt.Polygon poly = new java.awt.Polygon(
                new int[]{x, x + width, x + width / 2},
                new int[]{y + height, y + height, y},
                3
        );
        gc.setColor(this.color);
        if (filled) gc.drawPolygon(poly);
        else gc.fillPolygon(poly);
    }
    
    @Override
    public Point center(Graphics g) {
        return new Point(x + width / 2, y + height / 2);
    }
    
    @Override
    protected Triangle getThis() {
        return this;
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
}