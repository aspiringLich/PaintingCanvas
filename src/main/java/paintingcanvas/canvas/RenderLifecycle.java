package paintingcanvas.canvas;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * <p>
 *     Allows other classes to run code at different points during the rendering process. This is mainly useful for
 *     extensions / little widgets and things, but I'm sure you can find other uses.
 * </p>
 * <p>
 *     Here's <em>exactly</em> how it works (sort of). First the {@link java.awt.image.BufferedImage image} is created,
 *     and the background is drawn. Next, {@link RenderLifecycle#preRender(Graphics)} is called, and all the
 *     {@link paintingcanvas.drawable.Drawable Drawables} get drawn. Then {@link RenderLifecycle#postRender(Graphics)}
 *     is called, {@link RenderLifecycle#renderStart(Graphics)} is called, and finally, the image is copied over to the
 *     screen, and {@link RenderLifecycle#renderEnd(Graphics)} is called.
 * </p>
 * <p>
 *     Note that {@code preRender} / {@code postRender} are called on a different graphics context compared to
 *     {@code renderStart} / {@code renderEnd}.
 * </p>
 */
public interface RenderLifecycle {
    /**
     * Runs before everything else; the {@code image} in {@link CanvasPanel} will
     * reflect your changes.
     *
     * @param g The graphics context
     */
    default void preRender(Graphics g) {
    }

    /**
     * Runs after everything else; the {@code image} in {@link CanvasPanel} will
     * reflect your changes.
     *
     * @param g The graphics context
     */
    default void postRender(Graphics g) {
    }

    /**
     * Runs after everything else; the {@code image} in {@link CanvasPanel} will
     * not reflect your changes.
     *
     * @param g The graphics context
     */
    default void renderEnd(Graphics g) {
    }

    /**
     * Runs before everything else; the {@code image} in {@link CanvasPanel} will
     * not reflect your changes.
     *
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
        void antiAlias(Graphics g) {
            var gc = (Graphics2D) g;
            // Enable antialiasing for elements + text
            gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            gc.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }

        @Override
        public void preRender(Graphics g) {
            antiAlias(g);
        }

        @Override
        public void renderStart(Graphics g) {
            antiAlias(g);
        }
    }

    class CenteringLifecycle implements RenderLifecycle {
        boolean first = true;
        int x = 0, y = 0;

        @Override
        public void onResize(CanvasPanel panel, ComponentEvent e) {
            if (e.getComponent() == null) return;
            var canvas = Canvas.getGlobalInstance();

            var xDiff = panel.initialWidth - panel.getWidth() - x;
            var yDiff = panel.initialHeight - panel.getHeight() - y;

            if (first) {
                first = false;
                x = xDiff;
                y = yDiff;
                return;
            }

            synchronized (canvas.translation) {
                canvas.translation.setLocation(-xDiff / 2f, -yDiff / 2f);
            }

            canvas.panel.jframe.repaint();
        }
    }
}