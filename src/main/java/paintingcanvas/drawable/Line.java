package paintingcanvas.drawable;

import java.awt.*;

/**
 * A line: with a startpoint and an endpoint
 */
public class Line extends Drawable<Line> {
    Point endOffset;
    Stroke stroke;

    /**
     * Create a new Line element.
     * <pre>{@code
     * // Create a new Line from (100, 100) to (200, 200)
     * Line line = new Line(100, 100, 200, 200);
     * }</pre>
     *
     * @param x1 The X-position of the startpoint
     * @param y1 The Y-position of the startpoint
     * @param x2 The X-position of the endpoint
     * @param y2 The Y-position of the endpoint
     */
    public Line(int x1, int y1, int x2, int y2) {
        super(x1, y1, Color.BLACK);
        this.endOffset = new Point(x1 - x2, y1 - y2);
        this.stroke = new BasicStroke(1);
    }

    /**
     * Create a new Line element.
     * <pre>{@code
     * // Create a new Line from (100, 100) to (200, 200)
     * Line line = new Line(100, 100, 200, 200);
     * }</pre>
     *
     * @param x1    The X-position of the startpoint
     * @param y1    The Y-position of the startpoint
     * @param x2    The X-position of the endpoint
     * @param y2    The Y-position of the endpoint
     * @param color The color of the line
     */
    @SuppressWarnings("unused")
    public Line(int x1, int y1, int x2, int y2, Color color) {
        super(x1, y1, color);
        this.endOffset = new Point(x1 - x2, y1 - y2);
        this.stroke = new BasicStroke(1);
    }

    /**
     * Set the stroke of the line
     *
     * @param stroke a {@code Stroke} object to define this line's stroke
     * @return The original object to allow method chaining
     */
    @SuppressWarnings("unused")
    public Line setStroke(Stroke stroke) {
        this.stroke = stroke;
        return this;
    }

    /**
     * Set the thickness of the line
     *
     * @param thickness The thickness of the line in pixels
     * @return The original object to allow method chaining
     */
    public Line setThickness(int thickness) {
        this.stroke = new BasicStroke(thickness);
        return this;
    }

    /**
     * Get the endpoint of the line
     * @return a {@code Point} object representing the endpoint of the line
     */
    @SuppressWarnings("unused")
    public Point getEndpoint() {
        return new Point(this.x - endOffset.x, this.y - endOffset.y);
    }

    /**
     * Get the startpoint of the line
     * @return a {@code Point} object representing the startpoint of the line
     */
    @SuppressWarnings("unused")
    public Point getStartpoint() {
        return new Point(this.x, this.y);
    }

    @Override
    public void draw(Graphics2D gc) {
        gc.setColor(color);
        gc.setStroke(stroke);
        gc.drawLine(this.x, this.y, this.x - endOffset.x, this.y - endOffset.y);
    }

    @Override
    public Point center(Graphics g) {
        return new Point(this.x - endOffset.x / 2, this.y - endOffset.y / 2);
    }

    @Override
    protected Line getThis() {
        return this;
    }
}
