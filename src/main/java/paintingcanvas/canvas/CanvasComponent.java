package paintingcanvas.canvas;

import javax.swing.*;
import java.awt.*;

/**
 * Internal class that extends JComponent that does the initial setup of the JFrame
 * <p>
 * Mostly used to not clutter up the documentation
 */
public class CanvasComponent extends JComponent {
    public JFrame jframe;
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
        jframe.addComponentListener(new RenderLifecycle.ResizeListener(this));
    }

    /**
     * The function that actually renders stuff
     *
     * @param g the <code>Graphics</code> context in which to paint
     */
    public void paintComponent(Graphics g) {
        canvas.frame++;
        if (canvas.frame < 0) return;

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
            synchronized (canvas.syncObject) {
                canvas.syncObject.notifyAll();
            }
        }

        // Render elements
        canvas.renderLifecycles.forEach(e -> e.renderStart(g));
        for (var element : canvas.elements) {
            try {
                element.render(g);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        canvas.renderLifecycles.forEach(e -> e.renderEnd(g));
    }
}
