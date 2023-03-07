package paintingcanvas.animation;

import paintingcanvas.canvas.Canvas;
import paintingcanvas.drawable.Drawable;
import paintingcanvas.misc.TimeUnit;

/**
 * A builder used to add animations to an element.
 * <pre>{@code
 * Text text = new Text(100, 100, "Hello World");
 * text.animate()
 *     .add(moveTo(200, 200), 3);
 * }</pre>
 */
public class AnimationBuilder {
    protected final Drawable<?> drawable;

    public AnimationBuilder(Drawable<?> drawable) {
        this.drawable = drawable;
    }

    /**
     * Add a new animation to the element.
     * <pre>{@code
     * // these animations will run one after the other
     * obj.animate()
     *    .add(moveTo(100, 100), 100, TimeUnit.Frames)
     *    .add(colorTo(Color.BLUE), 100, TimeUnit.Frames);
     * }</pre>
     *
     * @param animation The animation type to add
     * @param duration  The amount of time the animation will last
     * @param unit      The unit of time used for {@code duration}
     * @return <code>this</code> to allow method chaining
     */
    public AnimationBuilder add(Animation animation, double duration, TimeUnit unit) {
        Canvas c = Canvas.getGlobalInstance();
        var _duration = unit.asFrames(duration);

        // init animation
        var _animation = animation.copy();
        _animation.drawable = drawable;
        _animation.init();
        _animation.startFrame = c.frame;
        _animation.duration = _duration;
        c.animations.add(_animation);

        // wait for this animation to finish
        c.sleep();

        return this;
    }

    /**
     * Add a new animation to the element.
     * <pre>{@code
     * // these animations will run one after the other
     * obj.animate()
     *    .add(moveTo(100, 100), 10)
     *    .add(colorTo(Color.BLUE), 10);
     * }</pre>
     *
     * @param animation The animation type to add
     * @param duration  The amount of time the animation will last. In seconds.
     * @return <code>this</code> to allow method chaining
     */
    public AnimationBuilder add(Animation animation, double duration) {
        return add(animation, duration, TimeUnit.Seconds);
    }

    /**
     * Add the animation alongside / with the previous animation.
     * <pre>{@code
     * // these animations will run at the same time
     * obj.animate()
     *    .add(moveTo(100, 100), 100, TimeUnit.Frames)
     *    .add(colorTo(Color.BLUE), 100, TimeUnit.Frames);
     * }</pre>
     *
     * @param animation The animation type to add
     * @param duration  The amount of time the animation will last
     * @param unit      The unit of time used for {@code duration}
     * @return <code>this</code> to allow method chaining
     */
    public AnimationBuilder with(Animation animation, double duration, TimeUnit unit) {
        Canvas c = Canvas.getGlobalInstance();
        var _duration = unit.asFrames(duration);

        // init animation
        var _animation = animation.copy();
        _animation.drawable = drawable;
        _animation.init();
        _animation.startFrame = c.frame;
        _animation.duration = _duration;
        c.animations.add(_animation);

        return this;
    }

    /**
     * Add the animation alongside / with the previous animation.
     * <pre>{@code
     * // these animations will run at the same time
     * obj.animate()
     *    .add(moveTo(100, 100), 10)
     *    .with(colorTo(Color.BLUE), 10);
     * }</pre>
     *
     * @param animation The animation type to add
     * @param duration  The amount of time the animation will last
     * @return <code>this</code> to allow method chaining
     */
    public AnimationBuilder with(Animation animation, double duration) {
        return with(animation, duration, TimeUnit.Seconds);
    }

    // needed to override the base object wait func

    /**
     * Sleep for the specified amount of time.
     * Used to add delays between animations
     *
     * @param seconds The amount of time to sleep for
     * @return <code>this</code> to allow method chaining
     */
    public AnimationBuilder sleep(double seconds) {
        try {
            Thread.sleep((long) (seconds * 1000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return this;
    }
}
