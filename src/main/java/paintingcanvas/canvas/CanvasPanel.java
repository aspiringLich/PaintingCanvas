package paintingcanvas.canvas;

import paintingcanvas.drawable.Drawable;

import javax.swing.*;
import java.awt.*;

/**
 * Internal class that extends JPanel that does the initial setup of the JFrame
 * <p>
 * Mostly used to not clutter up the documentation
 */
public class CanvasPanel extends JPanel {
    public JFrame jframe;
    Canvas canvas;

    private final int width, height;

    CanvasPanel(Canvas canvas, int width, int height, String title) {
        this.canvas = canvas;
        jframe = new JFrame();

        this.width = width;
        this.height = height;
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setTitle(title);
        jframe.setVisible(true);
//        jframe.setLayout(null);
        jframe.add(this);
        jframe.pack();
        jframe.setLocationRelativeTo(null);
        jframe.addComponentListener(new RenderLifecycle.ResizeListener(this));
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    /**
     * The function that actually renders stuff
     *
     * @param g the <code>Graphics</code> context in which to paint
     */
    public void paintComponent(Graphics g) {
        synchronized (canvas.frameSync) {
            canvas.frameSync.notify();
        }
        super.paintComponent(g);

        canvas.frame++;
        if (canvas.frame < 0) return;

        synchronized (canvas.animations) {
            // Update animations
            for (int i = 0; i < canvas.animations.size(); i++) {
                var animation = canvas.animations.get(i);
                if (!animation.ended(canvas.frame)) {
                    animation.update(canvas.frame);
                    continue;
                }

                // remove the animation
                canvas.animations.remove(i);
                i--;

                // unblock if no more animations
                if (canvas.animations.size() != 0) continue;
                synchronized (canvas.animationSync) {
                    canvas.animationSync.notifyAll();
                }
            }
        }

        // Render elements
        canvas.renderLifecycles.forEach(e -> e.renderStart(g));
        simplePaint(g);
        canvas.renderLifecycles.forEach(e -> e.renderEnd(g));
    }

    /**
     * Does one thing: draws all the thingies.
     * @param g The graphics context to draw into
     */
    public void simplePaint(Graphics g) {
        g.setColor(canvas.backgroundColor);
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        synchronized (Canvas.drawableSync) {
            for (Drawable<?> element : canvas.elements) {
                try {
                    element.render(g);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
