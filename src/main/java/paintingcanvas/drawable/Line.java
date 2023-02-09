package paintingcanvas.drawable;

import java.awt.*;

/**
 * A line: with a startpoint and an endpoint
 */
public class Line extends Drawable<Line> {
    /**
     * The offset of the endpoint from the startpoint (x, y)
     */
    public Point endOffset;
    
    /**
     * DO NOT USE, Overridden
     * @param color     the color of the outline
     * @param thickness the thickness of the outline
     * @return {@code this}
     */
    @Override
    public Line setOutline(int thickness, Color color) {
        throw new RuntimeException("setOutline is useless on Line, please use .setColor() and .setThickness instead");
    }
    
    /**
     * DO NOT USE, Overridden
     * @param thickness the thickness of the outline
     * @return {@code this}
     */
    @Override
    public Line setOutline(int thickness) {
        throw new RuntimeException("setOutline is useless on Line, please use .setColor() and .setThickness instead");
    }
    
    /**
     * DO NOT USE, Overridden
     * @param filled The value to set {@code this.filled} to
     * @return {@code this}
     */
    @Override
    public Line setFilled(boolean filled) {
        throw new RuntimeException("setFilled is useless on Line as it does not have anything to fill");
    }
    
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
        this.endOffset = new Point(x2 - x1, y2 - y1);
        this.outlineStroke = new BasicStroke(5);
    }

    /**
     * Create a new Line element.
     * <pre>{@code
     * // Create a new Line from (100, 100) to (200, 200)
     * Line line = new Line(100, 100, 200, 200, new Color(255, 0, 0));
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
        this.endOffset = new Point(x2 - x1, y2 - y1);
        this.outlineStroke = new BasicStroke(5);
    }

    /**
     * Set the stroke of the line
     *
     * @param stroke a {@code Stroke} object to define this line's stroke
     * @return The original object to allow method chaining
     */
    @SuppressWarnings("unused")
    public Line setStroke(Stroke stroke) {
        this.outlineStroke = stroke;
        return this;
    }

    /**
     * Set the thickness of the line
     *
     * @param thickness The thickness of the line in pixels
     * @return The original object to allow method chaining
     */
    public Line setThickness(int thickness) {
        this.outlineStroke = new BasicStroke(thickness);
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
     * @param x The new X-position of the startpoint
     * @param y The new Y-position of the startpoint
     * @return The original object to allow method chaining
     */
    public Line setStartpoint(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    /**
     * Set the endpoint of the line
     *
     * @param x The new X-position of the endpoint
     * @param y The new Y-position of the endpoint
     * @return The original object to allow method chaining
     */
    public Line setEndpoint(int x, int y) {
        this.endOffset = new Point(this.x - x, this.y - y);
        return this;
    }

    @Override
    protected void drawFilled(Graphics2D gc) {
        gc.setColor(color);
        gc.drawLine(this.x , this.y, this.x + endOffset.x, this.y + endOffset.y);
    }

    @Override
    protected void drawOutline(Graphics2D gc) {
        gc.setColor(color);
        gc.drawLine(this.x , this.y, this.x + endOffset.x, this.y + endOffset.y);
    }

    @Override
    public Point center(Graphics g) {
        return new Point(this.x + endOffset.x / 2, this.y + endOffset.y / 2);
    }

    @Override
    protected Line getThis() {
        return this;
    }
}
