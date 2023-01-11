package painter.drawable;

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
     * Get the X-coordinate of the object's center-point
     * @param g unecessary? Maybe?
     * @return the x-coordinate of the object's center-point
     */
    public abstract int centerX(Graphics g);

    /**
     * Get the Y-coordinate of the object's center-point
     * @param g unecessary? Maybe?
     * @return the y-coordinate of the object's center-point
     */
    public abstract int centerY(Graphics g);

    /**
     * Actually render the object itself
     *
     * This calls the draw method, but does some extra steps beforehand to lay it out correctly
     *
     * TODO: Might be unecessary but possibly recalculate this whenever the position is modified but save it somewhere
     * to avoid recalculating this every frame.
     * @param g The graphics context to draw the object with
     */
    public void render(Graphics g) {
        if (!this.visible) return;

        var gc = (Graphics2D) g;
        var transform = gc.getTransform();
        transform.setToRotation(this.rotation, centerX(g), centerY(g));
        gc.setTransform(transform);
        this.draw(g);
    }
}
