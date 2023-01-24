package paintingcanvas.painter;

import paintingcanvas.painter.animation.Animation;
import paintingcanvas.painter.animation.ColorAnimation;
import paintingcanvas.painter.animation.MovementAnimation;
import paintingcanvas.painter.animation.RotationAnimation;
import paintingcanvas.painter.drawable.Drawable;
import paintingcanvas.painter.misc.TimeUnit;

import java.awt.*;
import java.awt.event.ComponentEvent;

/**
 * A simpler interface for the painter.
 */
@SuppressWarnings("unused")
public abstract class App {
    // Used to block on .add() calls
    private static final Object syncObject = new Object();
    // Used to wait on sleepUntillAnimationEnds
    private static final Object userSyncObject = new Object();
    /**
     * The global Painter all Drawables access to add themselves to.
     */
    public static Painter painter;
    // The frame the builder is on (end of the last animation)
    private static int builderFrame;
    // The start of the last animation
    private static int lastBuilderFrame;
    // Used to make sure the first call to .add doesn't block
    private static boolean firstBlockingAnimation = true;
    // The frame the last animation ends on (excluding the last blocking animation)
    private static int animationFinish = -1;
    // The frame the last animation ends on (including the last animation)
    private static int lastAnimationFinish = -1;
    // The last size of the canvas, used for auto-centering of drawables
    private static Dimension lastSize;


    private static void _syncWait() {
        try {
            synchronized (syncObject) {
                syncObject.wait();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected static void sleep() {
        try {
            synchronized (userSyncObject) {
                userSyncObject.wait();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the current width of the canvas
     *
     * @return Canvas width
     * @see #height()
     */
    protected int width() {
        return painter.canvas.getWidth();
    }

    /**
     * Gets the current height of the canvas
     *
     * @return Canvas height
     * @see #width()
     */
    protected int height() {
        return painter.canvas.getHeight();
    }

    protected void _render() {
        if (animationFinish != -1 && painter.canvas.frame >= animationFinish) {
            animationFinish = -1;
            synchronized (syncObject) {
                syncObject.notify();
            }
        }

        if (lastAnimationFinish != -1 && painter.canvas.frame >= lastAnimationFinish) {
            lastAnimationFinish = -1;
            synchronized (userSyncObject) {
                userSyncObject.notify();
            }
        }

        // Run user supplied render function
        this.render();
    }

    /**
     * <code>[ADVANCED]</code>
     * An overwrite-able function to let you run code every frame.
     *
     * @see #setup()
     */
    public void render() {
    }

    /**
     * This is the function that is called when the program starts.
     * It is where you will put all your code.
     * (unless you want to be fancy)
     */
    protected abstract void setup() throws Exception;

    /**
     * Initialize and run the application.
     * (Don't worry about this)
     */
    public void run() {
        // Worth a try
        System.setProperty("sun.java2d.opengl", "true");

        // Init global painter
        painter = new Painter(1000, 600, "Java thingy ikd");

        painter.canvas.renderLifecycle = new Canvas.RenderLifecycle() {
            @Override
            public void onResize(Canvas canvas, ComponentEvent e) {
                if (lastSize == null) {
                    lastSize = e.getComponent().getSize();
                    return;
                }

                var newSize = canvas.getSize();
                if (lastSize.equals(newSize)) return;

                var widthDiff = (newSize.width - lastSize.width) / 2f;
                var heightDiff = (newSize.height - lastSize.height) / 2f;
                lastSize = newSize;

                synchronized (canvas.elements) {
                    canvas.elements.forEach(s -> {
                        s.x += widthDiff;
                        s.y += heightDiff;
                    });
                }

                synchronized (canvas.animations) {
                    canvas.animations.stream().filter(a -> a instanceof MovementAnimation).forEach(s -> {
                        var anim = (MovementAnimation) s;
                        anim.start = new Point(anim.start.x + (int) widthDiff, anim.start.y + (int) heightDiff);
                        anim.end = new Point(anim.end.x + (int) widthDiff, anim.end.y + (int) heightDiff);
                    });
                }

                canvas.repaint();
            }
        };

        // Init app
        painter.render(this);
        try {
            this.setup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // == Define animations ==

    protected void setTitle(String title) {
        painter.setTitle(title);
    }

    /**
     * Create a new animation to <code>color</code> in <a href="https://en.wikipedia.org/wiki/RGB_color_model">RGB</a>.
     * <pre>{@code
     * Circle o = new Circle(100, 100, 20);
     * o.animate().add(colorTo(255, 0, 0), 1);
     * }</pre>
     *
     * @param r The red component of the color (0-255)
     * @param g The green component of the color (0-255)
     * @param b The blue component of the color (0-255)
     * @return the {@link Animation} object
     */
    protected Animation colorTo(int r, int g, int b) {
        return colorTo(new Color(r, g, b));
    }

    /**
     * Create a new animation to <code>color</code> with a {@link Color} object.
     * <pre>{@code
     * Circle o = new Circle(100, 100, 20);
     * o.animate().add(colorTo(Color.RED), 1);
     * }</pre>
     *
     * @param color The color to animate to
     * @return the {@link Animation} object
     */
    protected Animation colorTo(Color color) {
        return new ColorAnimation(builderFrame, color, 0, null);
    }

    /**
     * Create a new animation to <code>color</code> with a <a href="https://en.wikipedia.org/wiki/RGB_color_model#Numeric_representations">8-bit RGB hex literal</a>.
     * <pre>{@code
     * Circle o = new Circle(100, 100, 20);
     * // 0xFF0000 is hex for (255, 0, 0), which is red
     * o.animate().add(colorTo(0xFF0000), 1);
     * }</pre>
     *
     * @param hex The color to animate to as a hex literal
     * @return the {@link Animation} object
     */
    protected Animation colorTo(int hex) {
        return new ColorAnimation(builderFrame, new Color(hex >> 16 & 0xff, hex >> 8 & 0xff, hex & 0xff), 0, null);
    }

    /**
     * Creates a new position animation to <code>(x, y)</code>.
     *
     * @param x The absolute x position to animate to
     * @param y The absolute y position to animate to
     * @return the {@link Animation} object
     */
    protected Animation moveTo(int x, int y) {
        return new MovementAnimation(builderFrame, 0, new Point(x, y), null);
    }

    /**
     * Creates a new rotation animation to <code>angle°</code>.
     * If you supply an angle {@code > 360} it will make more than one full rotation.
     *
     * @param angle The absolute angle to rotate to in degrees.
     * @return the {@link Animation} object
     */
    protected Animation rotateTo(int angle) {
        return new RotationAnimation(builderFrame, 0, Math.toRadians(angle), null);
    }

    /**
     * Waits for the specified time.
     *
     * @param time Time to wait
     * @param unit The unit that <code>time</code> is in
     */
    @SuppressWarnings("SameParameterValue")
    protected void sleep(float time, TimeUnit unit) {
        builderFrame += unit.asFrames(time);
        lastBuilderFrame = builderFrame;

        // Schedule unblocking thread
        synchronized (painter.canvas.events) {
            painter.canvas.events.add(new Event(builderFrame, c -> {
                synchronized (syncObject) {
                    syncObject.notify();
                }
            }));
        }

        // Wait for unblock
        _syncWait();
    }

    /**
     * Waits for the specified time in seconds.
     *
     * @param time The time in seconds.
     */
    protected void sleep(float time) {
        sleep(time, TimeUnit.Seconds);
    }

    /**
     * A builder used to add animations to an element.
     * <pre>{@code
     * Text text = new Text(100, 100, "Hello World");
     * text.animate()
     *     .add(moveTo(200, 200), 3);
     * }</pre>
     */
    public static class AnimationBuilder {
        protected final Drawable drawable;

        public AnimationBuilder(Drawable drawable) {
            this.drawable = drawable;
        }

        /**
         * Schedule the execution of arbitrary code with a {@link paintingcanvas.painter.Event.EventRunner} at an absolute time.
         * <pre>{@code
         * // Print 'hi' after 10 seconds
         * AnimationBuilder.schedule(10, c -> System.out.println("hi"), TimeUnit.Seconds, false);
         * }</pre>
         *
         * @param time   The time from the start of the program to the start of the animation
         * @param runner The {@link paintingcanvas.painter.Event.EventRunner} object
         * @param unit   The {@link TimeUnit} used for {@code time}
         * @param repeat If the event should repeat
         */
        public static void schedule(float time, Event.EventRunner runner, TimeUnit unit, boolean repeat) {
            var _time = unit.asFrames(time);
            synchronized (painter.canvas.events) {
                painter.canvas.events.add(new Event(_time, repeat, runner));
            }
        }

        /**
         * Schedule the execution of arbitrary code with a {@link paintingcanvas.painter.Event.EventRunner} at an absolute time.
         * <pre>{@code
         * // Print 'hi' after 10 seconds
         * AnimationBuilder.schedule(10, c -> System.out.println("hi"), TimeUnit.Seconds, false);
         * }</pre>
         *
         * @param time   The time in seconds from the start of the program to the start of the animation
         * @param repeat If the event should repeat
         * @param runner The {@link paintingcanvas.painter.Event.EventRunner} object
         */
        public static void schedule(float time, boolean repeat, Event.EventRunner runner) {
            schedule(time, runner, TimeUnit.Seconds, repeat);
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
        public AnimationBuilder add(Animation animation, float duration, TimeUnit unit) {
            var _duration = unit.asFrames(duration);
            if (!firstBlockingAnimation) {
                animationFinish = painter.canvas.animations.stream().map(a -> a.startFrame + a.duration).max(Integer::compareTo).orElse(0);
                lastAnimationFinish = Math.max(animationFinish, builderFrame + _duration);
                _syncWait();
            }
            firstBlockingAnimation = false;

            // builderFrame should be at *least* right now
            if (builderFrame < painter.canvas.frame) builderFrame = painter.canvas.frame;
            var save = builderFrame;

            animation.drawable = this.drawable;
            animation.startFrame = builderFrame;
            animation.duration = _duration;

            lastBuilderFrame = builderFrame;
            builderFrame += _duration;
            synchronized (painter.canvas.animations) {
                painter.canvas.animations.add(animation);
            }

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
        public AnimationBuilder add(Animation animation, float duration) {
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
        public AnimationBuilder with(Animation animation, float duration, TimeUnit unit) {
            var _duration = unit.asFrames(duration);
            lastAnimationFinish = Math.max(lastAnimationFinish, lastBuilderFrame + _duration);

            // lastBuilderFrame should be *at least* right now
            if (lastBuilderFrame < painter.canvas.frame) lastBuilderFrame = painter.canvas.frame;

            animation.drawable = this.drawable;
            animation.startFrame = lastBuilderFrame;
            animation.duration = _duration;

            // builderFrame should be a count of when the last animation will end
            // and thus when to add the next one
            // as such if lastBuilderFrame + duration exceeds it, it has to be updated
            if (builderFrame < lastBuilderFrame + _duration) builderFrame = lastBuilderFrame + _duration;

            synchronized (painter.canvas.animations) {
                painter.canvas.animations.add(animation);
            }
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
        public AnimationBuilder with(Animation animation, float duration) {
            return with(animation, duration, TimeUnit.Seconds);
        }

        // needed to override the base object wait func

        /**
         * Used to add delays between animations.
         * <pre>{@code
         * // the second animation will run 50 frames after the first one finishes
         * obj.animate()
         *    .add(moveTo(100, 100), 100)
         *    .wait(50, TimeUnit.Frames)
         *    .add(colorTo(Color.BLUE), 100);
         *
         * // the second animation will run 50 frames after the first one starts
         * obj.animate()
         *    .add(moveTo(100, 100), 100)
         *    .wait(50, TimeUnit.Frames)
         *    .with(colorTo(Color.BLUE), 100);
         * }</pre>
         *
         * @param duration Time to wait
         * @param unit     The {@link TimeUnit} to use for the {@code duration}
         * @return <code>this</code> to allow method chaining
         */
        public AnimationBuilder wait(float duration, TimeUnit unit) {
            var _duration = unit.asFrames(duration);
            lastBuilderFrame += _duration;
            builderFrame += _duration;
            return this;
        }

        /**
         * Used to add delays between animations.
         * <pre>{@code
         * // the second animation will run 10 seconds after the first one finishes
         * obj.animate()
         *    .add(moveTo(100, 100), 100)
         *    .wait(10)
         *    .add(colorTo(Color.BLUE), 100);
         *
         * // the second animation will run 10 seconds after the first one starts
         * obj.animate()
         *    .add(moveTo(100, 100), 100)
         *    .wait(10)
         *    .with(colorTo(Color.BLUE), 100);
         * }</pre>
         *
         * @param duration Frames to wait
         * @return <code>this</code> to allow method chaining
         */
        public AnimationBuilder wait(float duration) {
            return wait(duration, TimeUnit.Seconds);
        }

        /**
         * Used to add delays between animations.
         * <pre>{@code
         * // the second animation will run 10 seconds after the first one finishes
         * obj.animate()
         *    .add(moveTo(100, 100), 100)
         *    .wait(10)
         *    .add(colorTo(Color.BLUE), 100);
         *
         * // the second animation will run 10 seconds after the first one starts
         * obj.animate()
         *    .add(moveTo(100, 100), 100)
         *    .wait(10)
         *    .with(colorTo(Color.BLUE), 100);
         * }</pre>
         *
         * @param duration Frames to wait
         * @return <code>this</code> to allow method chaining
         */
        public AnimationBuilder wait(int duration) {
            return wait(duration, TimeUnit.Seconds);
        }

        public AnimationBuilder sleep() {
            App.sleep();
            return this;
        }

        /**
         * Schedule an {@link Animation} at an absolute time.
         * <pre>{@code
         * // obj will start moving to (100, 100) 20 frames after the program starts
         * obj.animate()
         *    .schedule(20, moveTo(100, 100), 10, TimeUnit.Frames);
         * }</pre>
         *
         * @param time      The time from the start of the program to the start of the animation
         * @param animation The {@link Animation} to execute
         * @param duration  The length of the animation
         * @param unit      The {@link TimeUnit} used for {@code time} and {@code duration}
         * @return The original object to allow method chaining
         */
        public AnimationBuilder schedule(float time, Animation animation, float duration, TimeUnit unit) {
            var _time = unit.asFrames(time);
            var _duration = unit.asFrames(duration);

            animation.drawable = this.drawable;
            animation.startFrame = painter.canvas.frame + _time;
            animation.duration = _duration;
            synchronized (painter.canvas.animations) {
                painter.canvas.animations.add(animation);
            }
            return this;
        }

        /**
         * Schedule an {@link Animation} at an absolute time.
         * <pre>{@code
         * // obj will start moving to (100, 100) 10 seconds after the program starts
         * obj.animate()
         *    .schedule(10, moveTo(100, 100), 5);
         * }</pre>
         *
         * @param time      The time from the start of the program to the start of the animation. In seconds.
         * @param animation The {@link Animation} to execute
         * @param duration  The length of the animation. In seconds.
         * @return The original object to allow method chaining
         */
        public AnimationBuilder schedule(float time, Animation animation, float duration) {
            return schedule(time, animation, duration, TimeUnit.Seconds);
        }
    }
}
