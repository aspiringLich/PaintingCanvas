package paintingcanvas.extensions;

import paintingcanvas.InternalCanvas;
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
    BufferedImage img;

    // == Image Sequence ==
    boolean recording = false;
    int inc;
    Path dir;
    String format;

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
        System.out.printf("Maybe: ffmpeg -r %d -i '%s/tmp_%%d.%s' -vf 'pad=ceil(iw/2)*2:ceil(ih/2)*2' -y -an out.mov", InternalCanvas.canvas.getOptions().fps, this.dir, this.format);
        return this;
    }

    public void screenshot(File file, String format) {
        try {
            System.out.println(file.getAbsolutePath());

            synchronized (imgSync) {
                if (!ImageIO.write(img, format, file)) {
                    var supported = String.join(", ", ImageIO.getWriterFormatNames());
                    System.err.printf("No appropriate image writer found for format `%s`, supported formats are [%s]\n", format, supported);
                    System.err.print("Please note the image format must support transparency.\n");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void renderEnd(Graphics2D g) {
        var canvas = InternalCanvas.canvas;
        var cmp = InternalCanvas.panel;

        synchronized (imgSync) {
            img = cmp.image;
        }

        if (!recording || this.dir == null) return;

        var size = cmp.getSize();
        var text = "REC";
        g.setFont(g.getFont().deriveFont(Font.PLAIN, 30));
        var width = g.getFontMetrics().stringWidth(text);
        var height = g.getFontMetrics().getHeight();
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(size.width - width - 45, 5, width + 40, height - 3);
        g.setColor(Color.WHITE);
        g.drawString(text, size.width - width - 10, height / 2 + 15);
        g.setColor(Color.RED);
        if ((canvas.getFrame() / 30) % 2 == 0) g.fillOval(size.width - 40 - width, 10, 25, 25);
        g.setColor(Color.WHITE);
        g.drawOval(size.width - 40 - width, 10, 25, 25);

        screenshot(this.dir.resolve(Path.of(String.format("tmp_%d.%s", inc++, this.format))).toFile(), this.format);
    }
}

//                  v file pattern                                  v idk i trust stack overflow
//         v framerate          v padding                                  v output file
// ffmpeg -r 30 -i 'tmp_%d.png' -vf "pad=ceil(iw/2)*2:ceil(ih/2)*2" -y -an out.mp4

// if we didn't have the padding, it would fail to compress to mp4 b/c it needs an even height
// or something