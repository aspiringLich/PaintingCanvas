package examples;

import paintingcanvas.canvas.Canvas;
import paintingcanvas.drawable.*;
import paintingcanvas.extensions.FrameCounter;

import java.awt.*;

@SuppressWarnings("unused")
public class Test {
    public static void main(String[] args) {
        Canvas canvas = new Canvas();
        new FrameCounter().line(
                () -> String.format("Animations: %d", canvas.animations.size())
        ).attach();

        Line l = new Line(0, 0, 100, 100, Color.RED).setColor(Color.blue);
        Circle c = new Circle(canvas.getWidth() / 2, canvas.getHeight() / 2, 50, Color.RED);
        while (true) {
            c.moveBy(100, 1, 1);
            canvas.sleep(1);
            c.moveBy(0, 100, 1);
            canvas.sleep(1);
            c.moveBy(-100, 1, 1);
            canvas.sleep(1);
            c.moveBy(0, -100, 1);
            canvas.sleep(1);
        }
    }
}
