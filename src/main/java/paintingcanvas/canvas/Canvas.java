package paintingcanvas.canvas;

import paintingcanvas.animation.Animation;
import paintingcanvas.drawable.Drawable;

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
     * the fps of the canvas
     */
    public static final int fps = 30;
    public static Canvas globalInstance;
    /**
     * the initial size of the Canvas
     */
    public final Dimension startSize;
    public final Point2D.Float translation;
    /**
     * the elements that are currently on the canvas
     */
    public final List<Drawable<?>> elements = new Vector<>();
    /**
     * the list of animations that are currently running
     */
    public final List<Animation> animations = new Vector<>();
    /**
     * A CanvasComponent, which handles all the rendering n stuff
     */
    public final CanvasPanel panel;
    /**
     * Used to lock the thread to wait for animations to finish
     */
    protected final Object animationSync = new Object();
    /**
     * Used to lock the thread to wait for a frame count
     */
    protected final Object frameSync = new Object();
    /**
     * Used to synchronize with drawables
     */
    public static final Object drawableSync = new Object();
    public boolean autoAdd;
    /**
     * The current frame
     */
    public int frame = -1;
    /**
     * The RenderLifecycle: allows you to write code to run before and after a frame is rendered
     */
    public List<RenderLifecycle> renderLifecycles = new Vector<>();
    /**
     * The background color of the canvas
     */
    public Color backgroundColor = Color.WHITE;

    /**
     * Initializes the canvas with a default size of 900x600
     * and a title of "Canvas"
     */
    public Canvas() {
        this(900, 600, "Canvas");
    }

    /**
     * Initializes the canvas
     *
     * @param width  the width of the canvas
     * @param height the height of the canvas
     * @param title  the title of the canvas
     */
    public Canvas(int width, int height, String title) {
        super();
        this.startSize = new Dimension(width, height);
        this.translation = new Point2D.Float(0, 0);
        this.panel = new CanvasPanel(this, width, height, title);

        if (globalInstance != null)
            throw new RuntimeException("There can only be one Canvas instance");
        Canvas.globalInstance = this;

        this.renderLifecycles.add(new RenderLifecycle.AntiAliasingLifecycle());
        if (getProp("paintingcanvas.autoCenter", true))
            this.renderLifecycles.add(new RenderLifecycle.CenteringLifecycle());
        if (getProp("paintingcanvas.autoAdd", true))
            this.autoAdd = true;

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

    private boolean getProp(String prop, boolean _default) {
        var val = System.getProperties().getProperty(prop);
        if (val == null) return _default;
        return Boolean.parseBoolean(val);
    }

    /**
     * Sets the background color of the canvas
     *
     * @param color the new background color
     */
    @SuppressWarnings("unused")
    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
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
        // if the time is short: just thread.sleep
        if (seconds < 0.1) {
            try {
                Thread.sleep((long) (seconds * 1000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        // otherwise wait for the frame count to reach the specified frame
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
}
