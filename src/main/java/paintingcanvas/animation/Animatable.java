package paintingcanvas.animation;

import paintingcanvas.drawable.Drawable;
import paintingcanvas.misc.Hue;

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
     * // the circle will slowly move to (100, 100) over 3 seconds
     * c.moveTo(100, 100, 3);
     * }</pre>
     *
     * @param x        the x position to move to
     * @param y        the y position to move to
     * @param duration the number of seconds it lasts
     * @return an {@link AnimationBuilder}
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
     * // the circle will slowly turn red over 3 seconds
     * c.colorTo(255, 0, 0, 3);
     * }</pre>
     *
     * @param r        red (0-255)
     * @param g        green (0-255)
     * @param b        blue (0-255)
     * @param duration the number of seconds it lasts
     * @return an {@link AnimationBuilder}
     */
    default AnimationBuilder colorTo(int r, int g, int b, double duration) {
        return this.addAnimation(Animation.colorTo(r, g, b), duration);
    }

    /**
     * Change the color of {@code this} to the specified {@code color} over {@code duration} seconds.
     * See <a href="https://en.wikipedia.org/wiki/RGBA_color_model">Wikipedia</a> for how this works.
     *
     * <pre>{@code
     * Circle c = new Circle(200, 200, 50);
     * // the circle will slowly turn red with with 100/255 opacity over three seconds
     * c.colorTo(255, 0, 0, 100, 3);
     * }</pre>
     *
     * @param r        red (0-255)
     * @param g        green (0-255)
     * @param b        blue (0-255)
     * @param a        alpha (0-255)
     * @param duration the number of seconds it lasts
     * @return an {@link AnimationBuilder}
     */
    default AnimationBuilder colorTo(int r, int g, int b, int a, double duration) {
        return this.addAnimation(Animation.colorTo(r, g, b, a), duration);
    }

    /**
     * Change the color of {@code this} to the specified {@code color} over {@code duration} seconds.
     * See <a href="https://en.wikipedia.org/wiki/RGB_color_model#Numeric_representations">Wikipedia</a> for how this works.
     *
     * <pre>{@code
     * Circle c = new Circle(200, 200, 50);
     * // the circle will slowly turn red over three seconds
     * c.colorTo(0xFF0000, 3);
     * }</pre>
     *
     * @param hex      the number describing the RGB color
     * @param duration the number of seconds it lasts
     * @return an {@link AnimationBuilder}
     */
    default AnimationBuilder colorTo(int hex, double duration) {
        return this.addAnimation(Animation.colorTo(hex), duration);
    }

    /**
     * Change the color of {@code this} to the specified {@code color} over {@code duration} seconds.
     *
     * <pre>{@code
     * Circle c = new Circle(200, 200, 50);
     * // the circle will slowly turn red over 3 seconds
     * c.colorTo(0xFF0000, 3);
     * }</pre>
     *
     * @param hue      the hue
     * @param duration the number of seconds it lasts
     * @return a {@link ColorAnimation}
     */
    default AnimationBuilder colorTo(Hue hue, double duration) {
        return this.addAnimation(Animation.colorTo(hue), duration);
    }

    /**
     * Change the color of {@code this} to the specified {@code color} over {@code duration} seconds.
     *
     * <pre>{@code
     * Circle c = new Circle(200, 200, 50);
     * // the circle will slowly turn red over 3 seconds
     * // then, the circle will slowly turn blue over 3 seconds
     * c.colorTo("red", 3).colorTo("#0000FF", 3);
     * }</pre>
     *
     * @param name     the hue name or the hex code
     * @param duration the number of seconds it lasts
     * @return an {@link AnimationBuilder}
     */
    default AnimationBuilder colorTo(String name, double duration) {
        return this.addAnimation(Animation.colorTo(name), duration);
    }

    /**
     * Change the color of {@code this} to the specified {@code color} over {@code duration} seconds.
     * See {@link Color} for the full list of colors, and constructors for this class
     *
     * <pre>{@code
     * Circle c = new Circle(200, 200, 50);
     * // the circle will turn slowly red over 3 seconds
     * c.colorTo(Color.RED, 3);
     * }</pre>
     *
     * @param color    the color to fade to
     * @param duration the number of seconds it lasts
     * @return an {@link AnimationBuilder}
     */
    default AnimationBuilder colorTo(Color color, double duration) {
        return this.addAnimation(Animation.colorTo(color), duration);
    }

    /**
     * Fade {@code this} out over {@code duration} seconds.
     *
     * <pre>{@code
     * Circle c = new Circle(200, 200, 50);
     * // the circle will slowly fade out over 3 seconds
     * c.fadeOut(3);
     * }</pre>
     *
     * @param duration the number of seconds it lasts
     * @return an {@link AnimationBuilder}
     */
    default AnimationBuilder fadeOut(double duration) {
        return this.addAnimation(Animation.fadeOut(), duration);
    }

    /**
     * Fade {@code this} in over {@code duration} seconds.
     *
     * <pre>{@code
     * Circle c = new Circle(200, 200, 50);
     * // the circle will slowly fade out over 3 seconds
     * // then, fade back in over 3 seconds
     * c.fadeOut(3).fadeIn(3);
     * }</pre>
     *
     * @param duration the number of seconds it lasts
     * @return an {@link AnimationBuilder}
     */
    default AnimationBuilder fadeIn(double duration) {
        return this.addAnimation(Animation.fadeIn(), duration);
    }

    /**
     * Rotate {@code this} to the specified {@code angle} degrees over {@code duration} seconds.
     * If you supply an angle {@code > 360} it will make more than one full rotation.
     *
     * <pre>{@code
     * Square s = new Square(200, 200, 50);
     * // the square will slowly rotate one turn counter-clockwise over 3 seconds
     * // then, slowly rotate two turns clockwise over 3 seconds
     * c.rotateTo(360, 3).rotateTo(-360, 3);
     * }</pre>
     *
     * @param angle    the absolute angle to rotate to in degrees.
     * @param duration the number of seconds it lasts
     * @return an {@link AnimationBuilder}
     */
    default AnimationBuilder rotateTo(int angle, double duration) {
        return this.addAnimation(Animation.rotateTo(angle), duration);
    }

    /**
     * Rotate {@code this} by {@code angle} degrees over {@code duration} seconds.
     * If you supply an angle {@code > 360} it will make more than one full rotation.
     *
     * <pre>{@code
     * Square s = new Square(200, 200, 50);
     * // the square will slowly rotate one turn counter-clockwise over 3 seconds
     * // then, slowly rotate one turn clockwise over 3 seconds
     * c.rotateBy(360, 3).rotateBy(-360, 3);
     * }</pre>
     *
     * @param angle    the relative angle to rotate to in degrees.
     * @param duration the number of seconds it lasts
     * @return an {@link AnimationBuilder}
     */
    default AnimationBuilder rotateBy(int angle, double duration) {
        return this.addAnimation(Animation.rotateBy(angle), duration);
    }

    /**
     * Move {@code this} by the specified {@code x} and {@code y} over {@code duration} seconds.
     *
     * <pre>{@code
     * Circle c = new Circle(200, 200, 50);
     * // the circle will slowly move down 100 over 3 seconds
     * // then, slowly move right over 3 seconds
     * c.moveBy(0, 100, 3).moveBy(200, 0, 3);
     * }</pre>
     *
     * @param x        the x to move by
     * @param y        the y to move by
     * @param duration the number of seconds it lasts
     * @return an {@link AnimationBuilder}
     */
    default AnimationBuilder moveBy(int x, int y, double duration) {
        return this.addAnimation(Animation.moveBy(x, y), duration);
    }

    /**
     * Move {@code this} by the specified {@code x} horizontally over {@code duration} seconds.
     * Positive values move right, negative values move left.
     * This method is equivalent to {@code moveBy(x, 0, duration)}
     *
     * <pre>{@code
     * Circle c = new Circle(200, 200, 50);
     * // the circle will move slowly right 300 pixels over 3 seconds
     * c.moveHorizontalBy(100, 3);
     * }</pre>
     *
     * @param x        the x to move by
     * @param duration the number of seconds it lasts
     * @return an {@link AnimationBuilder}
     */
    default AnimationBuilder moveHorizontalBy(int x, double duration) {
        return this.addAnimation(Animation.moveHorizontalBy(x), duration);
    }

    /**
     * Move {@code this} by the specified {@code y} vertically over {@code duration} seconds.
     * Positive values move down, negative values move up.
     * This method is equivalent to {@code moveBy(0, y, duration)}
     *
     * <pre>{@code
     * Circle c = new Circle(200, 200, 50);
     * // the circle will move right 300 pixels over 3 seconds
     * c.moveVerticalBy(100, 3);
     * }</pre>
     *
     * @param y        the y to move by
     * @param duration the number of seconds it lasts
     * @return an {@link AnimationBuilder}
     */
    default AnimationBuilder moveVerticalBy(int y, double duration) {
        return this.addAnimation(Animation.moveVerticalBy(y), duration);
    }
}
