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
    default void renderStart(Graphics g) {
    }

    default void renderEnd(Graphics g) {
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
        public void renderStart(Graphics g) {
            var gc = (Graphics2D) g;
            // Enable antialiasing for elements + text
            gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            gc.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }
    }

    class CenteringLifecycle implements RenderLifecycle {
        final int error = 50;
        private Point2D.Double lastSize;
        private boolean active = false;

        @Override
        public void onResize(CanvasPanel canvasComponent, ComponentEvent e) {
            if (e.getComponent() == null) return;
            var canvas = Canvas.getGlobalInstance();
            if (lastSize == null || !this.active) {
                var size = e.getComponent().getSize();
                lastSize = new Point2D.Double(size.width, size.height);
                if (Misc.equality(lastSize.x, canvas.startSize.width, error) &&
                        Misc.equality(lastSize.y, canvas.startSize.height, error))
                    active = true;
                return;
            }

            var _newSize = canvasComponent.jframe.getSize();
            var newSize = new Point2D.Double(_newSize.width, _newSize.height);
            if (lastSize.equals(newSize)) return;

            var widthDiff = (newSize.x - lastSize.x) / 2f;
            var heightDiff = (newSize.y - lastSize.y) / 2f;
            lastSize = newSize;

            synchronized (canvas.translation) {
                var old = canvas.translation;
                canvas.translation.setLocation(old.x + widthDiff, old.y + heightDiff);
            }

            canvas.panel.jframe.repaint();
        }
    }
}