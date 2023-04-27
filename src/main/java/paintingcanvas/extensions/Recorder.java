package paintingcanvas.extensions;

import paintingcanvas.canvas.Canvas;
import paintingcanvas.canvas.RenderLifecycle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

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
        this.inc = 0;

        try {
            Files.createDirectory(path);
        } catch (FileAlreadyExistsException e) {
            // make sure its safe to delete everything in it
            // if so, delete it

            var contents = path.toFile().listFiles();
            // go through all files in the folder and check if they are related to the recording
            for (var file : Objects.requireNonNull(contents)) {
                var name = file.getName();
                assert (file.isFile());
                if (name.startsWith("tmp_") && name.endsWith(format))
                    continue;
                if (name.equals("out.mov") || name.equals("out.mp4"))
                    continue;
                // if the file is not related to the recording, print an error
                System.err.printf(
                        "Recording folder `%s` has unrelated files in it (%s). " +
                                "Related files are generated pictures (e.g: tmp_1.%s) " +
                                "or `out.mov` / `out.mp4`. Recording aborted!\n", path, name, format);
                return this;
            }
            try {
                for (var file : contents)
                    Files.delete(file.toPath());
            } catch (IOException _e) {
                _e.printStackTrace();
                return this;
            }
        } catch (IOException e) {
            // something hapenned idk just return
            e.printStackTrace();
            return this;
        }

        this.recording = true;
        return this;
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
        var canvas = Canvas.getGlobalInstance();
        var cmp = canvas.panel;

        synchronized (imgSync) {
            img = cmp.image;
        }

        if (!recording || this.dir == null) return;
        {
            var size = cmp.getSize();
            var gc = (Graphics2D) g;
            var text = "REC";
            gc.setFont(gc.getFont().deriveFont(Font.PLAIN, 30));
            var width = gc.getFontMetrics().stringWidth(text);
            var height = gc.getFontMetrics().getHeight();
            gc.setColor(new Color(0, 0, 0, 180));
            gc.fillRect(size.width - width - 45, 5, width + 40, height - 3);
            gc.setColor(Color.WHITE);
            gc.drawString(text, size.width - width - 10, height / 2 + 15);
            gc.setColor(Color.RED);
            if ((canvas.frame / 30) % 2 == 0) gc.fillOval(size.width - 40 - width, 10, 25, 25);
            gc.setColor(Color.WHITE);
            gc.drawOval(size.width - 40 - width, 10, 25, 25);
        }
        screenshot(this.dir.resolve(Path.of(String.format("tmp_%d.%s", inc++, this.format))).toFile(), this.format);
    }
}

//                  v file pattern                                  v idk i trust stack overflow
//         v framerate          v padding                                  v output file
// ffmpeg -r 30 -i 'tmp_%d.jpg' -vf "pad=ceil(iw/2)*2:ceil(ih/2)*2" -y -an out.mp4

// if we didnt have the padding, it would fail to compress to mp4 b/c it needs an even height
// or something