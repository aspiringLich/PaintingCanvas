package paintingcanvas.canvas;

import paintingcanvas.InternalCanvas;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * The internal canvas component that is used to draw to the screen
 */
public class Canvas {
    /**
     * Initializes the canvas with a default size of 900x600
     * and a title of "Canvas"
     */
    public Canvas() {
        this(900, 600, "Canvas", new CanvasOptions());
    }

    /**
     * Initializes the canvas
     *
     * @param width  the width of the canvas
     * @param height the height of the canvas
     * @param title  the title of the canvas
     */
    public Canvas(int width, int height, String title) {
        this(width, height, title, new CanvasOptions());
    }

    /**
     * Initializes the canvas
     *
     * @param width   the width of the canvas
     * @param height  the height of the canvas
     * @param title   the title of the canvas
     * @param options options for the canvas
     */
    public Canvas(int width, int height, String title, CanvasOptions options) {
        if (InternalCanvas.initialized)
            throw new RuntimeException("Canvas has already been initialized");
        InternalCanvas.startSize = new Dimension(width, height);
        InternalCanvas.translation = new Point2D.Float(0, 0);
        InternalCanvas.panel = new CanvasPanel(this, width, height, title);
        InternalCanvas.canvas = this;

        InternalCanvas.options = options;
        if (options.antiAlias)
            InternalCanvas.renderLifecycles.add(new RenderLifecycle.AntiAliasingLifecycle());
        if (options.autoCenter)
            InternalCanvas.renderLifecycles.add(new RenderLifecycle.CenteringLifecycle());
        render();
    }

    /**
     * Sets the background color of the canvas
     *
     * @param color the new background color
     */
    @SuppressWarnings("unused")
    public void setBackgroundColor(Color color) {
        InternalCanvas.options.backgroundColor = color;
    }

    /**
     * Get the width of the canvas
     *
     * @return The width of the canvas
     */
    public int getWidth() {
        if (InternalCanvas.panel == null) return InternalCanvas.startSize.width;
        var width = InternalCanvas.panel.getWidth();
        return width == 0 ? InternalCanvas.startSize.width : width;
    }

    /**
     * Get the height of the canvas
     *
     * @return The height of the canvas
     */
    public int getHeight() {
        if (InternalCanvas.panel == null) return InternalCanvas.startSize.height;
        var height = InternalCanvas.panel.getHeight();
        return height == 0 ? InternalCanvas.startSize.height : height;
    }

    /**
     * Get the current frame count
     */
    public int getFrame() {
        return InternalCanvas.frame;
    }

    /**
     * Get the current configured options
     */
    public CanvasOptions getOptions() {
        return InternalCanvas.options;
    }

    /**
     * Set the title of this canvas
     *
     * @param title the new title
     */
    public void setTitle(String title) {
        InternalCanvas.panel.jframe.setTitle(title);
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
            synchronized (InternalCanvas.animationSync) {
                InternalCanvas.animationSync.wait();
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
            int targetFrame = InternalCanvas.frame + (int) (seconds * InternalCanvas.options.fps);
            synchronized (InternalCanvas.frameSync) {
                while (InternalCanvas.frame < targetFrame) {
                    try {
                        InternalCanvas.frameSync.wait();
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
        poolExecutor.scheduleAtFixedRate(
                InternalCanvas.panel::repaint, 0,
                1000000 / InternalCanvas.options.fps,
                TimeUnit.MICROSECONDS
        );
    }

    /**
     * Get the mouse position in the canvas.
     * L
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
        return InternalCanvas.panel.getMousePosition();
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
        synchronized (InternalCanvas.drawableSync) {
            r.run();
        }
    }
}