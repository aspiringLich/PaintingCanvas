package paintingcanvas.canvas;

import paintingcanvas.drawable.Drawable;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Internal class that extends JPanel that does the initial setup of the JFrame
 * <p>
 * Mostly used to not clutter up the documentation
 */
public class CanvasPanel extends JPanel {
    private final int width, height;
    public JFrame jframe;
    /**
     * The image that is drawn to the screen
     */
    public BufferedImage image;
    Canvas canvas;

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

        synchronized (canvas.translation) {
            g.translate((int) canvas.translation.x, (int) canvas.translation.y);
        }

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

        // Render elements onto an image
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        var ig = image.getGraphics();

        canvas.renderLifecycles.forEach(e -> e.renderStart(ig));
        ig.setColor(canvas.backgroundColor);
        ig.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        synchronized (Canvas.drawableSync) {
            for (Drawable<?> element : canvas.elements) {
                try {
                    element.render(ig);
                } catch (Exception e) {
                    e.printStackTrace();
                    element.erase();
                }
            }
        }

        // copy the image onto the screen
        g.drawImage(image, 0, 0, null);
        ig.dispose();
        canvas.renderLifecycles.forEach(e -> e.renderEnd(g));
    }
}
