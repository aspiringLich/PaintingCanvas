package stress_tests;

import paintingcanvas.InternalCanvas;
import paintingcanvas.canvas.Canvas;
import paintingcanvas.drawable.Polygon;
import paintingcanvas.drawable.Triangle;
import paintingcanvas.extensions.FrameCounter;
import paintingcanvas.extensions.InfoDisplay;

import java.awt.*;

@SuppressWarnings("unused")
public class RotateTest {
    static Color randomColor() {
        return new Color((float) Math.random(), (float) Math.random(), (float) Math.random());
    }

    public static void main(String[] args) {
        Canvas canvas = new Canvas();
        new FrameCounter().line(
                () -> String.format("Animations: %d", InternalCanvas.animations.size())
        ).attach();
        new InfoDisplay().attach();

        Triangle[] shapes = new Triangle[10];
        for (int i = 0; i < shapes.length; i++) {
            shapes[i] = new Triangle(i * 200, canvas.getHeight() / 2, 100, 200, randomColor());
        }


        while (true) {
            for (Triangle c : shapes) c.rotate(10);
            canvas.sleep(1);
        }
    }
}