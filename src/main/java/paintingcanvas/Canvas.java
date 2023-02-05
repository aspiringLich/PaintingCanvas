package paintingcanvas;

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
 * The front-end of the {@link CanvasComponent} class that's more user-friendly
 * and doesn't throw a wall of text at you in the documentation because it extends JComponent or something.
 */
@SuppressWarnings("unused")
public class Canvas {
    // how many frames per second do we want to run at?
    public static final int fps = 30;
    CanvasComponent canvas;

    /**
     * Creates a new canvas with a default size (900x600) and title ("Canvas")
     */
    public Canvas() {
        this(900, 600, "Canvas");
    }

    /**
     * Creates a new canvas
     * @param width the width of the canvas
     * @param height the height of the canvas
     * @param title the title of the canvas
     */
    public Canvas(int width, int height, String title) {
        canvas = new CanvasComponent(width, height, title, this);
        App.run(this, width, height, title);
    }

    /**
     * Gets the width of the canvas
     * @return the width of the canvas
     */
    public int width() {
        return canvas.getWidth();
    }

    /**
     * Gets the width of the canvas
     * @return the width of the canvas
     */
    public int height() {
        return canvas.getHeight();
    }

    /**
     * Sets the title of the canvas
     * @param title the title of the canvas
     */
    public void setTitle(String title) {
        canvas.jframe.setTitle(title);
    }

    /**
     * Sleeps until all the animations are finished
     */
    public void sleep() {
        App.sleep();
    }

    /**
     * Waits for the specified time.
     *
     * @param time Time to wait
     * @param unit The unit that <code>time</code> is in
     */
    @SuppressWarnings("SameParameterValue")
    public void sleep(double time, paintingcanvas.misc.TimeUnit unit) {
        App.sleep(time, unit);
    }

    /**
     * Waits for the specified time in seconds.
     *
     * @param time The time in seconds.
     */
    public void sleep(double time) {
        sleep(time, paintingcanvas.misc.TimeUnit.Seconds);
    }

    /**
     * Erases the provided drawable from the canvas
     *
     * @param drawable the drawable to erase
     */
    public void erase(Drawable<?> drawable) {
        synchronized (canvas.elements) {
            canvas.elements.remove(drawable);
        }
    }

    /**
     * The internal canvas component that is used to draw to the screen
     */
    protected static class CanvasComponent extends JComponent {
        public final List<Drawable<?>> elements = new Vector<>();
        public final List<Animation> animations = new ArrayList<>();
        public final List<Event> events = new Vector<>();
        public int frame = -1;
        public RenderLifecycle renderLifecycle = new RenderLifecycle() {
        };

        final JFrame jframe;

        public CanvasComponent(int width, int height, String title, Canvas canvas) {
            super();
            jframe = new JFrame();
            jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            jframe.setSize(width, height);
            jframe.setTitle(title);
            jframe.setVisible(true);


            jframe.add(this);
            jframe.addComponentListener(new CanvasComponent.ResizeListener(this));
        }

        public void render() {
            // TODO: Account for the time it takes to run the render function
            // (Implement the run with a loop and thread::sleep)
            ScheduledThreadPoolExecutor poolExecutor = new ScheduledThreadPoolExecutor(1);
            poolExecutor.scheduleAtFixedRate(() -> {
                App._render();
                this.repaint();
                SwingUtilities.updateComponentTreeUI(jframe);
                jframe.invalidate();
                jframe.validate();
            }, 0, 1000000 / fps, TimeUnit.MICROSECONDS);
        }

        /**
         * This function simply re-renders the canvas every single frame
         */
        public void run() {
            ScheduledThreadPoolExecutor poolExecutor = new ScheduledThreadPoolExecutor(1);
            new Thread(() -> poolExecutor.scheduleAtFixedRate(() -> {
                this.repaint();
                SwingUtilities.updateComponentTreeUI(jframe);
                jframe.invalidate();
                jframe.validate();
            }, 0, 1000000 / fps, TimeUnit.MICROSECONDS));
        }


        /**
         * Paint stuf
         *
         * @param g the <code>Graphics</code> context in which to paint
         */
        public void paintComponent(Graphics g) {
            this.frame++;
            if (frame < 0) return;

            // Check / run events
            synchronized (events) {
                for (var event : events) {
                    if ((!event.repeat && event.time == frame) || (event.repeat && frame % event.time == 0))
                        event.runner.run(this);
                }
            }

            // Update element tweens
            synchronized (animations) {
                for (var animation : animations) animation.update(this.frame);
            }

            // Render elements
            renderLifecycle.renderStart(g);
            synchronized (elements) {
                for (var element : elements) element.render(g);
            }
            renderLifecycle.renderEnd(g);
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

                canvas.renderLifecycle.onResize(canvas, e);
            }
        }
    }
}