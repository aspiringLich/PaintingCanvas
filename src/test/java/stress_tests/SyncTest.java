package stress_tests;

import java.awt.*;

import paintingcanvas.InternalCanvas;
import paintingcanvas.canvas.Canvas;
import paintingcanvas.drawable.*;
import paintingcanvas.extensions.*;
import paintingcanvas.drawable.Polygon;

@SuppressWarnings("unused")
public class SyncTest {
    static Color randomColor() {
        return new Color((float) Math.random(), (float) Math.random(), (float) Math.random());
    }

    public static void main(String[] args) {
        Canvas canvas = new Canvas();
        new FrameCounter().line(
                () -> String.format("Animations: %d", InternalCanvas.animations.size())
        ).attach();
        new InfoDisplay().attach();

        Polygon[] shapes = new Polygon[10];
        for (int i = 0; i < shapes.length; i++) {
            shapes[i] = new Polygon(0, 200).setColor(randomColor());
            for (int j = 0; j < 5; j++) {
                int x = (int) (Math.random() * 200);
                int y = (int) (Math.random() * 200);
                shapes[i].point(x, y);
            }
        }
        while (true) {
            int tempN = 10;
            Circle[] temps = new Circle[tempN];
            for (int i = 0; i < tempN; i++) {
                temps[i] = new Circle((int) (Math.random() * canvas.getWidth()), (int) (Math.random() * canvas.getHeight()), 10, randomColor());
            }

            for (int i = 0; i < 20; i++) {
                for (Polygon c : shapes) c.move(10, 0);
                for (Polygon c : shapes) c.move(-10, 0);
            }

            for (int i = 0; i < tempN; i++) {
                temps[i].erase();
            }
        }
    }
}