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

    public abstract int centerX(Graphics g);

    public abstract int centerY(Graphics g);


    public void render(Graphics g) {
        if (!this.visible) return;

        var gc = (Graphics2D) g;
        var transform = gc.getTransform();
        transform.setToRotation(this.rotation, centerX(g), centerY(g));
        gc.setTransform(transform);
        this.draw(g);
    }
}
