package paintingcanvas;

import paintingcanvas.animation.*;
import paintingcanvas.drawable.Drawable;
import paintingcanvas.misc.TimeUnit;

import java.awt.*;
import java.awt.event.ComponentEvent;

/**
 * <p>
 *     Hi person leafing through the documentation! This class is only really
 *     still here because i was too lazy to try and integrate
 *     everything with {@link Canvas.CanvasComponent}. You can use this
 *     to make your animations and stuff, but you should really use the Canvas class
 * </p><p>
 *     You know, actually, the canvas object only really exists to pretend that theres some object youre calling this stuff on when
 *     in reality its all static under the hood.
 * </p><p>
 *     So thats why it breaks if you make two canvases.
 * </p><p>
 *     Or does it? IDK i haven't tested it.
 * </p>
 */
@SuppressWarnings("unused")
public class App {
    // Used to block on .add() calls
    protected static final Object syncObject = new Object();
    // Used to wait on sleepUntillAnimationEnds
    protected static final Object userSyncObject = new Object();
    /**
     * The global Painter all Drawables access to add themselves to.
     */
    public static Canvas.CanvasComponent canvas;
    // The frame the builder is on (end of the last animation)
    protected static int builderFrame;
    // The start of the last animation
    protected static int lastBuilderFrame;
    // Used to make sure the first call to .add doesn't block
    protected static boolean firstBlockingAnimation = true;
    // The frame the last animation ends on (excluding the last blocking animation)
    protected static int animationFinish = -1;
    // The frame the last animation ends on (including the last animation)
    protected static int lastAnimationFinish = -1;
    // The last size of the canvas, used for auto-centering of drawables
    protected static Dimension lastSize;


    static void _syncWait() {
        try {
            synchronized (syncObject) {
                syncObject.wait();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected static void _render() {
        if (animationFinish != -1 && canvas.frame >= animationFinish) {
            animationFinish = -1;
            synchronized (syncObject) {
                syncObject.notify();
            }
        }

        if (lastAnimationFinish != -1 && canvas.frame >= lastAnimationFinish) {
            lastAnimationFinish = -1;
            synchronized (userSyncObject) {
                userSyncObject.notify();
            }
        }
    }

    /**
     * An overwrite-able function to let you run code every frame.
     */
    public void render() {
    }

    /**
     * Initialize and run the application with default width height and title parameters
     */
//    public static void run(Canvas.CanvasComponent canvas) {
//        run(canvas, 1000, 600, "Canvas");
//    }

    /**
     * Initialize and run the application.
     */
    public static void run(Canvas.CanvasComponent c, int width, int height, String title) {
        // Worth a try
//        System.setProperty("sun.java2d.opengl", "true");

        // Init global painter
        canvas = c;
        canvas.renderLifecycle = new Canvas.CanvasComponent.RenderLifecycle() {
            @Override
            public void onResize(Canvas.CanvasComponent canvas, ComponentEvent e) {
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
        canvas.render();
//        try {
//            this.setup();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    /**
     * Adds the element to the canvas
     * @param drawable element to be added
     */
    public static void addElement(Drawable<?> drawable) {
            canvas.elements.add(drawable);
    }

    /**
     * Sleeps until all the animations are finished
     */
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
     * Waits for the specified time.
     *
     * @param time Time to wait
     * @param unit The unit that <code>time</code> is in
     */
    @SuppressWarnings("SameParameterValue")
    protected static void sleep(double time, paintingcanvas.misc.TimeUnit unit) {
        builderFrame += unit.asFrames(time);
        lastBuilderFrame = builderFrame;

        // Schedule unblocking thread
        synchronized (canvas.events) {
            canvas.events.add(new paintingcanvas.Event(builderFrame, c -> {
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
    protected static void sleep(double time) {
        sleep(time, paintingcanvas.misc.TimeUnit.Seconds);
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
        protected Drawable<?> drawable;

        public AnimationBuilder(Drawable<?> drawable) {
            this.drawable = drawable;
        }

        /**
         * Schedule the execution of arbitrary code with a {@link Event.EventRunner} at an absolute time.
         * <pre>{@code
         * // Print 'hi' after 10 seconds
         * AnimationBuilder.schedule(10, c -> System.out.println("hi"), TimeUnit.Seconds, false);
         * }</pre>
         *
         * @param time   The time from the start of the program to the start of the animation
         * @param runner The {@link Event.EventRunner} object
         * @param unit   The {@link TimeUnit} used for {@code time}
         * @param repeat If the event should repeat
         */
        public static void schedule(double time, Event.EventRunner runner, TimeUnit unit, boolean repeat) {
            var _time = unit.asFrames(time);
            canvas.events.add(new Event(_time, repeat, runner));
        }

        /**
         * Schedule the execution of arbitrary code with a {@link Event.EventRunner} at an absolute time.
         * <pre>{@code
         * // Print 'hi' after 10 seconds
         * AnimationBuilder.schedule(10, c -> System.out.println("hi"), TimeUnit.Seconds, false);
         * }</pre>
         *
         * @param time   The time in seconds from the start of the program to the start of the animation
         * @param repeat If the event should repeat
         * @param runner The {@link Event.EventRunner} object
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
                animationFinish = canvas.animations.stream().map(a -> a.startFrame + a.duration).max(Integer::compareTo).orElse(0);
                lastAnimationFinish = Math.max(animationFinish, builderFrame + _duration);
                _syncWait();
            }
            firstBlockingAnimation = false;

            // builderFrame should be at *least* right now
            if (builderFrame < canvas.frame) builderFrame = canvas.frame;
            var save = builderFrame;

            animation.init(drawable);
            animation.startFrame = builderFrame;
            animation.duration = _duration;

            lastBuilderFrame = builderFrame;
            builderFrame += _duration;
            synchronized (canvas.animations) {
                canvas.animations.add(animation);
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
            if (lastBuilderFrame < canvas.frame) lastBuilderFrame = canvas.frame;

            animation.init(drawable);
            animation.startFrame = lastBuilderFrame;
            animation.duration = _duration;

            // builderFrame should be a count of when the last animation will end
            // and thus when to add the next one
            // as such if lastBuilderFrame + duration exceeds it, it has to be updated
            if (builderFrame < lastBuilderFrame + _duration) builderFrame = lastBuilderFrame + _duration;

            synchronized (canvas.animations) {
                canvas.animations.add(animation);
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

            animation.init(drawable);
            animation.startFrame = canvas.frame + _time;
            animation.duration = _duration;
            synchronized (canvas.animations) {
                canvas.animations.add(animation);
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
        public Drawable<?> drawable() {
            return this.drawable;
        }
    }
}
