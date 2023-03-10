package paintingcanvas.animation;

import paintingcanvas.drawable.Drawable;

import java.awt.*;

/**
 * An interface to call animation methods on animatable objects.
 */
public interface Animatable {
    /**
     * Get the {@link Drawable} element from this {@code Animatable}.
     *
     * @return the {@link Drawable}
     */
    Drawable<?> drawable();

    private AnimationBuilder addAnimation(Animation animation, double duration) {
        animation.drawable = drawable();
        return drawable().animate().add(animation, duration);
    }

    /**
     * Move {@code this} to the specified {@code x} and {@code y} over {@code duration} seconds
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
     * @return an {@code this}
     */
    default AnimationBuilder moveTo(int x, int y, double duration) {
        return this.addAnimation(Animation.moveTo(x, y), duration);
    }

    /**
     * Change the color of {@code this} to the specified {@code color} over {@code duration} seconds.
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
     * @return an {@code this}
     */
    default AnimationBuilder colorTo(int r, int g, int b, double duration) {
        return this.addAnimation(Animation.colorTo(r, g, b), duration);
    }

    /**
     * Change the color of {@code this} to the specified {@code color} over {@code duration} seconds.
     * See <a href="https://en.wikipedia.org/wiki/RGB_color_model#Numeric_representations">Wikipedia</a>
     * for how this works.
     *
     * <pre>{@code
     * Circle c = new Circle(200, 200, 50);
     * // the circle will turn red, and then blue
     * c.colorTo(0xFF0000, 3).colorTo(0x0000FF, 3);
     * }</pre>
     *
     * @param hex      The number describing the RGB color
     * @param duration the number of seconds it lasts
     * @return an {@code this}
     */
    default AnimationBuilder colorTo(int hex, double duration) {
        return this.addAnimation(Animation.colorTo(hex), duration);
    }

    /**
     * Change the color of {@code this} to the specified {@code color} over {@code duration} seconds.
     * See {@link Color}
     * for the full list of colors, and constructors for this class
     *
     * <pre>{@code
     * Circle c = new Circle(200, 200, 50);
     * // the circle will turn red, and then blue
     * c.colorTo(Color.RED, 3).colorTo(Color.BLUE, 3);
     * }</pre>
     *
     * @param color    The color to fade to
     * @param duration the number of seconds it lasts
     * @return an {@code this}
     */
    default AnimationBuilder colorTo(Color color, double duration) {
        return this.addAnimation(Animation.colorTo(color), duration);
    }

    /**
     * Fades {@code this} out over @{code duration} seconds.
     *
     * <pre>{@code
     * Circle c = new Circle(200, 200, 50);
     * // the circle will fade out, then in
     * c.fadeOut(3).fadeIn(3);
     * }</pre>
     *
     * @param duration the amount of time it takes to fade out
     * @return an {@code this}
     * @see #fadeIn(double)
     */
    default AnimationBuilder fadeOut(double duration) {
        return this.addAnimation(Animation.fadeOut(), duration);
    }

    /**
     * Fades {@code this} in over @{code duration} seconds.
     *
     * <pre>{@code
     * Circle c = new Circle(200, 200, 50);
     * // the circle will fade out, then in
     * c.fadeOut(3).fadeIn(3);
     * }</pre>
     *
     * @param duration the amount of time it takes to fade out
     * @return an {@code this}
     * @see #fadeOut(double)
     */
    default AnimationBuilder fadeIn(double duration) {
        return this.addAnimation(Animation.fadeIn(), duration);
    }

    /**
     * Rotates {@code this} to the specified <code>angle??</code>.
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
     * @return an {@code this}
     */
    default AnimationBuilder rotateTo(int angle, double duration) {
        return this.addAnimation(Animation.rotateTo(angle), duration);
    }

    /**
     * Rotates {@code this} by <code>angle??</code>.
     *
     * <pre>{@code
     * Square s = new Square(200, 200, 50);
     * // the square will rotate one turn counter-clockwise, then one turns clockwise
     * c.rotateTo(360, 3).colorTo(-360, 3);
     * }</pre>
     *
     * @param angle    The relative angle to rotate to in degrees.
     * @param duration the number of seconds it lasts
     * @return an {@code this}
     */
    default AnimationBuilder rotateBy(int angle, double duration) {
        return this.addAnimation(Animation.rotateBy(angle), duration);
    }

    /**
     * This method moves {@code this} by the specified {@code x} and {@code y}
     *
     * <pre>{@code
     * Circle c = new Circle(200, 200, 50);
     * // the circle will move down 100, and then right 200
     * c.moveBy(0, 100, 3).moveBy(200, 0, 3);
     * }</pre>
     *
     * @param x        the x to move by
     * @param y        the y to move by
     * @param duration the number of seconds it lasts
     * @return an {@code this}
     */
    default AnimationBuilder moveBy(int x, int y, double duration) {
        return this.addAnimation(Animation.moveBy(x, y), duration);
    }

    /**
     * <p>
     * This method moves {@code this} by the specified {@code x} horizontally.
     * Positive values move right, negative values move left.
     * </p><p>
     * This method is equivalent to {@code moveBy(x, 0, duration)}
     * </p>
     *
     * <pre>{@code
     * Circle c = new Circle(200, 200, 50);
     * // the circle will move right 300 pixels over 3 seconds
     * c.moveHorizontalBy(0, 100, 3);
     * }</pre>
     *
     * @param x        the x to move by
     * @param duration the number of seconds it lasts
     * @return an {@code this}
     */
    default AnimationBuilder moveHorizontalBy(int x, double duration) {
        return this.addAnimation(Animation.moveBy(x, 0).relative(), duration);
    }

    /**
     * <p>
     * This method moves {@code this} by the specified {@code x} horizontally.
     * Positive values move right, negative values move left.
     * </p><p>
     * This method is equivalent to {@code moveBy(0, y, duration)}
     * </p>
     * <pre>{@code
     * Circle c = new Circle(200, 200, 50);
     * // the circle will move right 300 pixels over 3 seconds
     * c.moveHorizontalBy(0, 100, 3);
     * }</pre>
     *
     * @param y        the y to move by
     * @param duration the number of seconds it lasts
     * @return an {@code this}
     */
    default AnimationBuilder moveVerticalBy(int y, double duration) {
        return this.addAnimation(Animation.moveBy(0, y).relative(), duration);
    }
}
