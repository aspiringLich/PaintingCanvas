package examples;

import paintingcanvas.animation.Animation;
import paintingcanvas.animation.Easing;
import paintingcanvas.canvas.Canvas;
import paintingcanvas.drawable.Rectangle;
import paintingcanvas.extensions.FrameCounter;

import java.awt.*;
import java.util.ArrayList;

public class AnimationDebug {
    final static int size = 20;
    final static int width = 50;
    final static int height = 50;
    final static int pad = size / 2;

    public static void main(String[] argv) {
        System.setProperty("sun.java2d.opengl", "true");
        System.setProperty("paintingcanvas.autoCenter", "false");
        var canvas = new Canvas(width * size, height * size /*+ 32*/, "test");
        // var rec = new Recorder().attach().record(Path.of("rec"), "jpg");
        new FrameCounter().lines(() -> new String[]{
                String.format("Frame: %d", canvas.frame),
                String.format("Used Memory: %dmb", (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1000 / 1000),
                String.format("Animations: %d", canvas.animations.size())
        }).attach();

        var rects = new ArrayList<Rectangle>();
        for (var x = 0; x < width; x++) {
            for (var y = 0; y < height; y++) {
                var rect = new Rectangle(pad + size * x, pad + size * y, size, size);
                rect.setColor(Color.getHSBColor((float) Math.random(), 1, 1));
                rect.setColor(Color.getHSBColor((x * y) / ((float) width * height), 1, 1));
                rects.add(rect);
            }
        }

        while (true) {
            canvas.sleep(4);
            var removed = new ArrayList<Rectangle>();
            for (var y = 0; y < height; y++) {
                for (var x = 0; x < width; x++) {
                    var rect = rects.remove((int) (Math.random() * rects.size()));
                    rect.animate().with(Animation.moveTo(pad + size * x, pad + size * y).easing(Easing.easeInOut(2)), 3);
                    removed.add(rect);
                }
            }
            rects = removed;
        }
    }
}

