package paintingcanvas;

import paintingcanvas.drawable.Drawable;
import paintingcanvas.animation.Animation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Canvas extends JComponent {
    public final List<Drawable> elements = new Vector<>();
    public final List<Animation> animations = new ArrayList<>();
    public final List<Event> events = new Vector<>();
    public int frame = -1;
    public RenderLifecycle renderLifecycle = new RenderLifecycle() {
    };

    // how many frames per second do we want to run at?
    public static final int fps = 30;
    private final JFrame jframe;

    public Canvas(int width, int height, String title) {
        super();
        jframe = new JFrame();
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setSize(width, height);
        jframe.setTitle(title);
        jframe.setVisible(true);

        jframe.add(this);
        jframe.addComponentListener(new Canvas.ResizeListener(this));
    }

    public void setTitle(String title) {
        jframe.setTitle(title);
    }

    /**
     * The "render function", this will run the function in app every single frame at the set fps.
     * An alternative to the "run" function
     *
     * @param app contains the render function to run every frame
     */
    public void render(App app) {
        // TODO: Account for the time it takes to run the render function
        // (Implement the run with a loop and thread::sleep)
        ScheduledThreadPoolExecutor poolExecutor = new ScheduledThreadPoolExecutor(1);
        poolExecutor.scheduleAtFixedRate(() -> {
            app._render();
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

        default void onResize(Canvas canvas, ComponentEvent e) {
            canvas.repaint();
        }
    }

    static class ResizeListener extends ComponentAdapter {
        final Canvas canvas;
        Dimension lastSize = new Dimension();

        ResizeListener(Canvas canvas) {
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