package paintingcanvas.drawable;

import paintingcanvas.misc.ElementContainer;
import paintingcanvas.misc.Hue;
import paintingcanvas.misc.Misc;

import java.awt.*;

/**
 * <p>
 *      Elements that can be colored in with a {@link java.awt.Color}
 * </p>
 *
 * @param <T> the type of the object
 */
interface Colorable<T extends Drawable<T>> extends Drawable<T> {
    void internalSetColor(Color color);

    /**
     * Get the current color of an element as a {@link Color}
     *
     * @return the {@link Color} of the element
     */
    Color getColor();

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
    default T setColor(Color color) {
        ElementContainer.atomic(() -> internalSetColor(color));
        return getThis();
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
    default T setColor(int hex) {
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
    default T setColor(Hue hue) {
        return setColor(hue.getColor());
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
    default T setColor(String name) {
        return setColor(Misc.stringToColor(name));
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
    default T setColor(int r, int g, int b) {
        return setColor(new Color(r, g, b));
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
    default T setColor(int r, int g, int b, int a) {
        return this.setColor(new Color(r, g, b, a));
    }
}
