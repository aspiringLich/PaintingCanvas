package paintingcanvas.animation;

import paintingcanvas.drawable.Drawable;
import paintingcanvas.misc.Hue;
import paintingcanvas.misc.Misc;

import java.awt.*;

/**
 * A class that stores information about animations transitions
 */
public abstract class Animation {
    /**
     * The frame at which the animation should start
     */
    public int startFrame;
    /**
     * The length of the animation in frames
     */
    public int duration;
    /**
     * The easing function to use
     */
    public Easing easing = Easing.linear();
    /**
     * The Drawable this animation acts on
     */
    Drawable<?> drawable;

    protected Animation(int startFrame, int duration) {
        this.startFrame = startFrame;
        this.duration = duration;
    }

    protected Animation() {
        this(0, 0);
    }

    /**
     * Create an animation that moves {@code this} to the specified {@code x} and {@code y}
     *
     * <pre>{@code
     * Circle c = new Circle(200, 200, 50);
     * // the circle will slowly move to (100, 100) over 3 seconds
     * c.animate().add(Animation.moveTo(100, 100), 3);
     * }</pre>
     *
     * @param x the x position to move to
     * @param y the y position to move to
     * @return a {@link MovementAnimation}
     */
    public static MovementAnimation moveTo(int x, int y) {
        return new MovementAnimation(new Point(x, y));
    }

    /**
     * Create an animation that changes the color of {@code this} to the specified {@code color}.
     * See <a href="https://en.wikipedia.org/wiki/RGB_color_model">Wikipedia</a> for how this works.
     *
     * <pre>{@code
     * Circle c = new Circle(200, 200, 50);
     * // the circle will slowly turn red over 3 seconds
     * c.animate().add(Animation.colorTo(255, 0, 0), 3);
     * }</pre>
     *
     * @param r red (0-255)
     * @param g green (0-255)
     * @param b blue (0-255)
     * @return a {@link ColorAnimation}
     */
    public static ColorAnimation colorTo(int r, int g, int b) {
        return new ColorAnimation(new Color(r, g, b));
    }

    /**
     * Create an animation that changes the color of {@code this} to the specified {@code color}.
     * See <a href="https://en.wikipedia.org/wiki/RGBA_color_model">Wikipedia</a> for how this works.
     *
     * <pre>{@code
     * Circle c = new Circle(200, 200, 50);
     * // the circle will slowly turn red with with 100/255 opacity over three seconds
     * c.animate().add(Animation.colorTo(255, 0, 0), 3);
     * }</pre>
     *
     * @param r red (0-255)
     * @param g green (0-255)
     * @param b blue (0-255)
     * @param a alpha (0-255)
     * @return a {@link ColorAnimation}
     */
    public static ColorAnimation colorTo(int r, int g, int b, int a) {
        return new ColorAnimation(new Color(r, g, b, a));
    }

    /**
     * Create an animation that changes the color of {@code this} to the specified {@code color}.
     * See <a href="https://en.wikipedia.org/wiki/RGB_color_model#Numeric_representations">Wikipedia</a> for how this works.
     *
     * <pre>{@code
     * Circle c = new Circle(200, 200, 50);
     * // the circle will slowly turn red over three seconds
     * c.animate().add(Animation.colorTo(0xFF0000), 3);
     * }</pre>
     *
     * @param hex the number describing the RGB color
     * @return a {@link ColorAnimation}
     */
    public static ColorAnimation colorTo(int hex) {
        return new ColorAnimation(new Color(hex));
    }

    /**
     * Create an animation that changes the color of {@code this} to the specified {@code color}.
     *
     * <pre>{@code
     * Circle c = new Circle(200, 200, 50);
     * // the circle will turn red, and then blue
     * c.colorTo(0xFF0000, 3).colorTo(0x0000FF, 3);
     * }</pre>
     *
     * @param hue the hue
     * @return a {@link ColorAnimation}
     */
    public static ColorAnimation colorTo(Hue hue) {
        return new ColorAnimation(hue.getColor());
    }

    /**
     * Create an animation that changes the color of {@code this} to the specified {@code color}.
     *
     * <pre>{@code
     * Circle c = new Circle(200, 200, 50);
     * // the circle will slowly turn red over 3 seconds
     * // then, the circle will slowly turn blue over 3 seconds
     * c.animate().add(Animation.colorTo("red"), 3).add(Animation.colorTo("#0000FF"), 3);
     * }</pre>
     *
     * @param name the hue name or the hex code
     * @return a {@link ColorAnimation}
     */
    public static ColorAnimation colorTo(String name) {
        return new ColorAnimation(Misc.stringToColor(name));
    }

    /**
     * Create an animation that changes the color of {@code this} to the specified {@code color}.
     * See {@link Color} for the full list of colors, and constructors for this class
     *
     * <pre>{@code
     * Circle c = new Circle(200, 200, 50);
     * // the circle will turn slowly red over 3 seconds
     * c.animate().add(Animation.colorTo(Color.RED, 3));
     * }</pre>
     *
     * @param color the color to fade to
     * @return an {@link Animation}
     */
    public static Animation colorTo(Color color) {
        return new ColorAnimation(color);
    }

    /**
     * Create an animation that fades {@code this} out.
     *
     * <pre>{@code
     * Circle c = new Circle(200, 200, 50);
     * // the circle will slowly fade out over 3 seconds
     * c.animate().add(Animation.fadeOut(), 3);
     * }</pre>
     *
     * @return an {@link OpacityAnimation}
     * @see #fadeIn()
     */
    public static OpacityAnimation fadeOut() {
        return new OpacityAnimation(0);
    }

    /**
     * Create an animation that fades {@code this} in.
     *
     * <pre>{@code
     * Circle c = new Circle(200, 200, 50);
     * // the circle will slowly fade out over 3 seconds
     * // then, fade back in over 3 seconds
     * c.animate().add(Animation.fadeOut(), 3).add(Animation.fadeIn(), 3);
     * }</pre>
     *
     * @return an {@link OpacityAnimation}
     * @see #fadeOut()
     */
    public static OpacityAnimation fadeIn() {
        return new OpacityAnimation(255);
    }

    /**
     * Create an animation that rotates {@code this} to the specified {@code angle} degrees.
     * If you supply an angle {@code > 360} it will make more than one full rotation.
     *
     * <pre>{@code
     * Square s = new Square(200, 200, 50);
     * // the square will slowly rotate one turn counter-clockwise over 3 seconds
     * // then, slowly rotate two turns clockwise over 3 seconds
     * c.animate().add(Animation.rotateTo(360), 3).add(Animation.rotateTo(-360), 3);
     * }</pre>
     *
     * @param angle The absolute angle to rotate to in degrees.
     * @return a {@link RotationAnimation}
     */
    public static RotationAnimation rotateTo(double angle) {
        return new RotationAnimation(angle);
    }

    /**
     * Create an animation that rotates {@code this} by {@code angle} degrees.
     *
     * <pre>{@code
     * Square s = new Square(200, 200, 50);
     * // the square will slowly rotate one turn counter-clockwise over 3 seconds
     * // then, slowly rotate one turn clockwise over 3 seconds
     * c.animate().add(Animation.rotateBy(360), 3).add(Animation.rotateBy(-360), 3);
     * }</pre>
     *
     * @param angle The relative angle to rotate to in degrees.
     * @return a {@link RotationAnimation}
     */
    public static RotationAnimation rotateBy(double angle) {
        return new RotationAnimation(angle).relative();
    }

    /**
     * Create an animation that moves {@code this} by the specified {@code x} and {@code y}
     *
     * <pre>{@code
     * Circle c = new Circle(200, 200, 50);
     * // the circle will slowly move down 100 over 3 seconds
     * // then, slowly move right over 3 seconds
     * c.animate().add(Animation.moveTo(0, 100), 3).add(Animation.moveTo(200, 0), 3);
     * }</pre>
     *
     * @param x the x to move by
     * @param y the y to move by
     * @return a {@link MovementAnimation}
     */
    public static MovementAnimation moveBy(int x, int y) {
        return new MovementAnimation(new Point(x, y)).relative();
    }

    /**
     * Create an animation that moves {@code this} by the specified {@code x} horizontally.
     * Positive values move right, negative values move left.
     * This method is equivalent to {@code moveBy(x, 0)}
     *
     * <pre>{@code
     * Circle c = new Circle(200, 200, 50);
     * // the circle will move slowly right 300 pixels over 3 seconds
     * c.animate().add(Animation.moveHorizontalBy(100), 3);
     * }</pre>
     *
     * @param x the x to move by
     * @return an {@link AnimationBuilder}
     */
    public static MovementAnimation moveHorizontalBy(int x) {
        return new MovementAnimation(new Point(x, 0)).relative();
    }

    /**
     * Create an animation that moves {@code this} by the specified {@code y} vertically.
     * Positive values move down, negative values move up.
     * This method is equivalent to {@code moveBy(0, y)}
     *
     * <pre>{@code
     * Circle c = new Circle(200, 200, 50);
     * // the circle will move right 300 pixels over 3 seconds
     * c.animate().add(Animation.moveVerticalBy(100), 3);
     * }</pre>
     *
     * @param y the y to move by
     * @return an {@link AnimationBuilder}
     */
    public static MovementAnimation moveVerticalBy(int y) {
        return new MovementAnimation(new Point(0, y)).relative();
    }

    /**
     * Internal method that is called to help copy this object
     */
    void copy(Animation in) {
        startFrame = in.startFrame;
        duration = in.duration;
        easing = in.easing;
    }

    abstract public Animation copy();

    /**
     * Sets the easing to be used by this animation
     *
     * @param easing the easing to be used
     * @return this
     */
    public Animation easing(Easing easing) {
        this.easing = easing;
        return this;
    }

    /**
     * Updates the animation with the current frame
     *
     * @param frame the current frame
     */
    public void update(int frame) {
        if (frame >= startFrame)
            this.updateAnimation(this.drawable, (frame - startFrame) / (double) this.duration);
    }

    /**
     * update the animation with the progress (0-1) and affected drawable
     *
     * @param drawable The affected drawable
     * @param progress Animation progress (0-1)
     */
    protected abstract void updateAnimation(Drawable<?> drawable, double progress);

    /**
     * Initialize the animation with the affected drawable
     *
     * @return this
     */
    Animation init(Drawable<?> drawable, int startFrame, int duration) {
        this.drawable = drawable;
        this.startFrame = startFrame;
        this.duration = duration;
        this.initAnimation(this.drawable);
        return this;
    }

    /**
     * Initialize the animation with the affected drawable
     *
     * @param drawable The affected drawable
     */
    protected abstract void initAnimation(Drawable<?> drawable);

    public boolean ended(int frame) {
        return frame > this.startFrame + this.duration;
    }
}
