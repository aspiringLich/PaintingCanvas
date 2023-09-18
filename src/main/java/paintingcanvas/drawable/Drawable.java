package paintingcanvas.drawable;

import paintingcanvas.animation.Animatable;
import paintingcanvas.animation.AnimationBuilder;
import paintingcanvas.canvas.Canvas;
import paintingcanvas.misc.ElementContainer;
import paintingcanvas.misc.Hue;
import paintingcanvas.misc.Misc;

import java.awt.*;

/**
 * An interface to connect to any objects that can be considered "painter.drawable.Drawable".
 * Provides various common methods to draw objects to a screen
 */
@SuppressWarnings("unused")
public abstract class Drawable<T extends Drawable<T>> implements Animatable {
    /**
     * The layer of the object, higher layers are rendered on top of lower layers.
     * By default, all objects are on layer 0.
     */
    int layer = 0;
    /**
     * Rotation of the object in radians (imagine using degrees)
     */
    double rotation;
    /**
     * Whether the object is visible or not
     */
    boolean visible = true;
    /**
     * Whether the object is filled or not
     */
    boolean filled = true;
    /**
     * the color of the object
     */
    Color color;
    /**
     * the X-position of the object
     */
    int x;
    /**
     * the Y-position of the object
     */
    int y;
    /**
     * the color of the outline
     */
    Color outlineColor = Color.BLACK;
    /**
     * the stroke of the outline
     */
    Stroke outlineStroke;

    Drawable(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;

        var canvas = Canvas.getGlobalInstance();
        if (canvas.options.isAutoAdd())
            canvas.elements.add(this);
    }

    /**
     * Sets the parameters for the outline of the shape
     *
     * <pre>{@code
     * // the circle is red outlined by a black outline
     * Circle c = new Circle(100, 100, 50);
     * c.setOutline(5);
     * c.setColor(Color.RED);
     *
     * // now just the outline is rendered
     * c.setFilled(false);
     * }</pre>
     *
     * @param color     the color of the outline
     * @param thickness the thickness of the outline
     * @return the original object to allow method chaining
     */
    public T setOutline(int thickness, Color color) {
        ElementContainer.atomic(() -> {
            this.outlineColor = color;
            this.outlineStroke = new BasicStroke(thickness);
        });
        return this.getThis();
    }

    /**
     * Sets the outline color of the shape
     *
     * <pre>{@code
     * // the circle is red outlined by a black outline
     * Circle c = new Circle(100, 100, 50);
     * c.setOutline(5);
     * c.setColor(Color.RED);
     *
     * // now the outline is blue
     * c.setOutline(Color.BLUE);
     * }</pre>
     *
     * @param color the color of the outline
     * @return the original object to allow method chaining
     */
    public T setOutline(Color color) {
        ElementContainer.atomic(() -> {
            this.outlineColor = color;
        });
        return this.getThis();
    }

    /**
     * Sets the parameters for the outline of the shape, with the color defaulting to black
     *
     * <pre>{@code
     * // the circle is red outlined by a green outline
     * Circle c = new Circle(100, 100, 50);
     * c.setOutline(5, Color.GREEN);
     * c.setColor(Color.RED);
     *
     * // now just the outline is rendered
     * c.setFilled(false);
     * }</pre>
     *
     * @param thickness the thickness of the outline
     * @return the original object to allow method chaining
     */
    public T setOutline(int thickness) {
        return this.setOutline(thickness, outlineColor);
    }

    /**
     * Removes the outline from the shape
     *
     * @return the original object to allow method chaining
     */
    public T removeOutline() {
        this.outlineStroke = null;
        return this.getThis();
    }

    /**
     * Get the object's centerpoint
     *
     * <pre>{@code
     * import java.awt.Point;
     *
     * Point p = drawable.getStartpoint();
     * int x = p.x;
     * int y = p.y;
     * }</pre>
     *
     * @param g Graphics context
     * @return the object's center-point
     */
    public Point center(Graphics g) {
        return new Point(this.x, this.y);
    }

    /**
     * Actually render the object itself
     * <p>
     * This calls the draw methods, but does some extra steps beforehand to lay it out correctly
     * <p>
     *
     * @param g the graphics context to draw the object with
     */
    public void render(Graphics g) {
        if (!this.visible) return;

        var gc = (Graphics2D) g;
        var save = gc.getTransform();
        var transform = gc.getTransform();

        var center = this.center(g);
        transform.rotate(this.rotation, center.x, center.y);
        gc.setTransform(transform);

        // if filled, draw filled then outline
        // if not filled just draw the outline
        if (this.filled) {
            gc.setColor(this.color);
            this.drawFilled(gc);
        }
        this.renderOutline(gc);

        gc.setTransform(save);
    }

    private void renderOutline(Graphics2D gc) {
        if (this.outlineStroke != null) {
            gc.setColor(this.outlineColor);
            gc.setStroke(this.outlineStroke);
            this.drawOutline(gc);
        }
    }

    /**
     * the color is set to `color`
     *
     * @param gc the graphics context to draw the object with
     */
    protected abstract void drawFilled(Graphics2D gc);

    /**
     * the color is set to `outlineColor`, the stroke is set to `outlineStroke`
     *
     * @param gc the graphics context to draw the object with
     */
    protected abstract void drawOutline(Graphics2D gc);

    protected abstract T getThis();

    /**
     * Hide the Object.
     * <pre>{@code
     * Circle o = new Circle(100, 100, 20);
     * o.hide();
     * }</pre>
     *
     * @return the original object to allow method chaining
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
     * @return the original object to allow method chaining
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
     * @return the original object to allow method chaining
     * @see #getX()
     * @see #setY(int)
     */
    public T setX(int x) {
        ElementContainer.atomic(() -> this.x = x);
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
     * @return the original object to allow method chaining
     * @see #setX(int)
     * @see #getY()
     */
    public T setY(int y) {
        ElementContainer.atomic(() -> this.y = y);
        return getThis();
    }

    /**
     * Get the position of the element
     *
     * @return the position of the element as a {@link Point}
     * @see #setPos(int, int)
     */
    public Point getPos() {
        return new Point(x, y);
    }

    /**
     * Set the position of the element.
     *
     * @param x the new absolute X-position of the element
     * @param y the new absolute Y-position of the element
     * @return the original object to allow method chaining
     * @see #getPos()
     * @see #setX(int)
     * @see #setY(int)
     */
    public T setPos(int x, int y) {
        ElementContainer.atomic(() -> {
            this.x = x;
            this.y = y;
        });
        return getThis();
    }

    /**
     * Moves this drawable by the specified x and y.
     *
     * <pre>{@code
     * Circle c = new Circle(100, 100, 20);
     * // moves this circle 10 pixels to the right and 10 pixels down
     * c.move(10, 10);
     * // moves this circle 10 pixels to the left and 10 pixels up
     * c.move(-10, -10);
     * }</pre>
     *
     * @param x the x to move by
     * @param y the y to move by
     * @return the original object to allow method chaining
     * @see #setPos(int, int)
     * @see #moveHorizontal(int)
     * @see #moveVertical(int)
     */
    public T move(int x, int y) {
        ElementContainer.atomic(() -> {
            this.x += x;
            this.y += y;
        });
        return getThis();
    }

    /**
     * Moves this drawable by the specified x and y.
     *
     * <pre>{@code
     * Circle c = new Circle(100, 100, 20);
     * // moves this circle 10 pixels to the right
     * c.moveHorizontal(10);
     * }</pre>
     *
     * @param x the x to move by
     * @return the original object to allow method chaining
     * @see #setPos(int, int)
     * @see #moveVertical(int)
     */
    public T moveHorizontal(int x) {
        ElementContainer.atomic(() -> this.x += x);
        return getThis();
    }

    /**
     * Moves this drawable by the specified x and y.
     *
     * <pre>{@code
     * Circle c = new Circle(100, 100, 20);
     * // moves this circle 10 pixels down
     * c.moveVertical(10);
     * }</pre>
     *
     * @param y the y to move by
     * @return the original object to allow method chaining
     * @see #setPos(int, int)
     * @see #moveHorizontal(int)
     */
    public T moveVertical(int y) {
        ElementContainer.atomic(() -> this.y += y);
        return getThis();
    }

    /**
     * Get the current color of an element as a {@link Color}
     *
     * @return the {@link Color} of the element
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Set the color of {@code this} to the specified {@code color}.
     * See <a href="https://en.wikipedia.org/wiki/RGB_color_model#Numeric_representations">Wikipedia</a> for how this works.
     *
     * <pre>{@code
     * Circle o = new Circle(100, 100, 20);
     * // 0xFF0000 is hex for (255, 0, 0), which is red
     * o.setColor(0xFF0000);
     * }</pre>
     *
     * @param hex the number describing the RGB color
     * @return the original object to allow method chaining
     */
    public T setColor(int hex) {
        return setColor(hex >> 16 & 0xff, hex >> 8 & 0xff, hex & 0xff);
    }

    /**
     * Set the color of the object with a {@link Hue} object.
     * <pre>{@code
     * Circle o = new Circle(100, 100, 20);
     * o.setColor(Hue.GREEN); // Set color to red
     * }</pre>
     *
     * @param hue the hue
     * @return the original object to allow method chaining
     */
    public T setColor(Hue hue) {
        ElementContainer.atomic(() -> this.color = hue.getColor());
        return getThis();
    }

    /**
     * Set the color of the object with a hue name or hex code.
     *
     * @param name the string describing the hue or the hex code
     * @return the original object to allow method chaining
     * @see Misc#stringToColor(String)
     *
     * <pre>{@code
     * Circle o = new Circle(100, 100, 20);
     * o.setColor("red"); // Set color to red
     * // #FF0000 is hex for (255, 0, 0), which is red
     * o.setColor("#FF0000"); // Set color to red, in a different way
     * }</pre>
     */
    public T setColor(String name) {
        return setColor(Misc.stringToColor(name));
    }

    /**
     * Set the color of the object with a {@link Color} object.
     * <pre>{@code
     * Circle o = new Circle(100, 100, 20);
     * o.setColor(Color.RED); // Set color to red
     * }</pre>
     *
     * @param color color.
     * @return the original object to allow method chaining
     */
    public T setColor(Color color) {
        ElementContainer.atomic(() -> this.color = color);
        return getThis();
    }

    /**
     * Set the color of {@code this} to the specified {@code color}.
     * See <a href="https://en.wikipedia.org/wiki/RGB_color_model">Wikipedia</a> for how this works.
     *
     * <pre>{@code
     * Circle o = new Circle(100, 100, 20);
     * o.setColor(255, 0, 0); // Set color to red
     * }</pre>
     *
     * @param r red (0-255)
     * @param g green (0-255)
     * @param b blue (0-255)
     * @return the original object to allow method chaining
     */
    public T setColor(int r, int g, int b) {
        return this.setColor(new Color(r, g, b));
    }

    /**
     * Set the color of {@code this} to the specified {@code color}
     * See <a href="https://en.wikipedia.org/wiki/RGBA_color_model">Wikipedia</a> for how this works.
     *
     * <pre>{@code
     * Circle o = new Circle(100, 100, 20);
     * o.setColor(255, 0, 0); // Set color to red
     * }</pre>
     *
     * @param r red (0-255)
     * @param g green (0-255)
     * @param b blue (0-255)
     * @param a alpha (0-255)
     * @return the original object to allow method chaining
     */
    public T setColor(int r, int g, int b, int a) {
        return this.setColor(new Color(r, g, b, a));
    }

    /**
     * Rotate this element by {@code rotation} degrees.
     * <pre>{@code
     * Circle o = new Circle(100, 100, 20);
     *
     * // Calling rotate(90) twice makes the object rotate 180°
     * o.rotate(90);
     * o.rotate(90);
     * }</pre>
     *
     * @param rotation Change in rotation. (Degrees)
     * @return the original object to allow method chaining
     * @see #setRotation(double)
     */
    public T rotate(double rotation) {
        ElementContainer.atomic(() -> this.rotation += Math.toRadians(rotation));
        return getThis();
    }

    /**
     * Get an elements rotation
     * <pre>{@code
     * Circle o = new Circle(100, 100, 20);
     * o.setRotation(90); // Sets the elements rotation to 90°
     * let i = o.getRotation(); // Gets the rotation
     * assert i == 90;
     * }</pre>
     *
     * @return the rotation of the object
     * @see #rotate(double)
     */
    public double getRotation() {
        return this.rotation;
    }

    /**
     * Set an elements rotation to {@code rotation} degrees.
     * <pre>{@code
     * Circle o = new Circle(100, 100, 20);
     * o.setRotation(90); // Sets the elements rotation to 90°
     * }</pre>
     *
     * @param rotation Absolute rotation. (Degrees)
     * @return the original object to allow method chaining
     * @see #rotate(double)
     */
    public T setRotation(double rotation) {
        ElementContainer.atomic(() -> this.rotation = rotation);
        return getThis();
    }

    /**
     * Set whether this object is filled or not
     *
     * <pre>{@code
     * // draw just an outline:
     * Circle c = new Circle(100, 100, 50, Color.RED);
     * c.setOutline(5);
     * c.setFilled(false);
     * }</pre>
     *
     * @param filled the value to set {@code this.filled} to
     * @return the original object to allow method chaining
     * @see #setOutline(int)
     */
    public T setFilled(boolean filled) {
        ElementContainer.atomic(() -> this.filled = filled);
        return getThis();
    }

    /**
     * Gets the current layer of the object.
     * By default, all objects are on layer 0.
     *
     * @return the original object to allow method chaining
     * @see #setLayer(int)
     * @see #bringToFront()
     * @see #sendToBack()
     */
    public int getLayer() {
        return this.layer;
    }

    /**
     * Puts the object on a specific layer.
     *
     * @param layer the layer to set the object to
     * @return the original object to allow method chaining
     * @see #getLayer()
     * @see #bringToFront()
     * @see #sendToBack()
     */
    public T setLayer(int layer) {
        ElementContainer.atomic(() -> {
            this.layer = layer;
            Canvas.getGlobalInstance().elements.setDirty();
        });
        return getThis();
    }

    /**
     * Brings the object in front of all other objects.
     *
     * @return the original object to allow method chaining
     * @see #getLayer()
     * @see #setLayer(int)
     * @see #sendToBack()
     */
    public T bringToFront() {
        return this.setLayer(Canvas.getGlobalInstance().elements.getMaxLayer() + 1);
    }

    /**
     * Puts the object behind all other objects.
     *
     * @return the original object to allow method chaining
     * @see #getLayer()
     * @see #setLayer(int)
     * @see #bringToFront()
     */
    public T sendToBack() {
        return this.setLayer(Canvas.getGlobalInstance().elements.getMinLayer() - 1);
    }

    /**
     * Erase this object from the canvas. This object will be gone and cannot* be added back!
     * <p>
     * <sub>* It can be added back just don't tell anyone shhhh</sub>
     * </p>
     */
    public void erase() {
        Canvas.getGlobalInstance().elements.remove(this);
    }

    /**
     * Gets the outline color
     *
     * @return the outline color
     */
    public Color getOutlineColor() {
        return this.outlineColor;
    }

    /**
     * Gets the outline stroke
     *
     * @return the outline stroke
     */
    public Stroke getOutlineStroke() {
        return this.outlineStroke;
    }

    /**
     * Start animating this object.
     *
     * @return An {@link AnimationBuilder} to start animating this object.
     */
    public AnimationBuilder animate() {
        return new AnimationBuilder(this);
    }

    @Override
    public Drawable<?> drawable() {
        return this;
    }
}
