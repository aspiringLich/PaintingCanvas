package stress_tests;

import paintingcanvas.InternalCanvas;
import paintingcanvas.animation.Animation;
import paintingcanvas.animation.Easing;
import paintingcanvas.canvas.Canvas;
import paintingcanvas.canvas.CanvasOptions;
import paintingcanvas.drawable.Rectangle;
import paintingcanvas.extensions.FrameCounter;
import paintingcanvas.extensions.Recorder;

import java.awt.*;
import java.nio.file.Path;
import java.util.ArrayList;

class RectangleItem {
    public Rectangle rect;
    public int x;
    public int y;
}

public class AnimationTest {
    final static int size = 10;
    final static int width = 50;
    final static int height = 50;
    final static int pad = size / 2;

    public static void main(String[] argv) {
        System.setProperty("sun.java2d.opengl", "true");

        var options = new CanvasOptions().autoCenter(false);
        var canvas = new Canvas(width * size, height * size /*+ 32*/, "test", options);
        new Recorder().record(Path.of("rec"), "png").attach();
        new FrameCounter().lines(() -> new String[]{
                String.format("Frame: %d", canvas.getFrame()),
                String.format("Used Memory: %dmb", (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1000 / 1000),
                String.format("Animations: %d", InternalCanvas.animations.size())
        }).attach();

        var rects = new ArrayList<RectangleItem>();
        for (var x = 0; x < width; x++) {
            for (var y = 0; y < height; y++) {
                var rect = new Rectangle(pad + size * x, pad + size * y, size, size);
                rect.setColor(Color.getHSBColor((float) (Math.pow(x * y, 0.8) / Math.pow(((float) width * height), 0.8)), 1, 1));

                var item = new RectangleItem();
                item.rect = rect;
                item.x = x;
                item.y = y;
                rects.add(item);
            }
        }

        canvas.sleep(1);
        int i = 0;
        while (++i > 0) {
            if (i % 3 == 0) {
                for (var item : rects) {
                    item.rect.animate().with(Animation.moveTo(pad + size * item.x, pad + size * item.y).easing(Easing.easeInOut(2)), 3);
                }
            } else {
                var removed = new ArrayList<RectangleItem>();
                for (var y = 0; y < height; y++) {
                    for (var x = 0; x < width; x++) {
                        var item = rects.remove((int) (Math.random() * rects.size()));
                        item.rect.animate().with(Animation.moveTo(pad + size * x, pad + size * y).easing(Easing.easeInOut(2)), 3);
                        removed.add(item);
                    }
                }
                rects = removed;
            }
            canvas.sleep(4);
        }
    }
}

