package paintingcanvas.drawable;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

/**
 * An SVG-like path used to draw lines and curves.
 *
 * Uses <a href="https://docs.oracle.com/javase/8/docs/api/java/awt/geom/Path2D.html">Path2D</a> internally.
 */
public class Path extends Drawable<Path> {
    Path2D path;
    Stroke stroke;

    /**
     * Create a new Path element. The path is initially empty.
     */
    public Path() {
        super(0, 0, Color.BLACK);
        this.path = new Path2D.Double();
        this.stroke = new BasicStroke(1);
        this.filled = false;
    }

    /**
     * Set the stroke of the line
     *
     * @param stroke a {@code Stroke} object to define this line's stroke
     * @return The original object to allow method chaining
     */
    @SuppressWarnings("unused")
    public Path setStroke(Stroke stroke) {
        this.stroke = stroke;
        return this;
    }

    /**
     * Set the thickness of the line
     *
     * @param thickness The thickness of the line in pixels
     * @return The original object to allow method chaining
     */
    public Path setThickness(int thickness) {
        this.stroke = new BasicStroke(thickness);
        return this;
    }

    /**
     * Move the cursor to a new position
     *
     * @param x The X-position of the new cursor position
     * @param y The Y-position of the new cursor position
     * @return The original object to allow method chaining
     */
    public Path cursorTo(int x, int y) {
        path.moveTo(x, y);
        return this;
    }

    /**
     * Make a line from the current cursor position to a new position
     *
     * @param x The X-position of the line endpoint
     * @param y The Y-position of the line endpoint
     * @return The original object to allow method chaining
     */
    public Path lineTo(int x, int y) {
        path.lineTo(x, y);
        return this;
    }

    /**
     * Make a quadratic curve from the current cursor position to a new position. See
     * <a href="https://en.wikipedia.org/wiki/B%C3%A9zier_curve#Quadratic_B%C3%A9zier_curves">Wikipedia: Bézier curve</a> for more information
     *
     * @param x1 The X-position of the control point
     * @param y1 The Y-position of the control point
     * @param x2 The X-position of the curve endpoint
     * @param y2 The Y-position of the curve endpoint
     * @return The original object to allow method chaining
     */
    @SuppressWarnings("unused")
    public Path quadTo(int x1, int y1, int x2, int y2) {
        path.quadTo(x1, y1, x2, y2);
        return this;
    }

    /**
     * Make a cubic curve from the current cursor position to a new position. See
     * <a href="https://en.wikipedia.org/wiki/B%C3%A9zier_curve#Cubic_B%C3%A9zier_curves">Wikipedia: Bézier curve</a> for more information
     *
     * @param x1 The X-position of the first control point
     * @param y1 The Y-position of the first control point
     * @param x2 The X-position of the second control point
     * @param y2 The Y-position of the second control point
     * @param x3 The X-position of the curve endpoint
     * @param y3 The Y-position of the curve endpoint
     * @return The original object to allow method chaining
     */
    @SuppressWarnings("unused")
    public Path curveTo(int x1, int y1, int x2, int y2, int x3, int y3) {
        path.curveTo(x1, y1, x2, y2, x3, y3);
        return this;
    }

    @Override
    public void draw(Graphics2D gc) {
        gc.setColor(color);
        gc.setStroke(stroke);
        Path2D copy = (Path2D)path.clone();
        copy.transform(AffineTransform.getTranslateInstance(x, y));

        if (this.filled) gc.fill(copy);
        else {
            gc.setStroke(stroke);
            gc.draw(copy);
        }
    }

    @Override
    public Point center(Graphics g) {
        var bounds = path.getBounds();
        return new Point(x + (int)bounds.getCenterX(), y + (int)bounds.getCenterY());
    }

    @Override
    protected Path getThis() {
        return this;
    }
}