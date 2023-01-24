package paintingcanvas.painter.drawable;

import java.awt.*;

/**
 * A Circle element.
 * <pre>{@code
 * // Create a new Circle at (100, 100) with a radius of 20.
 * Circle circle = new Circle(100, 100, 20);
 * }</pre>
 */
public class Circle extends Drawable<Circle> {
    int radius;

    /**
     * Create a new Circle element.
     * <pre>{@code
     * // Create a new Circle at (100, 100) with a radius of 20.
     * Circle circle = new Circle(100, 100, 20);
     * }</pre>
     *
     * @param x The X-position of the circle
     * @param y The Y-position of the circle
     * @param r The radius of the circle
     */
    public Circle(int x, int y, int r) {
        super(x, y, Color.BLACK);
        radius = r;
    }
    
    @Override
    public void draw(Graphics2D gc) {
        gc.setColor(color);
        if (this.filled) gc.fillOval(x, y, radius, radius);
        else gc.drawOval(x, y, radius, radius);
    }
    
    @Override
    public Point center(Graphics g) {
        return new Point(x, y);
    }

    @Override
    protected Circle getThis() {
        return this;
    }

    /**
     * Gets the radius of the circle.
     *
     * @return The radius of the circle in pixels
     * @see #setRadius(int)
     */
    public int getRadius() {
        return this.radius;
    }

    /**
     * Sets the radius of the circle.
     *
     * @param r The new radius of the circle in pixels
     * @return The original object to allow method chaining
     * @see #getRadius()
     */
    public Circle setRadius(int r) {
        this.radius = r;
        return this;
    }
}