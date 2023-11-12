package paintingcanvas.drawable;

import paintingcanvas.misc.Misc;

import java.awt.*;

/**
 * A line: with a startpoint and an endpoint
 */
public class Line extends DrawableBase<Line> {
    Point endOffset;
    Stroke stroke = new BasicStroke(1);

    /**
     * Create a new Line element.
     * <pre>{@code
     * // Create a new Line from (100, 100) to (200, 200)
     * Line line = new Line(100, 100, 200, 200);
     * }</pre>
     *
     * @param x1 the X-position of the startpoint
     * @param y1 the Y-position of the startpoint
     * @param x2 the X-position of the endpoint
     * @param y2 the Y-position of the endpoint
     */
    public Line(int x1, int y1, int x2, int y2) {
        super(x1, y1, Color.BLACK);
        this.endOffset = new Point(x2 - x1, y2 - y1);
    }

    /**
     * Create a new Line element.
     * <pre>{@code
     * // Create a new Line from (100, 100) to (200, 200)
     * Line line = new Line(100, 100, 200, 200, new Color(255, 0, 0));
     * }</pre>
     *
     * @param x1    the X-position of the startpoint
     * @param y1    the Y-position of the startpoint
     * @param x2    the X-position of the endpoint
     * @param y2    the Y-position of the endpoint
     * @param color the color of the line
     */
    public Line(int x1, int y1, int x2, int y2, Color color) {
        super(x1, y1, color);
        this.endOffset = new Point(x2 - x1, y2 - y1);
    }

    /**
     * Create a new Line element colored a hue name or hex code
     *
     * @param x1    the X-position of the startpoint
     * @param y1    the Y-position of the startpoint
     * @param x2    the X-position of the endpoint
     * @param y2    the Y-position of the endpoint
     * @param color the name of the color (case-insensitive)
     * @see Misc#stringToColor(String)
     * <pre>{@code
     * // Create a new Line from (100, 100) to (200, 200)
     * Line line = new Line(100, 100, 200, 200, "red");
     * }</pre>
     */
    public Line(int x1, int y1, int x2, int y2, String color) {
        this(x1, y1, x2, y2, Misc.stringToColor(color));
    }

    public Line setStroke(int thickness, Color color) {
        this.color = color;
        this.stroke = new BasicStroke(thickness);
        return this;
    }

    /**
     * Set the stroke of the line
     *
     * @param stroke a {@code Stroke} object to define this line's stroke
     * @return the original object to allow method chaining
     */
    public Line setStroke(Stroke stroke) {
        this.stroke = stroke;
        return this;
    }

    /**
     * Set the thickness of the line
     *
     * @param thickness the thickness of the line in pixels
     * @return the original object to allow method chaining
     */
    public Line setThickness(int thickness) {
        this.stroke = new BasicStroke(thickness);
        return this;
    }

    /**
     * Get the endpoint of the line
     *
     * <pre>{@code
     * import java.awt.Point;
     *
     * Point p = line.getEndpoint();
     * int x = p.x;
     * int y = p.y;
     * }</pre>
     *
     * @return a {@link Point} object representing the startpoint of the line
     */
    public Point getEndpoint() {
        return new Point(this.x - endOffset.x, this.y - endOffset.y);
    }

    /**
     * Get the startpoint of the line
     *
     * <pre>{@code
     * import java.awt.Point;
     *
     * Point p = line.getStartpoint();
     * int x = p.x;
     * int y = p.y;
     * }</pre>
     *
     * @return a {@link Point} object representing the startpoint of the line
     */
    public Point getStartpoint() {
        return new Point(this.x, this.y);
    }

    /**
     * Get the startpoint of the line
     *
     * @param x the new X-position of the startpoint
     * @param y the new Y-position of the startpoint
     * @return the original object to allow method chaining
     */
    public Line setStartpoint(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    /**
     * Set the endpoint of the line
     *
     * @param x the new X-position of the endpoint
     * @param y the new Y-position of the endpoint
     * @return the original object to allow method chaining
     */
    public Line setEndpoint(int x, int y) {
        this.endOffset = new Point(this.x - x, this.y - y);
        return this;
    }

    @Override
    void draw(Graphics2D gc) {
        gc.drawLine(0, 0, endOffset.x, endOffset.y);
    }

    @Override
    public Point center(Graphics2D g) {
        return new Point(this.x + endOffset.x / 2, this.y + endOffset.y / 2);
    }

    public Line getThis() {
        return this;
    }
}
