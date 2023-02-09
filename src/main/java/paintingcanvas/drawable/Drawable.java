package paintingcanvas.drawable;

import paintingcanvas.App;
import paintingcanvas.animation.Animatable;

import java.awt.*;

/**
 * An interface to connect to any objects that can be considered "painter.drawable.Drawable".
 * Provides various common methods to draw objects to a screen
 */
@SuppressWarnings("unused")
public abstract class Drawable<T extends Drawable<T>> implements Animatable {
    /**
     * Rotation of the object in radians (imagine using degrees)
     */
    public double rotation;
    /**
     * Whether the object is visible or not
     */
    public boolean visible = true;
    /**
     * Whether the object is filled or not
     */
    public boolean filled = true;
    /**
     * The color of the object
     */
    public Color color;
    /**
     * The X-position of the object
     */
    public int x;
    /**
     * The Y-position of the object
     */
    public int y;
    /**
     * The color of the outline
     */
    public Color outlineColor = Color.BLACK;
    /**
     * The stroke of the outline
     */
    public Stroke outlineStroke;
    
    Drawable(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
        App.addElement(this);
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
     * @return The original object to allow method chaining
     */
    public T setOutline( int thickness, Color color) {
        this.outlineColor = color;
        this.outlineStroke = new BasicStroke(thickness);
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
     *
     * @param thickness the thickness of the outline
     * @return The original object to allow method chaining
     */
    public T setOutline(int thickness) {
        return this.setOutline(thickness, outlineColor);
    }
    
    /**
     * Removes the outline from the shape
     *
     * @return The original object to allow method chaining
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
        gc.setTransform(transform);
        
        // if filled, draw filled then outline
        // if not filled just draw the outline
        if (this.filled) {
            gc.setColor(this.color);
            this.drawFilled(gc);
        }
        this.renderOutline(gc);
    }
    
    private void renderOutline(Graphics2D gc) {
        if (this.outlineStroke != null) {
            gc.setColor(this.outlineColor);
            gc.setStroke(this.outlineStroke);
            this.drawOutline(gc);
        }
    }
    
    protected abstract void drawFilled(Graphics2D gc);
    
    protected abstract void drawOutline(Graphics2D gc);
    
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
     * @param x The x to move by
     * @param y The y to move by
     * @return The original object to allow method chaining
     * @see #setPos(int, int)
     * @see #moveHorizontal(int)
     * @see #moveVertical(int)
     */
    public T move(int x, int y) {
        this.x += x;
        this.y += y;
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
     * @param x The x to move by
     * @return The original object to allow method chaining
     * @see #setPos(int, int)
     * @see #moveVertical(int)
     */
    public T moveHorizontal(int x) {
        this.x += x;
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
     * @param y The y to move by
     * @return The original object to allow method chaining
     * @see #setPos(int, int)
     * @see #moveHorizontal(int)
     */
    public T moveVertical(int y) {
        this.y += y;
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
        return this.setColor(new Color(r, g, b));
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
     * <pre>{@code
     * // draw just an outline:
     * Circle c = new Circle(100, 100, 50, Color.RED);
     * c.setOutline(5);
     * c.setFilled(false);
     * }</pre>
     * 
     * @param filled The value to set {@code this.filled} to
     * @return The original object to allow method chaining
     * @see #setOutline(int)
     */
    public T setFilled(boolean filled) {
        this.filled = filled;
        return getThis();
    }
    
    /**
     * <p>
     * Erase this object from the canvas. This object will be gone and cannot* be added back!
     * </p><p>
     * <sub>* It can be added back just don't tell anyone shhhh</sub>
     * </p>
     */
    public void erase() {
        App.canvas.erase(this);
    }
    
    public App.AnimationBuilder animate() {
        return new App.AnimationBuilder(this);
    }
    
    public Drawable<?> drawable() {
        return this;
    }
}
