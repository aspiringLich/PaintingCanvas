package paintingcanvas.animation;

import paintingcanvas.drawable.Drawable;

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
     * Creates an animation that moves {@code this} to the specified {@code x} and {@code y}
     * over {@code duration} seconds
     *
     * <pre>{@code
     * Circle c = new Circle(200, 200, 50);
     * // the circle will move to (100, 100), and then to (200, 200)
     * c.moveTo(100, 100, 3).moveTo(200, 200, 3);
     * }</pre>
     *
     * @param x the x-position to move to
     * @param y the y-position to move to
     * @return an {@link Animation}
     */
    public static Animation moveTo(int x, int y) {
        return new MovementAnimation(new Point(x, y));
    }

    /**
     * Creates an animation that changes the color of {@code this} to the specified {@code color} over {@code duration} seconds.
     * See <a href="https://en.wikipedia.org/wiki/RGB_color_model">Wikipedia</a> for how this works.
     *
     * <pre>{@code
     * Circle c = new Circle(200, 200, 50);
     * // the circle will turn red, and then blue
     * c.colorTo(255, 0, 0, 3).colorTo(0, 0, 255, 3);
     * }</pre>
     *
     * @param r red (0-255)
     * @param g green (0-255)
     * @param b blue (0-255)
     * @return an {@link Animation}
     */
    public static Animation colorTo(int r, int g, int b) {
        return new ColorAnimation(new Color(r, g, b));
    }

    /**
     * Creates an animation that changes the color of {@code this} to the specified {@code color} over {@code duration} seconds.
     * See <a href="https://en.wikipedia.org/wiki/RGB_color_model#Numeric_representations">Wikipedia</a> for how this works.
     *
     * <pre>{@code
     * Circle c = new Circle(200, 200, 50);
     * // the circle will turn red, and then blue
     * c.colorTo(0xFF0000, 3).colorTo(0x0000FF, 3);
     * }</pre>
     *
     * @param hex The number describing the RGB color
     * @return an {@link Animation}
     */
    public static Animation colorTo(int hex) {
        return new ColorAnimation(new Color(hex));
    }

    /**
     * Creates an animation that changes the color of {@code this} to the specified {@code color} over {@code duration} seconds.
     * See {@link Color}
     * for the full list of colors, and constructors for this class
     *
     * <pre>{@code
     * Circle c = new Circle(200, 200, 50);
     * // the circle will turn red, and then blue
     * c.colorTo(Color.RED, 3).colorTo(Color.BLUE, 3);
     * }</pre>
     *
     * @param color The color to fade to
     * @return an {@link Animation}
     */
    public static Animation colorTo(Color color) {
        return new ColorAnimation(color);
    }

    /**
     * Creates an animation that fades {@code this} out over @{code duration} seconds.
     *
     * <pre>{@code
     * Circle c = new Circle(200, 200, 50);
     * // the circle will fade out, then in
     * c.fadeOut(3).fadeIn(3);
     * }</pre>
     *
     * @return an {@link Animation}
     * @see #fadeIn()
     */
    public static Animation fadeOut() {
        return new OpacityAnimation(0);
    }

    /**
     * Creates an animation that fades {@code this} in over @{code duration} seconds.
     *
     * <pre>{@code
     * Circle c = new Circle(200, 200, 50);
     * // the circle will fade out, then in
     * c.fadeOut(3).fadeIn(3);
     * }</pre>
     *
     * @return an {@link Animation}
     * @see #fadeOut()
     */
    public static Animation fadeIn() {
        return new OpacityAnimation(1);
    }

    /**
     * Creates an animation that rotates {@code this} to the specified <code>angle°</code>.
     * If you supply an angle {@code > 360} it will make more than one full rotation.
     *
     * <pre>{@code
     * Square s = new Square(200, 200, 50);
     * // the square will rotate one turn counter-clockwise, then 2 turns clockwise
     * c.rotateTo(360, 3).colorTo(-360, 3);
     * }</pre>
     *
     * @param angle The absolute angle to rotate to in degrees.
     * @return an {@code AnimationBuilder}
     */
    public static Animation rotateTo(int angle) {
        return new RotationAnimation(Math.toRadians(angle));
    }

    /**
     * Creates an animation that rotates {@code this} by <code>angle°</code>.
     *
     * <pre>{@code
     * Square s = new Square(200, 200, 50);
     * // the square will rotate one turn counter-clockwise, then one turns clockwise
     * c.rotateTo(360, 3).colorTo(-360, 3);
     * }</pre>
     *
     * @param angle The relative angle to rotate to in degrees.
     * @return an {@link Animation}
     */
    public static Animation rotateBy(int angle) {
        return new RotationAnimation(Math.toRadians(angle)).relative();
    }

    /**
     * Creates an animation that moves {@code this} by the specified {@code x} and {@code y}
     *
     * <pre>{@code
     * Circle c = new Circle(200, 200, 50);
     * // the circle will move down 100, and then right 200
     * c.moveTo(0, 100, 3).moveTo(200, 0, 3);
     * }</pre>
     *
     * @param x the x to move by
     * @param y the y to move by
     * @return an {@link Animation}
     */
    public static Animation moveBy(int x, int y) {
        return new MovementAnimation(new Point(x, y)).relative();
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
