package paintingcanvas.drawable;

import paintingcanvas.App;
import paintingcanvas.animation.Animatable;

import java.awt.*;

/**
 * An interface to connect to any objects that can be considered "painter.drawable.Drawable".
 * Provides various common methods to draw objects to a screen
 */
public abstract class Drawable<T extends Drawable<T>> implements Animatable {
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
        App.canvas.elements.add(this);
    }

    /**
     * Draw the actual object onto the screen
     *
     * @param gc The graphics context
     */
    public abstract void draw(Graphics2D gc);

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
        var transform = gc.getTransform();
        var center = this.center(g);
        transform.setToRotation(this.rotation, center.x, center.y);
        transform.translate(center.x - x, center.y - y);
        gc.setTransform(transform);

        this.draw(gc);
    }

    protected abstract T getThis();

    /**
     * Hide the Object.
     * <pre>{@code
     * Circle o = new Circle(100, 100, 20);
     * o.hide();
     * }</pre>
     *
     * @return The original object to allow method chaining
     * @see #show()
     */
    public T hide() {
        this.visible = false;
        return getThis();
    }

    /**
     * Show the Object
     * <pre>{@code
     * Circle o = new Circle(100, 100, 20);
     * o.show();
     * }</pre>
     *
     * @return The original object to allow method chaining
     * @see #hide()
     */
    public T show() {
        this.visible = true;
        return getThis();
    }

    /**
     * Get the X-position of the element
     *
     * @return the X-position of the element
     * @see #getY()
     * @see #setX(int)
     */
    public int getX() {
        return this.x;
    }

    /**
     * Set the X-position of the object
     *
     * @param x the new X-position of the Object
     * @return The original object to allow method chaining
     * @see #getX()
     * @see #setY(int)
     */
    public T setX(int x) {
        this.x = x;
        return getThis();
    }

    /**
     * Get the Y-position of the element.
     *
     * @return the Y-position of the object
     * @see #getY() ()
     * @see #setX(int)
     */
    public int getY() {
        return this.y;
    }

    /**
     * Set the Y-position of the element
     *
     * @param y the new Y-position of the Object
     * @return The original object to allow method chaining
     * @see #setX(int)
     * @see #getY()
     */
    public T setY(int y) {
        this.y = y;
        return getThis();
    }

    /**
     * Get the position of the element
     *
     * @return The position of the element as a {@link Point}
     * @see #setPos(int, int)
     */
    public Point getPos() {
        return new Point(x, y);
    }

    /**
     * Set the position of the element.
     *
     * @param x The new absolute X-position of the element
     * @param y The new absolute Y-position of the element
     * @return The original object to allow method chaining
     * @see #getPos()
     * @see #setX(int)
     * @see #setY(int)
     */
    public T setPos(int x, int y) {
        this.x = x;
        this.y = y;
        return getThis();
    }

    /**
     * Set the color of the element with <a href="https://en.wikipedia.org/wiki/RGB_color_model">RGB</a>.
     * <pre>{@code
     * Circle o = new Circle(100, 100, 20);
     * o.setColor(255, 0, 0); // Set color to red
     * }</pre>
     *
     * @param r The red component of the color (0-255)
     * @param g The green component of the color (0-255)
     * @param b The blue component of the color (0-255)
     * @return The original object to allow method chaining
     */
    public T setColor(int r, int g, int b) {
        this.color = new Color(r, g, b);
        return getThis();
    }

    /**
     * Get the current color of an element as a {@link Color}
     *
     * @return The {@link Color} of the element
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Set the color of the object with a {@link Color} object.
     * <pre>{@code
     * Circle o = new Circle(100, 100, 20);
     * o.setColor(Color.RED); // Set color to red
     * }</pre>
     *
     * @param color color.
     * @return The original object to allow method chaining
     */
    public T setColor(Color color) {
        this.color = color;
        return getThis();
    }

    /**
     * Set the color of the object with a <a href="https://en.wikipedia.org/wiki/RGB_color_model#Numeric_representations">8-bit RGB hex literal</a>.
     * <pre>{@code
     *     Circle o = new Circle(100, 100, 20);
     * // 0xFF0000 is hex for (255, 0, 0), which is red
     * o.setColor(0xFF0000);
     * }</pre>
     *
     * @param hex The color as a hex literal
     * @return The original object to allow method chaining
     */
    public T setColor(int hex) {
        return setColor(hex >> 16 & 0xff, hex >> 8 & 0xff, hex & 0xff);
    }

    /**
     * Rotate this element by <code>rotation°</code>.
     * <pre>{@code
     * Circle o = new Circle(100, 100, 20);
     *
     * // Calling rotate(90) twice makes the object rotate 180°
     * o.rotate(90);
     * o.rotate(90);
     * }</pre>
     *
     * @param rotation Change in rotation. (Degrees)
     * @return The original object to allow method chaining
     * @see #setRotation(double)
     */
    public T rotate(double rotation) {
        this.rotation += Math.toRadians(rotation);
        return getThis();
    }

    /**
     * Set an elements rotation to <code>rotation°</code>.
     * <pre>{@code
     * Circle o = new Circle(100, 100, 20);
     * o.rotate(90); // Sets the elements rotation to 90°
     * }</pre>
     *
     * @param rotation Absolute rotation. (Degrees)
     * @return The original object to allow method chaining
     * @see #rotate(double)
     */
    public T setRotation(double rotation) {
        this.rotation = rotation;
        return getThis();
    }

    /**
     * Set whether this object is filled or not
     *
     * @param filled The value to set {@code this.filled} to
     * @return The original object to allow method chaining
     */
    public T setFilled(boolean filled) {
        this.filled = filled;
        return getThis();
    }

    public App.AnimationBuilder getAnimationbuilder() {
        return new App.AnimationBuilder(this);
    }

    public Drawable drawable() {
        return this;
    }
}
