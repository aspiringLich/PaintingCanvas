package painter.drawable;

import java.awt.*;

/**
 * An interface to connect to any objects that can be considered "painter.drawable.Drawable".
 * Provides various common methods to draw objects to a screen
 */
public abstract class Drawable {
    /**
     * Draw the actual object onto the screen
     * @param g The graphics
     */
    public abstract void draw(Graphics g);

    /** The color of the Drawable object */
    public Color color;

    /**
     * Sets the color via red green and blue arguments
     *
     * @param r red (0-255)
     * @param g green (0-255)
     * @param b blue (0-255)
     * @return The original object to allow method chaining
     */
    public Drawable setColor(int r, int g, int b) {
        this.color = new Color(r, g, b);
        return this;
    }

    // the x and y positions of the onject
    public int x;
    public int y;

    /**
     * Get the x-position of this object
     * @return the x-position
     */
    public int getX() {
        return x;
    }

    /**
     * Get the y-position of this object
     * @return the y-position
     */
    public int getY() {
        return y;
    }

    /**
     * Set the x-position of this object
     * @param x the x-position
     * @return The original object to allow method chaining
     */
    public Drawable setX(int x) {
        setPos(x, y);
        return this;
    }

    /**
     * Set the y-position of this object
     * @param y the y-position
     * @return The original object to allow method chaining
     */
    public Drawable setY(int y) {
        setPos(x, y);
        return this;
    }

    int rawX;
    int rawY;

    /**
     * Set the x and y position of this object.
     * @param x the x-position
     * @param y the y-position
     * @return The original object to allow method chaining
     */
    public Drawable setPos(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }
}
