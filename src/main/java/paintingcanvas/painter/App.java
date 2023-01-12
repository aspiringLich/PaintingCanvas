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
 * an abstract class to allow interfacing with the painter library whilst keeping the painter library distinct
 */
public abstract class App {
    /**
     * The global Painter all Drawables access to add themselves to.
     */
    protected static Painter painter;
    protected static Dimension lastSize;
    private static int builderFrame;
    private static int lastBuilderFrame;
    protected int width;
    protected int height;

    public void render() {
    }

    public abstract void setup() throws Exception;

    /**
     * Initialize and run the application
     */
    public void run() {
        // Worth a try
        System.setProperty("sun.java2d.opengl", "true");

        // Init global painter
        this.width = 1000;
        this.height = 600;
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
        try {
            this.setup();
        } catch (Exception e) {
            e.printStackTrace();
        }
        painter.render(this);
    }

    // == Define animations ==

    /**
     * Makes an animation that sets the color of the object with rgb
     *
     * <pre>
     * // this should animate the square red
     * square.animate().add(colorTo(255, 0, 0), 100);
     * </pre>
     *
     * @param r red (0 - 255)
     * @param g green (0 - 255)
     * @param b blue (0 - 255)
     * @return An animation
     */
    protected Animation colorTo(int r, int g, int b) {
        return colorTo(new Color(r, g, b));
    }

    protected Animation colorTo(Color color) {
        return new ColorAnimation(builderFrame, color, 0, null);
    }

    protected Animation moveTo(int x, int y) {
        return new MovementAnimation(builderFrame, 0, new Point(x, y), null);
    }

    protected Animation rotateTo(int angle) {
        return new RotationAnimation(builderFrame, 0, Math.toRadians(angle), null);
    }

    public void sleep(float time, TimeUnit unit) {
        builderFrame += unit.asFrames(time);
        lastBuilderFrame = builderFrame;
    }

    public void sleep(float time) {
        sleep(time, TimeUnit.Seconds);
    }

    protected abstract static class SimpleElement<T extends SimpleElement<T>> {
        Drawable _super;

        public SimpleElement(Drawable drawable) {
            this._super = drawable;
            synchronized (painter.canvas.elements) {
                painter.canvas.elements.add(this._super);
            }
        }

        public abstract T getThis();

        /**
         * Hide the Object
         *
         * @return The original object to allow method chaining
         */
        public T hide() {
            this._super.visible = false;
            return getThis();
        }

        /**
         * Show the Object
         *
         * @return The original object to allow method chaining
         */
        public T show() {
            this._super.visible = true;
            return getThis();
        }

        /**
         * @return the X-position of the Object
         */
        public int getX() {
            return _super.x;
        }

        /**
         * Set the X-position of the object
         *
         * @param x the new X-position of the Object
         * @return The original object to allow method chaining
         */
        public T setX(int x) {
            _super.x = x;
            return getThis();
        }

        /**
         * @return the Y-position of the object
         */
        public int getY() {
            return _super.y;
        }

        /**
         * Set the Y-position of the object
         *
         * @param y the new Y-position of the Object
         * @return The original object to allow method chaining
         */
        public T setY(int y) {
            _super.y = y;
            return getThis();
        }

        /**
         * Set the color of the object with rgb
         *
         * @param r red (0 - 255)
         * @param g green (0 - 255)
         * @param b blue (0 - 255)
         * @return The original object to allow method chaining
         */
        public T setColor(int r, int g, int b) {
            this._super.color = new Color(r, g, b);
            return getThis();
        }

        public Color getColor() {
            return this._super.color;
        }

        /**
         * Set the color of the object with a Color object
         *
         * @param color color.
         * @return The original object to allow method chaining
         */
        public T setColor(Color color) {
            this._super.color = color;
            return getThis();
        }

        /**
         * Set the color of the object with a hex code
         *
         * @param hex the hex code for the color
         * @return The original object to allow method chaining
         */
        public T setColor(int hex) {
            return setColor(hex >> 16 & 0xff, hex >> 8 & 0xff, hex & 0xff);
        }

        /**
         * Rotate this object dRotation degrees
         *
         * @param dRotation change in rotation.
         * @return The original object to allow method chaining
         */
        public T rotate(double dRotation) {
            this._super.rotation += dRotation;
            return getThis();
        }

        /**
         * Rotate this object to rotation degrees
         *
         * @param rotation rotation to rotate to
         * @return The original object to allow method chaining
         */
        public T rotateTo(double rotation) {
            this._super.rotation = rotation;
            return getThis();
        }

        public AnimationBuilder animate() {
            return new AnimationBuilder(_super);
        }
    }

    protected static class Text extends SimpleElement<Text> {
        public Text(int x, int y, String text) {
            super(new paintingcanvas.painter.drawable.Text(x, y, text, 30));
        }

        public Text setFontSize(float size) {
            var text = (paintingcanvas.painter.drawable.Text) this._super;
            text.setSize(size);
            return this;
        }

        @Override
        public Text getThis() {
            return this;
        }

        public String getText() {
            return ((paintingcanvas.painter.drawable.Text) this._super).text;
        }

        public Text setText(String text) {
            ((paintingcanvas.painter.drawable.Text) this._super).text = text;
            return this;
        }
    }

    protected static class Ellipse extends SimpleElement<Ellipse> {
        public Ellipse(int x, int y, int w, int h) {
            super(new paintingcanvas.painter.drawable.Ellipse(x, y, w, h));
        }

        @Override
        public Ellipse getThis() {
            return this;
        }
    }

    protected static class Circle extends SimpleElement<Circle> {
        public Circle(int x, int y, int r) {
            super(new paintingcanvas.painter.drawable.Ellipse(x, y, r, r));
        }

        @Override
        public Circle getThis() {
            return this;
        }
    }

    protected static class Rectangle extends SimpleElement<Rectangle> {
        public Rectangle(int x, int y, int w, int h) {
            super(new paintingcanvas.painter.drawable.Rectangle(x, y, w, h));
        }

        @Override
        public Rectangle getThis() {
            return this;
        }
    }

    protected static class Square extends SimpleElement<Square> {
        public Square(int x, int y, int s) {
            super(new paintingcanvas.painter.drawable.Rectangle(x, y, s, s));
        }

        @Override
        public Square getThis() {
            return this;
        }
    }

    protected static class Polygon extends SimpleElement<Polygon> {
        public Polygon(int x, int y, java.awt.Polygon polygon) {
            super(new paintingcanvas.painter.drawable.Polygon(x, y, polygon));
        }

        public Polygon(int x, int y, int[] xPoints, int[] yPoints) {
            super(new paintingcanvas.painter.drawable.Polygon(x, y, xPoints, yPoints));
        }

        @Override
        public Polygon getThis() {
            return this;
        }
    }

    protected static class Triangle extends SimpleElement<Triangle> {
        public Triangle(int x, int y, int w, int h) {
            super(new paintingcanvas.painter.drawable.Polygon(x, y, new int[]{
                    0,
                    w / 2,
                    w
            }, new int[]{
                    h,
                    0,
                    h
            }));
        }

        @Override
        public Triangle getThis() {
            return this;
        }
    }

    /**
     * This class provides various methods to construct and schedule animations.
     * You can create an instance of the class by simply:
     *
     * <pre>
     * obj.animate();
     * </pre>
     * Then you are free to call any of the below methods on it to start adding animations. These methods include
     * <code>.add()</code>, <code>.with()</code>, and <code>.schedule()</code>.
     * <br>
     * You should use <code>.add()</code> and <code>.with()</code> for simpler animations. <code>.add()</code>
     * waits until all animations finish before running, and <code>.with()</code> runs the animation right now,
     * with any previous <code>.add()</code> animations, which allows you to run multiple animations at the same time.
     * That's probably confusing so see this piece of code down here:
     *
     * <pre>
     * // these three animations will happen all at the same time
     * square.animate()
     *       .add(moveTo(100, 100), 100)
     *       .with(colorTo(Color.RED), 100);
     * circle.animate()
     *       .with(colorTo(color.BLUE), 100);
     *
     * // this animation will happen after all the previous animations finish
     * text.animate.add(rotateTo(360), 100);
     * </pre>
     *
     * as a shorthand, instead of <code>.animate().add()</code> you can instead just use <code>.animate()</code>
     *
     * <pre>
     * square.animate(colorTo(Color.RED), 100);
     * </pre>
     *
     * Since <code>.add()</code> and <code>.with()</code> are blocking, you can use <code>.schedule()</code>
     * to manually specify when animations run for more complicated animations.
     *
     * <pre>
     *
     * </pre>
     */
    protected static class AnimationBuilder {
        public final Drawable drawable;
//        public int startFrame;
//        public int prevFrame;
//        public int frame;

        public AnimationBuilder(Drawable drawable) {
            this.drawable = drawable;
        }

        /**
         * Add an animation to an object. If there are any currently running animations it waits until
         * those finish before running. This ensures that whenever you call <code>.add()</code>, this animation
         * will run after any <code>.add()</code>'s or <code>.with()</code>'s before it.
         * <pre>
         * // these animations will run one after the other
         * obj.animate()
         *    .add(moveTo(100, 100), 100)
         *    .add(colorTo(Color.BLUE), 100);
         * </pre>
         *
         * @param animation The animation type to add
         * @param duration  The amount of time the animation will last
         * @return <code>this</code> to allow method chaining
         */
        public AnimationBuilder add(Animation animation, float duration, TimeUnit unit) {
            // waitUntilAnimationsDone();

            var _duration = unit.asFrames(duration);

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

        // TODO: Update Docs
        public AnimationBuilder add(Animation animation, float duration) {
            return add(animation, duration, TimeUnit.Seconds);
        }

        // TODO: Update Docs

        /**
         * Add the animation alongside / with the previous animation.
         * <pre>
         * // these animations will run at the same time
         * obj.animate()
         *    .add(moveTo(100, 100), 100)
         *    .with(colorTo(Color.BLUE), 100);
         * </pre>
         *
         * @param animation The animation type to add
         * @param duration  The amount of time the animation will last
         * @return <code>this</code> to allow method chaining
         */
        public AnimationBuilder with(Animation animation, float duration, TimeUnit unit) {
            var _duration = unit.asFrames(duration);

            animation.drawable = this.drawable;
            animation.startFrame = lastBuilderFrame;
            animation.duration = _duration;

///           builderFrame +=
//            builderFrame += _duration;
            synchronized (painter.canvas.animations) {
                painter.canvas.animations.add(animation);
            }
            return this;
        }

        // TODO: Update Docs
        public AnimationBuilder with(Animation animation, float duration) {
            return with(animation, duration, TimeUnit.Seconds);
        }

        // TODO: Update Docs

        /**
         * Adds the next animation <code>frame</code> frames after it normally would.
         * <pre>
         * // the second animation will run 50 frames after the first one finishes
         * obj.animate()
         *    .add(moveTo(100, 100), 100)
         *    .wait(50)
         *    .add(colorTo(Color.BLUE), 100);
         *
         * // the second animation will run 50 frames after the first one starts
         * obj.animate()
         *    .add(moveTo(100, 100), 100)
         *    .wait(50)
         *    .with(colorTo(Color.BLUE), 100);
         * </pre>
         *
         * @param duration Frames to wait
         * @return <code>this</code> to allow method chaining
         */
        public AnimationBuilder wait(float duration, TimeUnit unit) {
            var _duration = unit.asFrames(duration);
            lastBuilderFrame = builderFrame;
            builderFrame += _duration;
            return this;
        }

        // TODO: Update Docs
        public AnimationBuilder wait(float duration) {
            return wait(duration, TimeUnit.Seconds);
        }

        // TODO: Update Docs
        // needed to override the base object wait func
        public AnimationBuilder wait(int duration) {
            return wait(duration, TimeUnit.Seconds);
        }

        // TODO: Update Docs

        /**
         * Add the animation at the end of the animation queue. If there's no previous animations it adds the animation now.
         * <pre>
         * // this animation will run 50 frames from now and
         * // last for 100 frames, ending 150 frames from now
         * obj.animate()
         *    .schedule(50, moveTo(100, 100), 100)
         * </pre>
         *
         * @param time      frames, after now, to add the animation
         * @param animation The animation type to add
         * @param duration  The amount of time the animation will last
         * @return <code>this</code> to allow method chaining
         */
        public AnimationBuilder schedule(float time, Animation animation, float duration, TimeUnit unit) {
            var _time = unit.asFrames(time);
            var _duration = unit.asFrames(duration);

            animation.drawable = this.drawable;
            animation.startFrame = builderFrame + _time;
            animation.duration = _duration;
            synchronized (painter.canvas.animations) {
                painter.canvas.animations.add(animation);
            }
            return this;
        }

        // TODO: Update Docs
        public AnimationBuilder schedule(float time, Animation animation, float duration) {
            return schedule(time, animation, duration, TimeUnit.Seconds);
        }

        // TODO: Update Docs
        public AnimationBuilder schedule(float time, Event.EventRunner runner, TimeUnit unit, boolean repeat) {
            var _time = unit.asFrames(time);
            synchronized (painter.canvas.events) {
                painter.canvas.events.add(new Event(_time, repeat, runner));
            }

            return this;
        }

        // TODO: Update Docs
        public AnimationBuilder schedule(float time, boolean repeat, Event.EventRunner runner) {
            return schedule(time, runner, TimeUnit.Seconds, repeat);
        }
    }
}
