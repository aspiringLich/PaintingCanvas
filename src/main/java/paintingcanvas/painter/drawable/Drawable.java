package paintingcanvas.painter.drawable;

import java.awt.*;

/**
 * An interface to connect to any objects that can be considered "painter.drawable.Drawable".
 * Provides various common methods to draw objects to a screen
 */
public abstract class Drawable {
    /**
     * Rotation of the object in radians (imagine using degrees)
     */
    public double rotation;
    public boolean visible = true;
    public boolean filled = true;
    public Color color;
    public int x;
    public int y;

    Drawable(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    /**
     * Draw the actual object onto the screen
     *
     * @param g The graphics
     */
    public abstract void draw(Graphics g);

    /**
     * Get the Y-coordinate of the object's center-point
     *
     * @param g Graphics context
     * @return The object's center-point
     */
    public abstract Point center(Graphics g);

    /**
     * Actually render the object itself
     * <p>
     * This calls the draw method, but does some extra steps beforehand to lay it out correctly
     * <p>
     *
     * @param g The graphics context to draw the object with
     */
    public void render(Graphics g) {
        if (!this.visible) return;

        var gc = (Graphics2D) g;
        // TODO: Might be unnecessary but possibly recalculate this whenever the position is modified but save it somewhere to avoid recalculating this every frame.
        var transform = gc.getTransform();
        var center = this.center(g);

        transform.setToRotation(this.rotation, center.x, center.y);
        transform.scale(1.0, -1.0);
        gc.setTransform(transform);

        var bounds = gc.getClipBounds();
        gc.translate(bounds.getWidth() / 2, -bounds.getHeight() / 2);

        this.draw(gc);
    }
}
