package paintingcanvas.extensions;

import paintingcanvas.canvas.Canvas;
import paintingcanvas.canvas.RenderLifecycle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Recorder implements RenderLifecycle {
    // == Screenshot ==
    final Object imgSync = new Object();
    // == Misc ==
    boolean rendering = true;
    BufferedImage img;

    // == Image Sequence ==
    boolean recording = false;
    int inc;
    Path dir;
    String format;

    public Recorder attach() {
        Canvas.getGlobalInstance().renderLifecycles.add(this);
        return this;
    }

    public Recorder record(String format) {
        try {
            this.dir = Files.createTempDirectory(".tmp");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.format = format;
        this.recording = true;
        this.inc = 0;
        return this;
    }

    public Recorder stop() {
        this.recording = false;
        System.out.printf("Maybe: ffmpeg -framerate %d -pattern_type glob -i \"%s/tmp_*.%s\" out.mp4", Canvas.fps, this.dir, this.format);
        return this;
    }

    public void screenshot(File file, String format) {
        try {
            synchronized (imgSync) {
                ImageIO.write(img, format, file);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

        if (!recording || this.dir == null) return;
        screenshot(this.dir.resolve(Path.of(String.format("tmp_%d.%s", inc++, this.format))).toFile(), this.format);
    }
}

// ffmpeg -framerate 60 -pattern_type glob -i '*.jpg' -c:v libx264 -pix_fmt yuv420p out.mp4