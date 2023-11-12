package paintingcanvas.drawable;

import paintingcanvas.misc.ElementContainer;

import java.awt.*;

/**
 * <p>
 * Elements that can be outlined.
 * </p>
 * <p>
 * If stroke is null, the outline should be removed.
 * </p>
 * <p>
 * Also provides fill methods because yes
 * </p>
 *
 * @param <T> the type of the object
 */
public interface Outlineable<T extends Drawable<T>> extends Drawable<T> {
    void internalSetOutlineStroke(Stroke stroke);

    void internalSetOutlineColor(Color color);

    void internalSetFilled(boolean filled);

    /**
     * Gets the outline color
     *
     * @return the outline color
     */
    Color getOutlineColor();


    /**
     * Gets the outline stroke
     *
     * @return the outline stroke
     */
    Stroke getOutlineStroke();

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
    default T setOutline(int thickness, Color color) {
        ElementContainer.atomic(() -> {
            internalSetOutlineStroke(new BasicStroke(thickness));
            internalSetOutlineColor(color);
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
    default T setOutline(Color color) {
        ElementContainer.atomic(() -> internalSetOutlineColor(color));
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
    default T setOutline(int thickness) {
        ElementContainer.atomic(() -> internalSetOutlineStroke(new BasicStroke(thickness)));
        return this.getThis();
    }

    /**
     * Removes the outline from the shape
     *
     * @return the original object to allow method chaining
     */
    default T removeOutline() {
        internalSetOutlineStroke(null);
        return this.getThis();
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
    default T setFilled(boolean filled) {
        ElementContainer.atomic(() -> internalSetFilled(filled));
        return getThis();
    }
}
