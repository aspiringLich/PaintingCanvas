package examples;

import paintingcanvas.canvas.Canvas;
import paintingcanvas.drawable.Polygon;
import paintingcanvas.drawable.*;
import paintingcanvas.extensions.FrameCounter;

import java.awt.*;

@SuppressWarnings("unused")
public class Test {
    public static void main(String[] args) throws Exception {
        Canvas canvas = new Canvas();
        new FrameCounter().line(
                () -> String.format("Animations: %d", canvas.animations.size())
        ).attach();

        Circle c = new Circle(canvas.getWidth() / 2, canvas.getHeight() / 2, 50, Color.RED);
        while (true) {
            c.moveBy(100, 1, 1);
            c.moveBy(0, 100, 1);
            c.moveBy(-100, 1, 1);
            c.moveBy(0, -100, 1);
        }
    }
}
