import paintingcanvas.App;
import paintingcanvas.Canvas;
import paintingcanvas.animation.Animation;
import paintingcanvas.animation.Easing;
import paintingcanvas.drawable.Rectangle;
import paintingcanvas.extensions.FrameCounter;

import java.awt.*;
import java.util.ArrayList;

public class AnimationDebug {
    final static int size = 1;
    final static int width = 1700;
    final static int height = 1000;
    final static int pad = size / 2;

    public static void main(String[] argv) {
        System.setProperty("sun.java2d.opengl", "true");
        var canvas = new Canvas(width * size + 16, height * size + 16, "test");
        new FrameCounter().lines(() -> new String[]{
                String.format("Frame: %d", canvas.canvas.frame),
                String.format("Used Memory: %dmb", (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1000 / 1000),
                String.format("Animations: %d", App.canvas.canvas.animations.size())
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
                    rect.animate().with(Animation.moveTo(pad + size * x, pad + size * y).easing(Easing.inOutNth(2)), 3);
                    removed.add(rect);
                }
            }
            rects = removed;
        }
    }
}

