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
    final int initialWidth, initialHeight;
    public JFrame jframe;
    /**
     * The image that is drawn to the screen
     */
    public BufferedImage image;
    public Canvas canvas;

    CanvasPanel(Canvas canvas, int width, int height, String title) {
        this.initialWidth = width;
        this.initialHeight = height;

        this.canvas = canvas;
        jframe = new JFrame();

        jframe.setTitle(title);
//        jframe.setLayout(null);
        jframe.add(this);
        jframe.pack();
        jframe.setLocationRelativeTo(null);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.addComponentListener(new RenderLifecycle.ResizeListener(this));
        jframe.setVisible(true);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(initialWidth, initialHeight);
    }

    /**
     * The function that actually renders stuff
     *
     * @param g the <code>Graphics</code> context in which to paint
     */
    public void paintComponent(Graphics g) {
        var gc = (Graphics2D) g;
        synchronized (canvas.frameSync) {
            canvas.frameSync.notify();
        }
        super.paintComponent(gc);

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

        // Render elements onto an image
        image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        var ig = (Graphics2D) image.getGraphics();

        ig.setColor(Color.WHITE);
        ig.fillRect(0, 0, getWidth(), getHeight());
        synchronized (canvas.translation) {
//            System.out.println(canvas.translation);
            ig.translate((int) canvas.translation.x, (int) canvas.translation.y);
        }

        canvas.renderLifecycles.forEach(e -> e.preRender(ig));
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
        canvas.renderLifecycles.forEach(e -> e.postRender(ig));

        // copy the image onto the screen
        canvas.renderLifecycles.forEach(e -> e.renderStart(g));
        gc.drawImage(
                image,
                0, 0,
                null
        );
        ig.dispose();
        canvas.renderLifecycles.forEach(e -> e.renderEnd(g));
    }
}
