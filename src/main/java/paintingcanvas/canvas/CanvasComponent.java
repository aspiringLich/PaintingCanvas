package paintingcanvas.canvas;

import javax.swing.*;
import java.awt.*;

/**
 * Internal class that extends JComponent that does the initial setup of the JFrame
 * <p>
 * Mostly used to not clutter up the documentation
 */
class CanvasComponent extends JComponent {
    JFrame jframe;
    Canvas canvas;

    CanvasComponent(Canvas canvas, int width, int height, String title) {
        this.canvas = canvas;
        jframe = new JFrame();
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setSize(width, height);
        jframe.setTitle(title);
        jframe.setVisible(true);
        jframe.add(this);
        jframe.setLocationRelativeTo(null);
    }

    /**
     * The function that actually renders stuff
     *
     * @param g the <code>Graphics</code> context in which to paint
     */
    public void paintComponent(Graphics g) {
        canvas.frame++;
        var frame = canvas.frame;
        if (frame < 0) return;

        // Update animations
        synchronized (canvas.animations) {
            for (int i = 0; i < canvas.animations.size(); i++) {
                var animation = canvas.animations.get(i);

                if (animation.ended(frame)) {
                    // remove the animation
                    canvas.animations.remove(i);
                    i--;
                    // unblock if no more animations
                    if (canvas.animations.size() == 0) {
                        synchronized (canvas.syncObject) {
                            canvas.syncObject.notifyAll();
                        }
                    }
                    continue;
                }
                animation.update(frame);
            }
        }

        // Render elements
        canvas.renderLifecycle.renderStart(g);
        synchronized (canvas.elements) {
            for (var element : canvas.elements) {
                try {
                    element.render(g);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        canvas.renderLifecycle.renderEnd(g);
    }
}
