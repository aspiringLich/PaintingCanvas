package paintingcanvas.drawable;

import paintingcanvas.InternalCanvas;
import paintingcanvas.misc.ElementContainer;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.function.Consumer;

/**
 * <p>
 * Elements that can be translated and rotated
 * </p>
 *
 * @param <T> the type of the object
 */
public interface Positionable<T extends Drawable<T>> extends Drawable<T> {
    void internalSetPos(int x, int y);
    double internalGetRotation();
    void internalSetRotation(double rotation);

    /**
     * Get the position of the element
     *
     * @return the position of the element as a {@link Point}
     * @see #setPos(int, int)
     */
    public Point getPos();

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
    public double getRotation();

    /**
     * Get the X-position of the element
     *
     * @return the X-position of the element
     * @see #getY()
     * @see #setX(int)
     */
    public default int getX() {
        return getPos().x;
    }

    /**
     * Set the X-position of the object
     *
     * @param x the new X-position of the Object
     * @return the original object to allow method chaining
     * @see #getX()
     * @see #setY(int)
     */
    public default T setX(int x) {
        ElementContainer.atomic(() -> internalSetPos(x, getY()));
        return getThis();
    }

    /**
     * Get the Y-position of the element.
     *
     * @return the Y-position of the object
     * @see #getY()
     * @see #setX(int)
     */
    public default int getY() {
        return getPos().y;
    }

    /**
     * Set the Y-position of the element
     *
     * @param y the new Y-position of the Object
     * @return the original object to allow method chaining
     * @see #setX(int)
     * @see #getY()
     */
    public default T setY(int y) {
        ElementContainer.atomic(() -> internalSetPos(getX(), y));
        return getThis();
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
    public default T setPos(int x, int y) {
        ElementContainer.atomic(() -> internalSetPos(x, y));
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
    public default T move(int x, int y) {
        ElementContainer.atomic(() -> internalSetPos(getX() + x, getY() + y));
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
    public default T moveHorizontal(int x) {
        return move(x, 0);
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
    public default T moveVertical(int y) {
        return move(0, y);
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
    public default T rotate(double rotation) {
        ElementContainer.atomic(() -> internalSetRotation(internalGetRotation() + Math.toRadians(rotation)));
        return getThis();
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
    public default T setRotation(double rotation) {
        ElementContainer.atomic(() -> internalSetRotation(Math.toRadians(rotation)));
        return getThis();
    }
}
