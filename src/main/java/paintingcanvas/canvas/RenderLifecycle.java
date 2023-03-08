package paintingcanvas.canvas;

import paintingcanvas.animation.MovementAnimation;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public interface RenderLifecycle {
    default void renderStart(Graphics g) {
    }

    default void renderEnd(Graphics g) {
    }

    default void onResize(CanvasComponent canvas, ComponentEvent e) {
    }

    class ResizeListener extends ComponentAdapter {
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
        private static Dimension lastSize;

        @Override
        public void onResize(CanvasComponent canvasComponent, ComponentEvent e) {
            if (lastSize == null) {
                lastSize = e.getComponent().getSize();
                return;
            }

            var canvas = Canvas.getGlobalInstance();
            var newSize = canvasComponent.jframe.getSize();
            if (lastSize.equals(newSize)) return;

            var widthDiff = (newSize.width - lastSize.width) / 2f;
            var heightDiff = (newSize.height - lastSize.height) / 2f;
            lastSize = newSize;

            synchronized (canvas.elements) {
                canvas.elements.forEach(s -> {
                    s.x += widthDiff;
                    s.y += heightDiff;
                });
            }

            canvas.animations.stream().filter(a -> a instanceof MovementAnimation).forEach(s -> {
                var anim = (MovementAnimation) s;
                anim.start = new Point(anim.start.x + (int) widthDiff, anim.start.y + (int) heightDiff);
                anim.end = new Point(anim.end.x + (int) widthDiff, anim.end.y + (int) heightDiff);
            });

            canvas.component.jframe.repaint();
        }
    }
}