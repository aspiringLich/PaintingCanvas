package paintingcanvas.drawable;

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
     * // Create a new Circle centered at (100, 100) with a radius of 20.
     * Circle circle = new Circle(100, 100, 20);
     * }</pre>
     *
     * @param centerX The X-position of the circle
     * @param centerY The Y-position of the circle
     * @param radius  The radius of the circle
     */
    public Circle(int centerX, int centerY, int radius) {
        super(centerX, centerY, Color.BLACK);
        this.radius = radius;
    }

    /**
     * Create a new Circle element.
     * <pre>{@code
     * // Create a new Circle centered at (100, 100) with a radius of 20.
     * Circle circle = new Circle(100, 100, 20, new Color(255, 0, 0));
     * }</pre>
     *
     * @param centerX The X-position of the circle
     * @param centerY The Y-position of the circle
     * @param radius  The radius of the circle
     */
    public Circle(int centerX, int centerY, int radius, Color color) {
        super(centerX, centerY, color);
        this.radius = radius;
    }

    @Override
    public void draw(Graphics2D gc) {
        gc.setColor(color);
        if (this.filled) gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);
        else gc.drawOval(x - radius, y - radius, radius, radius);
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