package paintingcanvas.canvas;

import paintingcanvas.animation.Animation;
import paintingcanvas.drawable.Drawable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
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
    public final Point startSize;
    /**
     * the elements that are currently on the canvas
     */
    public final List<Drawable<?>> elements = new Vector<>();
    /**
     * the list of animations that are currently running
     */
    public final List<Animation> animations = new ArrayList<>();
    /**
     * A CanvasComponent, which handles all the rendering n stuff
     */
    public final CanvasComponent component;
    /**
     * Used to lock the thread to wait for animations to finish
     */
    protected final Object syncObject = new Object();
    /**
     * The current frame
     */
    public int frame = -1;
    /**
     * The RenderLifecycle: allows you to write code to run before and after a frame is rendered
     */
    public RenderLifecycle renderLifecycle = new RenderLifecycle() {
    };

    /**
     * Initializes the canvas with a default size of 900x600
     * and a title of "Canvas"
     */
    public Canvas() {
        this(900, 600, "Canvas");
    }

    /**
     * Initializes the canvas
     * @param width the width of the canvas
     * @param height the height of the canvas
     * @param title the title of the canvas
     */
    public Canvas(int width, int height, String title) {
        super();
        this.startSize = new Point(width, height);
        this.component = new CanvasComponent(this, width, height, title);

        if (globalInstance != null)
            throw new RuntimeException("There can only be one Canvas instance");
        Canvas.globalInstance = this;

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
     * Get the width of the canvas
     *
     * @return The width of the canvas
     */
    public int getWidth() {
        var width = component.getWidth();
        return width == 0 ? startSize.x : width;
    }

    /**
     * Get the height of the canvas
     *
     * @return The height of the canvas
     */
    public int getHeight() {
        var height = component.getHeight();
        return height == 0 ? startSize.y : height;
    }

    /**
     * Set the title of this canvas
     *
     * @param title the new title
     */
    public void setTitle(String title) {
        this.component.jframe.setTitle(title);
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
            synchronized (syncObject) {
                syncObject.wait();
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
        try {
            Thread.sleep((long) (seconds * 1000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void render() {
        // TODO: Account for the time it takes to run the render function
        // (Implement the run with a loop and thread::sleep)
        ScheduledThreadPoolExecutor poolExecutor = new ScheduledThreadPoolExecutor(1);
        poolExecutor.scheduleAtFixedRate(() -> {
            component.repaint();
            SwingUtilities.updateComponentTreeUI(component.jframe);
            component.jframe.invalidate();
            component.jframe.validate();
        }, 0, 1000000 / fps, TimeUnit.MICROSECONDS);
    }

    public interface RenderLifecycle {
        default void renderStart(Graphics g) {
            var gc = (Graphics2D) g;
            // Enable antialiasing for elements + text
            gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            gc.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }

        default void renderEnd(Graphics g) {
        }

        default void onResize(CanvasComponent canvas, ComponentEvent e) {
            canvas.repaint();
        }
    }

    static class ResizeListener extends ComponentAdapter {
        final CanvasComponent canvas;
        Dimension lastSize = new Dimension();

        ResizeListener(CanvasComponent canvas) {
            this.canvas = canvas;
        }

        public void componentResized(ComponentEvent e) {
            // Don't fire callback multiple times for a single event
            var thisSize = e.getComponent().getSize();
            if (thisSize.equals(lastSize)) return;
            lastSize = thisSize;

            canvas.canvas.renderLifecycle.onResize(canvas, e);
        }
    }
}
