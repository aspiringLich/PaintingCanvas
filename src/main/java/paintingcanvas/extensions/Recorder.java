package paintingcanvas.extensions;

import paintingcanvas.canvas.Canvas;
import paintingcanvas.canvas.RenderLifecycle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Recorder implements RenderLifecycle {
    final Object imgSync = new Object();
    boolean rendering = true;
    BufferedImage img;

    public Recorder attach() {
        Canvas.getGlobalInstance().renderLifecycles.add(this);
        return this;
    }

    @Override
    public void renderEnd(Graphics g) {
        rendering ^= true;
        if (rendering) return;

        synchronized (imgSync) {
            var cmp = Canvas.getGlobalInstance().component;
            img = new BufferedImage(cmp.getWidth(), cmp.getHeight(), BufferedImage.TYPE_INT_RGB);
            var gc = img.getGraphics();
            cmp.paint(gc);
            gc.dispose();
        }
    }

    public void screenshot(File file, String format) {
        try {
            ImageIO.write(img, format, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
