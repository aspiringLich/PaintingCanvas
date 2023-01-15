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
    private static final Object syncObject = new Object();
    /**
     * The global Painter all Drawables access to add themselves to.
     */
    protected static Painter painter;
    private static Dimension lastSize;
    private static int builderFrame;
    private static int lastBuilderFrame;

    private static void _syncWait() {
        try {
            synchronized (syncObject) {
                syncObject.wait();
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

    /**
     * <code>[ADVANCED]</code>
     * An overwrite-able function to let you run code every frame.
     */
    public void render() {
    }

    // TODO: docs
    public abstract void setup() throws Exception;

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

    /**
     * Create a new animation to <code>color</code> in <a href="https://en.wikipedia.org/wiki/RGB_color_model">RGB</a>.
     * <pre>{@code
     *     Circle o = new Circle(100, 100, 20);
     *     o.animate().add(colorTo(255, 0, 0), 1);
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
     *     Circle o = new Circle(100, 100, 20);
     *     o.animate().add(colorTo(Color.RED), 1);
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
     *     Circle o = new Circle(100, 100, 20);
     *     // 0xFF0000 is hex for (255, 0, 0), which is red
     *     o.animate().add(colorTo(0xFF0000), 1);
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
     * If you supply an angle <code>> 360</code> it will make more than one full rotation.
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
    public void sleep(float time, TimeUnit unit) {
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
    public void sleep(float time) {
        sleep(time, TimeUnit.Seconds);
    }

    /**
     * A wrapper over a {@link Drawable} with a more verbose API
     *
     * @param <T> The class of the element extending {@link SimpleElement}
     */
    protected abstract static class SimpleElement<T extends SimpleElement<T>> {
        protected final Drawable _super;

        protected SimpleElement(Drawable drawable) {
            this._super = drawable;
            synchronized (painter.canvas.elements) {
                painter.canvas.elements.add(this._super);
            }
        }

        protected abstract T getThis();

        /**
         * Hide the Object.
         * <pre>{@code
         *     Circle o = new Circle(100, 100, 20);
         *     o.hide();
         * }</pre>
         *
         * @return The original object to allow method chaining
         * @see #show()
         */
        public T hide() {
            this._super.visible = false;
            return getThis();
        }

        /**
         * Show the Object
         * <pre>{@code
         *     Circle o = new Circle(100, 100, 20);
         *     o.show();
         * }</pre>
         *
         * @return The original object to allow method chaining
         * @see #hide()
         */
        public T show() {
            this._super.visible = true;
            return getThis();
        }

        /**
         * Get the X-position of the element
         *
         * @return the X-position of the element
         * @see #getY()
         * @see #setX(int)
         */
        public int getX() {
            return _super.x;
        }

        /**
         * Set the X-position of the object
         *
         * @param x the new X-position of the Object
         * @return The original object to allow method chaining
         * @see #getX()
         * @see #setY(int)
         */
        public T setX(int x) {
            _super.x = x;
            return getThis();
        }

        /**
         * Get the Y-position of the element.
         *
         * @return the Y-position of the object
         * @see #getY() ()
         * @see #setX(int)
         */
        public int getY() {
            return _super.y;
        }

        /**
         * Set the Y-position of the element
         *
         * @return The original object to allow method chaining
         * @see #setX(int)
         * @see #getY()
         */
        public T setY(int y) {
            _super.y = y;
            return getThis();
        }

        /**
         * Set the color of the element with <a href="https://en.wikipedia.org/wiki/RGB_color_model">RGB</a>.
         * <pre>{@code
         *     Circle o = new Circle(100, 100, 20);
         *     o.setColor(255, 0, 0); // Set color to red
         * }</pre>
         *
         * @param r The red component of the color (0-255)
         * @param g The green component of the color (0-255)
         * @param b The blue component of the color (0-255)
         * @return The original object to allow method chaining
         */
        public T setColor(int r, int g, int b) {
            this._super.color = new Color(r, g, b);
            return getThis();
        }

        /**
         * Get the current color of an element as a {@link Color}
         *
         * @return The {@link Color} of the element
         */
        public Color getColor() {
            return this._super.color;
        }

        /**
         * Set the color of the object with a {@link Color} object.
         * <pre>{@code
         *     Circle o = new Circle(100, 100, 20);
         *     o.setColor(Color.RED); // Set color to red
         * }</pre>
         *
         * @param color color.
         * @return The original object to allow method chaining
         */
        public T setColor(Color color) {
            this._super.color = color;
            return getThis();
        }

        /**
         * Set the color of the object with a <a href="https://en.wikipedia.org/wiki/RGB_color_model#Numeric_representations">8-bit RGB hex literal</a>.
         * <pre>{@code
         *     Circle o = new Circle(100, 100, 20);
         *     // 0xFF0000 is hex for (255, 0, 0), which is red
         *     o.setColor(0xFF0000);
         * }</pre>
         *
         * @param hex The color as a hex literal
         * @return The original object to allow method chaining
         */
        public T setColor(int hex) {
            return setColor(hex >> 16 & 0xff, hex >> 8 & 0xff, hex & 0xff);
        }

        /**
         * Rotate this element by <code>rotation°</code>.
         * <pre>{@code
         *     Circle o = new Circle(100, 100, 20);
         *
         *     // Calling rotate(90) twice makes the object rotate 180°
         *     o.rotate(90);
         *     o.rotate(90);
         * }</pre>
         *
         * @param rotation Change in rotation. (Degrees)
         * @return The original object to allow method chaining
         * @see #rotateTo(double)
         */
        public T rotate(double rotation) {
            this._super.rotation += Math.toRadians(rotation);
            return getThis();
        }

        /**
         * Set an elements rotation to <code>rotation°</code>.
         * <pre>{@code
         *     Circle o = new Circle(100, 100, 20);
         *     o.rotate(90); // Sets the elements rotation to 90°
         * }</pre>
         *
         * @param rotation Absolute rotation. (Degrees)
         * @return The original object to allow method chaining
         * @see #rotate(double)
         */
        public T rotateTo(double rotation) {
            this._super.rotation = rotation;
            return getThis();
        }

        /**
         * Create an {@link AnimationBuilder}.
         * Used to animate different propertys of an element (position, rotation, color).
         * <pre>{@code
         *     Circle o = new Circle(100, 100, 20);
         *
         *     // Animate Circle o to move to 200, 200 for 3 seconds
         *     o.animate().add(moveTo(200, 200), 3);
         * }</pre>
         *
         * @return {@link AnimationBuilder}
         */
        public AnimationBuilder animate() {
            return new AnimationBuilder(_super);
        }
    }

    /**
     * A Text element, used for drawing text on the canvas.
     * <pre>{@code
     *     Text text = new Text(100, 100, "Hello World");
     * }</pre>
     */
    protected static class Text extends SimpleElement<Text> {
        /**
         * Create a new Text element.
         * The default font size is 30, and the default font is <u>comic sans</u> <em>(not sorry)</em>.
         * <pre>{@code
         *     Text text = new Text(100, 100, "Hello World");
         * }</pre>
         *
         * @param x    The X-position of the text
         * @param y    The Y-position of the text
         * @param text The text
         */
        public Text(int x, int y, String text) {
            super(new paintingcanvas.painter.drawable.Text(x, y, text, 30));
        }

        /**
         * Sets the size of the text in <a href="https://en.wikipedia.org/wiki/Point_(typography)">points</a>.
         * <pre>{@code
         *     Text text = new Text(100, 100, "Hello World");
         *     text.setFontSize(40); // Set font size to 40 points
         * }</pre>
         *
         * @param size Font size in points
         * @return The original object to allow method chaining
         */
        public Text setFontSize(float size) {
            ((paintingcanvas.painter.drawable.Text) this._super).setSize(size);
            return this;
        }

        @Override
        protected Text getThis() {
            return this;
        }

        /**
         * Gets the current text of the element.
         * <pre>{@code
         *     Text text = new Text(100, 100, "Hello World");
         *     System.out.println(text.getText()); // => Hello World
         * }</pre>
         *
         * @return The text as a {@link String}
         * @see #setText(String)
         */
        public String getText() {
            return ((paintingcanvas.painter.drawable.Text) this._super).text;
        }

        /**
         * Sets the text of the element.
         * <pre>{@code
         *     Text text = new Text(100, 100, "Hello World");
         *     text.setText("Go Go Mango");
         * }</pre>
         *
         * @param text The new text for the element as a {@link String}
         * @return The original object to allow method chaining
         */
        public Text setText(String text) {
            ((paintingcanvas.painter.drawable.Text) this._super).text = text;
            return this;
        }
    }

    /**
     * An <a href="https://en.wikipedia.org/wiki/Ellipse">ellipse</a> element.
     * <pre>{@code
     *     // Create a new ellipse at (100, 100) with width 20 and height 30
     *     Ellipse ellipse = new Ellipse(100, 100, 20, 30);
     * }</pre>
     */
    protected static class Ellipse extends SimpleElement<Ellipse> {
        /**
         * Create a new Ellipse element.
         * <pre>{@code
         *     // Create a new ellipse at (100, 100) with width 20 and height 30
         *     Ellipse ellipse = new Ellipse(100, 100, 20, 30);
         * }</pre>
         *
         * @param x The X-position of the ellipse
         * @param y The Y-position of the ellipse
         * @param w The width of the ellipse
         * @param h The height of the ellipse
         */
        public Ellipse(int x, int y, int w, int h) {
            super(new paintingcanvas.painter.drawable.Ellipse(x, y, w, h));
        }

        @Override
        protected Ellipse getThis() {
            return this;
        }
    }

    /**
     * A Circle element.
     * <pre>{@code
     *     // Create a new Circle at (100, 100) with a radius of 20.
     *     Circle circle = new Circle(100, 100, 20);
     * }</pre>
     */
    protected static class Circle extends SimpleElement<Circle> {
        /**
         * Create a new Circle element.
         * <pre>{@code
         *     // Create a new Circle at (100, 100) with a radius of 20.
         *     Circle circle = new Circle(100, 100, 20);
         * }</pre>
         *
         * @param x The X-position of the circle
         * @param y The Y-position of the circle
         * @param r The radius of the circle
         */
        public Circle(int x, int y, int r) {
            super(new paintingcanvas.painter.drawable.Ellipse(x, y, r, r));
        }

        @Override
        protected Circle getThis() {
            return this;
        }
    }

    /**
     * A Rectangle element.
     * <pre>{@code
     *     // Create a new Rectangle at (100, 100) with a width of 20 and a height of 30
     *     Rectangle rectangle = new Rectangle(100, 100, 20, 30);
     * }</pre>
     */
    protected static class Rectangle extends SimpleElement<Rectangle> {
        /**
         * Create a new Rectangle element.
         * <pre>{@code
         *     // Create a new Rectangle at (100, 100) with a width of 20 and a height of 30
         *     Rectangle rectangle = new Rectangle(100, 100, 20, 30);
         * }</pre>
         *
         * @param x The X-position of the rectangle
         * @param y The Y-position of the rectangle
         * @param w The width of the rectangle
         * @param h The height of the rectangle
         */
        public Rectangle(int x, int y, int w, int h) {
            super(new paintingcanvas.painter.drawable.Rectangle(x, y, w, h));
        }

        @Override
        protected Rectangle getThis() {
            return this;
        }
    }

    /**
     * A Square element.
     * <pre>{@code
     *     // Create a new Square at (100, 100) with a size of 30px
     *     Square square = new Square(100, 100, 30);
     * }</pre>
     */
    protected static class Square extends SimpleElement<Square> {
        /**
         * Create a new Square element.
         * <pre>{@code
         *     Square square = new Square(100, 100, 30);
         * }</pre>
         *
         * @param x The X-position of the square
         * @param y The Y-position of the square
         * @param s The size of the square
         */
        public Square(int x, int y, int s) {
            super(new paintingcanvas.painter.drawable.Rectangle(x, y, s, s));
        }

        @Override
        public Square getThis() {
            return this;
        }
    }

    /**
     * A Polygon Element.
     * <pre>{@code
     *     Polygon polygon = new Polygon(100, 100,
     *          new int[] {0, 25, 50},
     *          new int[] {50, 25, 0},
     *     );
     * }</pre>
     */
    protected static class Polygon extends SimpleElement<Polygon> {
        /**
         * Create a new Polygon element from a {@link java.awt.Polygon}.
         * <pre>{@code
         *     Polygon polygon = new Polygon(100, 100, new java.awt.Polygon(
         *         new int[] {0, 25, 50},
         *         new int[] {50, 25, 0},
         *         3
         *      ));
         * }</pre>
         *
         * @param x       The X-position of the polygon
         * @param y       The Y-position of the polygon
         * @param polygon The {@link java.awt.Polygon} element
         */
        public Polygon(int x, int y, java.awt.Polygon polygon) {
            super(new paintingcanvas.painter.drawable.Polygon(x, y, polygon));
        }

        /**
         * Create a new Polygon element from a list of x-points and y-points.
         * <pre>{@code
         *     Polygon polygon = new Polygon(100, 100,
         *          new int[] {0, 25, 50},
         *          new int[] {50, 25, 0},
         *     );
         * }</pre>
         *
         * @param x       The X-position of the polygon
         * @param y       The Y-position of the polygon
         * @param xPoints List of X-points
         * @param yPoints List of Y-points
         */
        public Polygon(int x, int y, int[] xPoints, int[] yPoints) {
            super(new paintingcanvas.painter.drawable.Polygon(x, y, xPoints, yPoints));
        }

        @Override
        public Polygon getThis() {
            return this;
        }
    }

    /**
     * A Triangle element.
     * <pre>{@code
     *     // Create a new Triangle at (100, 100) that is 20px wide and 30px tall
     *     Triangle triangle = new Triangle(100, 100, 20, 30);
     * }</pre>
     */
    protected static class Triangle extends SimpleElement<Triangle> {
        /**
         * Create a new Triangle element.
         * <pre>{@code
         *     // Create a new Triangle at (100, 100) that is 20px wide and 30px tall
         *     Triangle triangle = new Triangle(100, 100, 20, 30);
         * }</pre>
         *
         * @param x The X-position of the triangle
         * @param y The Y-position of the triangle
         * @param w The width of the triangle
         * @param h The height of the triangle
         */
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
        protected Triangle getThis() {
            return this;
        }
    }

    /**
     * A builder used to add animations to an element.
     * <pre>{@code
     *     Text text = new Text(100, 100, "Hello World");
     *     text.animate()
     *         .add(moveTo(200, 200), 3);
     * }</pre>
     */
    protected static class AnimationBuilder {
        protected final Drawable drawable;

        protected AnimationBuilder(Drawable drawable) {
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
         * @param repeat If the event should repeat
         * @param unit   The {@link TimeUnit} used for {@code time}
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
         * @param runner The {@link paintingcanvas.painter.Event.EventRunner} object
         * @param repeat If the event should repeat
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

            animation.drawable = this.drawable;
            animation.startFrame = builderFrame;
            animation.duration = _duration;

            lastBuilderFrame = builderFrame;
            builderFrame += _duration;
            synchronized (painter.canvas.animations) {
                painter.canvas.animations.add(animation);
            }

            synchronized (painter.canvas.events) {
                painter.canvas.events.add(new Event(builderFrame, c -> {
                    synchronized (syncObject) {
                        syncObject.notify();
                    }
                }));
            }
            _syncWait();
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
         * // TODO: Verify that wait + with has the following behavior
         * // the second animation will run 50 frames after the first one starts
         * obj.animate()
         *    .add(moveTo(100, 100), 100)
         *    .wait(50, TimeUnit.Frames)
         *    .with(colorTo(Color.BLUE), 100);
         * }</pre>
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

        /**
         * Used to add delays between animations.
         * <pre>{@code
         * // the second animation will run 10 seconds after the first one finishes
         * obj.animate()
         *    .add(moveTo(100, 100), 100)
         *    .wait(10)
         *    .add(colorTo(Color.BLUE), 100);
         *
         * // TODO: Verify that wait + with has the following behavior
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
         * // TODO: Verify that wait + with has the following behavior
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

        /**
         * Schedule an {@link Animation} at an absolute time.
         * <pre>{@code
         * // obj will start moving to (100, 100) 20 frames after the program states
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
            animation.startFrame = builderFrame + _time;
            animation.duration = _duration;
            synchronized (painter.canvas.animations) {
                painter.canvas.animations.add(animation);
            }
            return this;
        }

        /**
         * Schedule an {@link Animation} at an absolute time.
         * <pre>{@code
         * // obj will start moving to (100, 100) 10 seconds after the program states
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
