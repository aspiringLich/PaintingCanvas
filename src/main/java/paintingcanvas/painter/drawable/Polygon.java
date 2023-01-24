package paintingcanvas.painter.drawable;

import java.awt.*;

/**
 * A Polygon Element.
 * <pre>{@code
 * Polygon polygon = new Polygon(100, 100,
 *     new int[] {0, 25, 50},
 *     new int[] {50, 25, 0},
 * );
 * }</pre>
 */
public class Polygon extends Drawable<Polygon> {
    public java.awt.Polygon polygon;
    
    /**
     * Create a new empty polygon, meant to be used in conjunction with <code>.point(x, y)</code>
     *
     * <pre>{@code
     * Polygon p = new Polygon(100, 100)
     *                     .point(0, 50)
     *                     .point(25, 25)
     *                     .point(50, 0)
     * }</pre>
     *
     * @param x The X-position of the polygon
     * @param y The Y-position of the polygon
     * @see #point(int, int)
     */
    public Polygon(int x, int y) {
        super(x, y, Color.BLACK);
        this.polygon = new java.awt.Polygon();
    }
    
    /**
     * Create a new Polygon element from a {@link java.awt.Polygon}.
     * <pre>{@code
     * Polygon polygon = new Polygon(100, 100, new java.awt.Polygon(
     *     new int[] {0, 25, 50},
     *     new int[] {50, 25, 0},
     *     3
     * ));
     * }</pre>
     *
     * @param x       The X-position of the polygon
     * @param y       The Y-position of the polygon
     * @param polygon The {@link java.awt.Polygon} element
     */
    public Polygon(int x, int y, java.awt.Polygon polygon) {
        super(x, y, Color.BLACK);
        this.polygon = polygon;
    }
    
    /**
     * Create a new Polygon element from a list of x-points and y-points.
     * <pre>{@code
     * Polygon polygon = new Polygon(100, 100,
     *     new int[] {0, 25, 50},
     *     new int[] {50, 25, 0},
     * );
     * }</pre>
     *
     * @param x       The X-position of the polygon
     * @param y       The Y-position of the polygon
     * @param xPoints List of X-points
     * @param yPoints List of Y-points
     */
    public Polygon(int x, int y, int[] xPoints, int[] yPoints) {
        super(x, y, Color.BLACK);
        this.polygon = new java.awt.Polygon(xPoints, yPoints, xPoints.length);
    }
    
    @Override
    public void draw(Graphics2D gc) {
        gc.setColor(color);
        polygon.translate(x, y);
        if (this.filled) gc.fillPolygon(polygon);
        else gc.drawPolygon(polygon);
        polygon.translate(-x, -y);
    }
    
    
    @Override
    public Point center(Graphics g) {
        return new Point(x + polygon.getBounds().width / 2, y + polygon.getBounds().height / 2);
    }
    
    /**
     * Adds a point to this polygon
     * @param x the x-position of said point
     * @param y the y-position of said point
     */
    public void point(int x, int y) {
        polygon.addPoint(x, y);
    }
    
    @Override
    public Polygon getThis() {
        return this;
    }
}