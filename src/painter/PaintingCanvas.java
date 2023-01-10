package painter;

import painter.drawable.Drawable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

public class PaintingCanvas extends JComponent {
    public final List<Drawable> elements = new ArrayList<>();
    public RenderLifecycle renderLifecycle = new RenderLifecycle() {};


    /**
     * Paint stuf
     *
     * @param g the <code>Graphics</code> context in which to paint
     */
    public void paintComponent(Graphics g) {
        renderLifecycle.renderStart(g);
        for (Drawable object : elements) object.draw(g);
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

        default void onResize(PaintingCanvas canvas, ComponentEvent e) {
            canvas.repaint();
        }
    }

    static class ResizeListener extends ComponentAdapter {
        PaintingCanvas canvas;

        ResizeListener(PaintingCanvas canvas) {
            this.canvas = canvas;
        }

        public void componentResized(ComponentEvent e) {
            canvas.renderLifecycle.onResize(canvas, e);
        }
    }
}