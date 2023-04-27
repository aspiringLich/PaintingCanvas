package paintingcanvas.canvas;

import paintingcanvas.misc.Misc;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Point2D;

/**
 * Allows other classes to run code at different points during the rendering
 * process. This is mainly useful for extensions / little widgets and things,
 * but I'm sure you can find other uses.
 */
public interface RenderLifecycle {
    /**
     * Runs before everything else; the {@code image} in {@link CanvasPanel} will
     * reflect your changes.
     * @param g The graphics context
     */
    default void preRender(Graphics g) {
    }

    /**
     * Runs after everything else; the {@code image} in {@link CanvasPanel} will
     * reflect your changes.
     * @param g The graphics context
     */
    default void postRender(Graphics g) {
    }

    /**
     * Runs after everything else; the {@code image} in {@link CanvasPanel} will
     * not reflect your changes.
     * @param g The graphics context
     */
    default void renderEnd(Graphics g) {
    }

    /**
     * Runs before everything else; the {@code image} in {@link CanvasPanel} will
     * not reflect your changes.
     * @param g The graphics context
     */
    default void renderStart(Graphics g) {
    }

    default void onResize(CanvasPanel canvas, ComponentEvent e) {
    }

    class ResizeListener extends ComponentAdapter {
        final CanvasPanel canvas;
        Dimension lastSize = new Dimension();

        ResizeListener(CanvasPanel canvas) {
            this.canvas = canvas;
        }

        public void componentResized(ComponentEvent e) {
            // Don't fire callback multiple times for a single event
            var thisSize = e.getComponent().getSize();
            if (thisSize.equals(lastSize)) return;
            lastSize = thisSize;

            canvas.canvas.renderLifecycles.forEach(i -> i.onResize(canvas, e));
        }
    }

    class AntiAliasingLifecycle implements RenderLifecycle {
        @Override
        public void preRender(Graphics g) {
            var gc = (Graphics2D) g;
            // Enable antialiasing for elements + text
            gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            gc.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }
    }

    class CenteringLifecycle implements RenderLifecycle {
        @Override
        public void onResize(CanvasPanel panel, ComponentEvent e) {
            if (e.getComponent() == null) return;
            var canvas = Canvas.getGlobalInstance();

            var xdiff = panel.initialWidth - panel.getWidth();
            var ydiff = panel.initialHeight - panel.getHeight();

            synchronized (canvas.translation) {
                canvas.translation.setLocation(-xdiff / 2f, -ydiff / 2f);
            }

            canvas.panel.jframe.repaint();
        }
    }
}