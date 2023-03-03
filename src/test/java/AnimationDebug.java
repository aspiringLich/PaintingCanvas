import paintingcanvas.App;
import paintingcanvas.Canvas;
import paintingcanvas.animation.Animation;
import paintingcanvas.animation.Easing;
import paintingcanvas.drawable.Rectangle;
import paintingcanvas.extensions.FrameCounter;

import java.awt.*;
import java.util.ArrayList;

public class AnimationDebug {
    final static int size = 10;
    final static int width = 100;
    final static int height = 100;

    public static void main(String[] argv) {
        System.setProperty("sun.java2d.opengl", "true");
        var canvas = new Canvas(1000, 1000, "test");
        App.canvas.canvas.renderLifecycle = new FrameCounter();

        var pad = size / 2;
        var rects = new ArrayList<Rectangle>();
        for (var x = 0; x < width; x++) {
            for (var y = 0; y < height; y++) {
                var rect = new Rectangle(pad + size * x, pad + size * y, size, size);
                rect.setColor(Color.getHSBColor((float) Math.random(), 1, 1));
                rects.add(rect);
            }
        }

        while (true) {
            canvas.sleep(4);
            var removed = new ArrayList<Rectangle>();
            for (var x = 0; x < width; x++) {
                for (var y = 0; y < height; y++) {
                    var rect = rects.remove((int) (Math.random() * rects.size()));
                    rect.animate().with(Animation.moveTo(pad + size * x, pad + size * y).easing(Easing.inOutNth(2)), 3);
                    removed.add(rect);
                }
            }
            rects = removed;
        }
    }
}

