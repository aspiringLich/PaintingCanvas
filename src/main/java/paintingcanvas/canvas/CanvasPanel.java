package paintingcanvas.canvas;

import paintingcanvas.InternalCanvas;

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
        var animations = InternalCanvas.animations;
        var options = InternalCanvas.options;

        synchronized (InternalCanvas.frameSync) {
            InternalCanvas.frameSync.notify();
        }
        super.paintComponent(gc);

        InternalCanvas.frame++;
        if (InternalCanvas.frame < 0) return;

        synchronized (InternalCanvas.animations) {
            // Update animations
            for (int i = 0; i < animations.size(); i++) {
                var animation = animations.get(i);
                if (!animation.ended(InternalCanvas.frame)) {
                    animation.update(InternalCanvas.frame);
                    continue;
                }

                // remove the animation
                animations.remove(i);
                i--;

                // unblock if no more animations
                if (!animations.isEmpty()) continue;
                synchronized (InternalCanvas.animationSync) {
                    InternalCanvas.animationSync.notifyAll();
                }
            }
        }

        // Render elements onto an image
        image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        var ig = (Graphics2D) image.getGraphics();

        ig.setColor(options.backgroundColor);
        ig.fillRect(0, 0, getWidth(), getHeight());
        synchronized (InternalCanvas.translation) {
//            System.out.println(canvas.translation);
            ig.translate((int) InternalCanvas.translation.x, (int) InternalCanvas.translation.y);
        }

        InternalCanvas.renderLifecycles.forEach(e -> e.preRender(ig));
        synchronized (InternalCanvas.drawableSync) {
            InternalCanvas.elements.foreach(element -> {
                try {
                    element.render(ig);
                } catch (Exception e) {
                    e.printStackTrace();
                    element.erase();
                }
            });
        }
        InternalCanvas.renderLifecycles.forEach(e -> e.postRender(ig));

        // copy the image onto the screen
        InternalCanvas.renderLifecycles.forEach(e -> e.renderStart(gc));
        gc.drawImage(
                image,
                0, 0,
                null
        );
        ig.dispose();
        InternalCanvas.renderLifecycles.forEach(e -> e.renderEnd(gc));
    }
}
