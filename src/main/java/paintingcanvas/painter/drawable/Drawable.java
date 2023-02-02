package paintingcanvas.painter.drawable;

import paintingcanvas.painter.App;
import paintingcanvas.painter.animation.ColorAnimation;
import paintingcanvas.painter.animation.MovementAnimation;
import paintingcanvas.painter.animation.RotationAnimation;

import java.awt.*;

/**
 * An interface to connect to any objects that can be considered "painter.drawable.Drawable".
 * Provides various common methods to draw objects to a screen
 */
public abstract class Drawable<T extends Drawable<T>> {
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
        App.painter.canvas.elements.add(this);
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
//        transform.scale(1.0, -1.0);
//        gc.setTransform(transform);
//
//        var bounds = gc.getClipBounds();
//        gc.translate(0.0, -bounds.getHeight());

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
     * @see #rotateTo(double)
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
    public T rotateTo(double rotation) {
        this.rotation = rotation;
        return getThis();
    }

    /**
     * Create an {@link App.AnimationBuilder}.
     * Used to animate different properties of an element (position, rotation, color).
     * <pre>{@code
     * Circle o = new Circle(100, 100, 20);
     *
     * // Animate Circle o to move to 200, 200 for 3 seconds
     * o.animate().add(moveTo(200, 200), 3);
     * }</pre>
     *
     * @return {@link App.AnimationBuilder}
     */
    public App.AnimationBuilder animate() {
        return new App.AnimationBuilder(this);
    }

    /**
     * This method moves {@code this} to the specified {@code x} and {@code y} over {@code duration} seconds
     *
     * <pre>{@code
     * Circle c = new Circle(200, 200, 50);
     * // the circle will move to (100, 100), and then to (200, 200)
     * c.moveTo(100, 100, 3).moveTo(200, 200, 3);
     * }</pre>
     *
     * @param x        the x-position to move to
     * @param y        the y-position to move to
     * @param duration the number of seconds it lasts
     * @return an {@code AnimationBuilder}
     * @see #animate()
     */
    public App.AnimationBuilder moveTo(int x, int y, float duration) {
        return this.animate().add(new MovementAnimation(0, 0, new Point(x, y), this), duration);
    }

    /**
     * This method changes the color of {@code this} to the specified {@code color} over {@code duration} seconds.
     * See <a href="https://en.wikipedia.org/wiki/RGB_color_model">Wikipedia</a> for how this works.
     *
     * <pre>{@code
     * Circle c = new Circle(200, 200, 50);
     * // the circle will turn red, and then blue
     * c.colorTo(255, 0, 0, 3).colorTo(0, 0, 255, 3);
     * }</pre>
     *
     * @param r        red (0-255)
     * @param g        green (0-255)
     * @param b        blue (0-255)
     * @param duration the number of seconds it lasts
     * @return an {@code AnimationBuilder}
     * @see #animate()
     */
    public App.AnimationBuilder colorTo(byte r, byte g, byte b, float duration) {
        return this.animate().add(new ColorAnimation(0, new Color(r, g, b), 0, this), duration);
    }

    /**
     * This method changes the color of {@code this} to the specified {@code color} over {@code duration} seconds.
     * See <a href="https://en.wikipedia.org/wiki/RGB_color_model#Numeric_representations">Wikipedia</a> for how this works.
     *
     * <pre>{@code
     * Circle c = new Circle(200, 200, 50);
     * // the circle will turn red, and then blue
     * c.colorTo(0xFF0000, 3).colorTo(0x0000FF, 3);
     * }</pre>
     *
     * @param hex      The number describing the RGB color
     * @param duration the number of seconds it lasts
     * @return an {@code AnimationBuilder}
     * @see #animate()
     */
    public App.AnimationBuilder colorTo(int hex, float duration) {
        return this.animate().add(new ColorAnimation(0, new Color(hex), 0, this), duration);
    }

    /**
     * This method changes the color of {@code this} to the specified {@code color} over {@code duration} seconds.
     * See <a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.desktop/java/awt/Color.html">The Oracle docs</a> for the full list of colors,
     * and constructors for this class
     *
     * <pre>{@code
     * Circle c = new Circle(200, 200, 50);
     * // the circle will turn red, and then blue
     * c.colorTo(Color.RED, 3).colorTo(Color.BLUE, 3);
     * }</pre>
     *
     * @param color    The color to fade to
     * @param duration the number of seconds it lasts
     * @return an {@code AnimationBuilder}
     * @see #animate()
     */
    public App.AnimationBuilder colorTo(Color color, float duration) {
        return this.animate().add(new ColorAnimation(0, color, 0, this), duration);
    }

    /**
     * Creates a new rotation animation to <code>angle°</code>.
     * If you supply an angle {@code > 360} it will make more than one full rotation.
     *
     * <pre>{@code
     * Square s = new Square(200, 200, 50);
     * // the square will rotate one turn counter-clockwise, then 2 turns clockwise
     * c.rotateTo(360, 3).colorTo(-360, 3);
     * }</pre>
     *
     * @param angle    The absolute angle to rotate to in degrees.
     * @param duration the number of seconds it lasts
     * @return an {@code AnimationBuilder}
     * @see #animate()
     */
    public App.AnimationBuilder rotateTo(int angle, float duration) {
        return this.animate().add(new RotationAnimation(0, 0, Math.toRadians(angle), this), duration);
    }
}
