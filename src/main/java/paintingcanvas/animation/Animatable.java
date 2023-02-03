package paintingcanvas.animation;

import paintingcanvas.App;
import paintingcanvas.drawable.Drawable;

import java.awt.*;

/**
 * An interface to call animation methods on animatable objects.
 */
public interface Animatable {
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
    App.AnimationBuilder animate();

    /**
     * Get the {@link Drawable} element from this {@code Animatable}.
     *
     * @return the {@link Drawable}
     */
    Drawable drawable();

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
    default App.AnimationBuilder moveTo(int x, int y, double duration) {
        var animation = Animation.moveTo(x, y);
        animation.drawable = drawable();
        return this.animate().add(animation, duration);
    }

    /**
     * This method moves {@code this} by the specified {@code x} and {@code y}
     *
     * <pre>{@code
     * Circle c = new Circle(200, 200, 50);
     * // the circle will move down 100, and then right 200
     * c.moveTo(0, 100, 3).moveTo(200, 0, 3);
     * }</pre>
     *
     * @param x        the x to move by
     * @param y        the y to move by
     * @param duration the number of seconds it lasts
     * @return an {@code AnimationBuilder}
     * @see #animate()
     */
    default App.AnimationBuilder move(int x, int y, double duration) {
        var animation = Animation.moveTo(x + drawable().x, y + drawable().y);
        animation.drawable = drawable();
        return this.animate().add(animation, duration);
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
    default App.AnimationBuilder colorTo(int r, int g, int b, double duration) {
        var animation = Animation.colorTo(r, g, b);
        animation.drawable = drawable();
        return this.animate().add(animation, duration);
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
    default App.AnimationBuilder colorTo(int hex, double duration) {
        var animation = Animation.colorTo(hex);
        animation.drawable = drawable();
        return this.animate().add(animation, duration);
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
    default App.AnimationBuilder colorTo(Color color, double duration) {
        var animation = Animation.colorTo(color);
        animation.drawable = drawable();
        return this.animate().add(animation, duration);
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
    default App.AnimationBuilder rotateTo(int angle, double duration) {
        var animation = Animation.rotateTo(angle);
        animation.drawable = drawable();
        return this.animate().add(animation, duration);
    }

    /**
     * Creates a new rotation animation to rotate this object by <code>angle°</code>.
     *
     * <pre>{@code
     * Square s = new Square(200, 200, 50);
     * // the square will rotate one turn counter-clockwise, then one turns clockwise
     * c.rotateTo(360, 3).colorTo(-360, 3);
     * }</pre>
     *
     * @param angle    The relative angle to rotate to in degrees.
     * @param duration the number of seconds it lasts
     * @return an {@code AnimationBuilder}
     * @see #animate()
     */
    default App.AnimationBuilder rotate(int angle, double duration) {
        var animation = Animation.rotateTo(angle + (int)Math.toDegrees(drawable().rotation));
        animation.drawable = drawable();
        return this.animate().add(animation, duration);
    }
}
