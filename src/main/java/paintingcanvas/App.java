package paintingcanvas;

import paintingcanvas.animation.*;
import paintingcanvas.drawable.Drawable;
import paintingcanvas.misc.TimeUnit;

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
     * Initialize and run the application with default width height and title parameters
     */
    public void run() {
        run(1000, 600, "Canvas");
    }

    /**
     * Initialize and run the application.
     */
    public void run(int width, int height, String title) {
        // Worth a try
        System.setProperty("sun.java2d.opengl", "true");

        // Init global painter
        painter = new Painter(width, height, title);

        painter.canvas.renderLifecycle = new paintingcanvas.Canvas.RenderLifecycle() {
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
     * Waits for the specified time.
     *
     * @param time Time to wait
     * @param unit The unit that <code>time</code> is in
     */
    @SuppressWarnings("SameParameterValue")
    protected void sleep(double time, TimeUnit unit) {
        builderFrame += unit.asFrames(time);
        lastBuilderFrame = builderFrame;

        // Schedule unblocking thread
        synchronized (painter.canvas.events) {
            painter.canvas.events.add(new paintingcanvas.Event(builderFrame, c -> {
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
    protected void sleep(double time) {
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
    public static class AnimationBuilder implements Animatable {
        protected final Drawable drawable;

        public AnimationBuilder(Drawable drawable) {
            this.drawable = drawable;
        }

        /**
         * Schedule the execution of arbitrary code with a {@link paintingcanvas.Event.EventRunner} at an absolute time.
         * <pre>{@code
         * // Print 'hi' after 10 seconds
         * AnimationBuilder.schedule(10, c -> System.out.println("hi"), TimeUnit.Seconds, false);
         * }</pre>
         *
         * @param time   The time from the start of the program to the start of the animation
         * @param runner The {@link paintingcanvas.Event.EventRunner} object
         * @param unit   The {@link TimeUnit} used for {@code time}
         * @param repeat If the event should repeat
         */
        public static void schedule(double time, paintingcanvas.Event.EventRunner runner, TimeUnit unit, boolean repeat) {
            var _time = unit.asFrames(time);
            synchronized (painter.canvas.events) {
                painter.canvas.events.add(new paintingcanvas.Event(_time, repeat, runner));
            }
        }

        /**
         * Schedule the execution of arbitrary code with a {@link paintingcanvas.Event.EventRunner} at an absolute time.
         * <pre>{@code
         * // Print 'hi' after 10 seconds
         * AnimationBuilder.schedule(10, c -> System.out.println("hi"), TimeUnit.Seconds, false);
         * }</pre>
         *
         * @param time   The time in seconds from the start of the program to the start of the animation
         * @param repeat If the event should repeat
         * @param runner The {@link paintingcanvas.Event.EventRunner} object
         */
        public static void schedule(double time, boolean repeat, Event.EventRunner runner) {
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
        public AnimationBuilder add(Animation animation, double duration, TimeUnit unit) {
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
        public AnimationBuilder with(Animation animation, double duration) {
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
        public AnimationBuilder wait(double duration, TimeUnit unit) {
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
        public AnimationBuilder wait(double duration) {
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
        public AnimationBuilder schedule(double time, Animation animation, double duration, TimeUnit unit) {
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
        public AnimationBuilder schedule(double time, Animation animation, double duration) {
            return schedule(time, animation, duration, TimeUnit.Seconds);
        }

        @Override
        public AnimationBuilder animate() {
            return this;
        }

        @Override
        public Drawable drawable() {
            return this.drawable;
        }

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
         */
        public App.AnimationBuilder moveTo(int x, int y, double duration) {
            return this.add(new MovementAnimation(0, 0, new Point(x, y), this.drawable), duration);
        }
    }
}
