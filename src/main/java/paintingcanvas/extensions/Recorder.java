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

/**
 * A class that allows you to record the canvas to a image sequence, can be combined using ffmpeg
 * or something similar
 * <p>
 * stores the images in a temporary directory using {@link Files#createTempDirectory}
 */
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

    public Recorder record(Path path, String format) {
        this.dir = path;
        this.format = format;
        this.recording = true;
        this.inc = 0;

        if (filledFolder(this.dir))
            System.err.printf("Recording folder `%s` already has files in it.", path);

        try {
            Files.createDirectory(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    private boolean filledFolder(Path path) {
        if (!Files.exists(path)) return false;
        var contents = path.toFile().listFiles();
        return contents == null || contents.length > 0;
    }

    public Recorder stop() {
        this.recording = false;
        System.out.printf("Maybe: ffmpeg -r %d -i '%s/tmp_%%d.%s' -vf 'pad=ceil(iw/2)*2:ceil(ih/2)*2' -y -an out.mov", Canvas.fps, this.dir, this.format);
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
        var cmp = Canvas.getGlobalInstance().panel;

        synchronized (imgSync) {
            img = new BufferedImage(cmp.getWidth(), cmp.getHeight(), BufferedImage.TYPE_INT_RGB);
            var gc = img.getGraphics();
            cmp.paint(gc);
            Canvas.globalInstance.frame -= 1;
            gc.dispose();
        }

        if (!recording || this.dir == null) return;
        {
            var size = cmp.getSize();
            var gc = (Graphics2D) g;
            var text = "REC";
            gc.setFont(gc.getFont().deriveFont(Font.PLAIN, 30));
            var width = gc.getFontMetrics().stringWidth(text);
            var height = gc.getFontMetrics().getHeight();
            gc.setColor(Color.WHITE);
            gc.drawString(text, size.width - width - 10, height / 2 + 20);
            gc.setColor(Color.RED);
            gc.fillOval(size.width - 40 - width, 15, 25, 25);
        }
        screenshot(this.dir.resolve(Path.of(String.format("tmp_%d.%s", inc++, this.format))).toFile(), this.format);
    }
}

//                  v file pattern                                  v idk i trust stack overflow
//         v framerate          v padding                                  v output file
// ffmpeg -r 30 -i 'tmp_%d.jpg' -vf "pad=ceil(iw/2)*2:ceil(ih/2)*2" -y -an out.mp4

// if we didnt have the padding, it would fail to compress to mp4 b/c it needs an even height
// or something