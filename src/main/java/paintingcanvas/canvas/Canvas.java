package paintingcanvas.canvas;

import paintingcanvas.animation.Animation;
import paintingcanvas.drawable.Drawable;
import paintingcanvas.misc.ElementContainer;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * The internal canvas component that is used to draw to the screen
 */
public class Canvas {
    /**
     * The FPS of the canvas
     */
    public static final int fps = 30;
     /**
     * Sync with drawables: Use when modifying a drawable
     */
    public static final Object drawableSync = new Object();
    public static Canvas globalInstance;
    /**
     * The initial size of the Canvas
     */
    public final Dimension startSize;
    public final Point2D.Float translation;
    /**
     * The elements that are currently on the canvas
     */
    public final ElementContainer elements = new ElementContainer();
    /**
     * The list of animations that are currently running
     */
    public final List<Animation> animations = new Vector<>();
    /**
     * A CanvasComponent, which handles all the rendering n stuff
     */
    public final CanvasPanel panel;
    /**
     * Sync with animations: Notifies on animation finish
     */
    protected final Object animationSync = new Object();
    /**
     * Sync with frame: Notifies on end of frame
     */
    protected final Object frameSync = new Object();
    /**
     * The current frame
     */
    public int frame = -1;
    /**
     * The RenderLifecycle: allows you to write code to run before and after a frame is rendered
     */
    public List<RenderLifecycle> renderLifecycles = new Vector<>();
    /**
     * The options for the behavior of the canvas, see {@link CanvasOptions}
     */
    public CanvasOptions options;

    /**
     * Initializes the canvas with a default size of 900x600
     * and a title of "Canvas"
     */
    public Canvas() {
        this(900, 600, "Canvas", new CanvasOptions());
    }

    /**
     * Initialize the canvas
     *
     * @param width  the width of the canvas
     * @param height the height of the canvas
     * @param title  the title of the canvas
     */
    public Canvas(int width, int height, String title) {
        this(width, height, title, new CanvasOptions());
    }

    /**
     * Initialize the canvas with more options
     *
     * @param width   the width of the canvas
     * @param height  the height of the canvas
     * @param title   the title of the canvas
     * @param options options for the canvas
     */
    public Canvas(int width, int height, String title, CanvasOptions options) {
        super();
        this.startSize = new Dimension(width, height);
        this.translation = new Point2D.Float(0, 0);
        this.panel = new CanvasPanel(this, width, height, title);

        if (globalInstance != null)
            throw new RuntimeException("There can only be one Canvas instance");
        Canvas.globalInstance = this;

        this.options = options;
        if (options.antiAlias)
            this.renderLifecycles.add(new RenderLifecycle.AntiAliasingLifecycle());
        if (options.autoCenter)
            this.renderLifecycles.add(new RenderLifecycle.CenteringLifecycle());
        render();
    }

    /**
     * Get the global instance of the Canvas (used internally to access the Canvas)
     *
     * @return The global instance of Canvas
     */
    static public Canvas getGlobalInstance() {
        if (globalInstance == null)
            throw new RuntimeException("Canvas has not been initialized!");
        return globalInstance;
    }

    /**
     * Sets the background color of the canvas
     *
     * @param color the new background color
     */
    @SuppressWarnings("unused")
    public void setBackgroundColor(Color color) {
        this.options.backgroundColor = color;
    }

    /**
     * Get the width of the canvas
     *
     * @return The width of the canvas
     */
    public int getWidth() {
        if (panel == null) return startSize.width;
        var width = panel.getWidth();
        return width == 0 ? startSize.width : width;
    }

    /**
     * Get the height of the canvas
     *
     * @return The height of the canvas
     */
    public int getHeight() {
        if (panel == null) return startSize.height;
        var height = panel.getHeight();
        return height == 0 ? startSize.height : height;
    }

    /**
     * Set the title of this canvas
     *
     * @param title the new title
     */
    public void setTitle(String title) {
        this.panel.jframe.setTitle(title);
    }

    /**
     * Sleeps until all animations finish.
     * <pre>{@code
     * Circle c = new Circle(200, 200, 50);
     * c.animate().with(Animation.colorTo(Color.RED), 2)
     *            .with(Animation.moveBy(100, 0), 2);
     * canvas.sleep()
     * c.setColor(Color.BLUE);
     * }</pre>
     */
    public void sleep() {
        try {
            synchronized (animationSync) {
                animationSync.wait();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sleeps for the specified amount of seconds
     * <pre>{@code
     * Circle c = new Circle(200, 200, 50);
     * canvas.sleep(1); // Sleeps for 1 second
     * c.setColor(Color.RED);
     * }</pre>
     *
     * @param seconds The number of seconds to sleep for
     */
    public void sleep(double seconds) {
        // if the time is short, just Thread.sleep
        if (seconds < 0.1) {
            try {
                Thread.sleep((long) (seconds * 1000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        // otherwise, wait for the frame count to reach the specified frame
        else {
            int targetFrame = frame + (int) (seconds * fps);
            synchronized (frameSync) {
                while (frame < targetFrame) {
                    try {
                        frameSync.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    public void render() {
        // TODO: Account for the time it takes to run the render function
        // (Implement the run with a loop and thread::sleep) or dont -- im sure you will get a warning for busy waiting
        ScheduledThreadPoolExecutor poolExecutor = new ScheduledThreadPoolExecutor(1);
        poolExecutor.scheduleAtFixedRate(panel::repaint, 0, 1000000 / fps, TimeUnit.MICROSECONDS);
    }

    /**
     * Get the mouse position in the canvas.
     *
     * <pre>{@code
     * Point mousePos = canvas.getMousePos();
     * if (mousePos != null) {
     *     int x = mousePos.x;
     *     int y = mousePos.y;
     * }
     * }</pre>
     *
     * @return The mouse position, or {@code null} if the mouse is not hovering over the canvas
     */
    public Point getMousePos() {
        return panel.getMousePosition();
    }

    /**
     * Runs the specified code all on the same frame.
     * Without it, because the rendering is decoupled from your code, many operations can be randomly split across frames.
     *
     * <pre>{@code
     * Canvas canvas = new Canvas();
     * Polygon[] shapes = ...;
     * canvas.atomic(() -> {
     *     for (Polygon c : shapes) c.move(10, 0);
     * });
     * }</pre>
     *
     * @param r The code to run
     */
    public void atomic(Runnable r) {
        synchronized (drawableSync) {
            r.run();
        }
    }
}
