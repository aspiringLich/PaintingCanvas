package examples;

import paintingcanvas.canvas.Canvas;
import paintingcanvas.drawable.Line;
import paintingcanvas.drawable.Polygon;
import paintingcanvas.extensions.FrameCounter;
import paintingcanvas.extensions.InfoDisplay;

import java.awt.*;

@SuppressWarnings("unused")
public class Test {
    static Color randomColor() {
        return new Color((float) Math.random(), (float) Math.random(), (float) Math.random());
    }

    public static void main(String[] args) {
        Canvas canvas = new Canvas();
        new FrameCounter().line(
            () -> String.format("Animations: %d", canvas.animations.size())
        ).attach();
        new InfoDisplay().attach();

        Line l = new Line(0, 0, 100, 100, Color.RED).setColor(Color.blue);
        Polygon[] shapes = new Polygon[10];
        for (int i = 0; i < shapes.length; i++) {
            shapes[i] = new Polygon(0, 200, randomColor());
            for (int j = 0; j < 5; j++) {
                int x = (int) (Math.random() * 200);
                int y = (int) (Math.random() * 200);
                shapes[i].point(x, y);
            }
        }
        while (true) {
            for (int i = 0; i < 20; i++) {
                for (Polygon c : shapes) c.move(10, 0);
                for (Polygon c : shapes) c.move(-10, 0);
            }
        }
    }
}
