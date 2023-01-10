package painter;

import painter.drawable.Drawable;
import painter.tween.Tween;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

public class Canvas extends JComponent {
    public final List<Drawable> elements = new ArrayList<>();
    public final List<Tween> tweens = new ArrayList<>();
    public int frame = -1;
    public RenderLifecycle renderLifecycle = new RenderLifecycle() {};


    /**
     * Paint stuf
     *
     * @param g the <code>Graphics</code> context in which to paint
     */
    public void paintComponent(Graphics g) {
        this.frame++;
        if (frame < 0) return;

        // Update element tweens
        for (Tween tween : tweens) tween.update(this.frame);

        // Render elements
        renderLifecycle.renderStart(g);
        for (Drawable element : elements) element.render(g);
        renderLifecycle.renderEnd(g);
    }


    public interface RenderLifecycle {
        default void renderStart(Graphics g) {
            var gc = (Graphics2D) g;
            // Enable antialiasing for elements + text
            gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            gc.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }

        default void renderEnd(Graphics g) {}

        default void onResize(Canvas canvas, ComponentEvent e) {
            canvas.repaint();
        }
    }

    static class ResizeListener extends ComponentAdapter {
        Canvas canvas;
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