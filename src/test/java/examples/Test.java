package examples;

import paintingcanvas.Canvas;
import paintingcanvas.drawable.Line;
import paintingcanvas.drawable.Path;
import paintingcanvas.drawable.Polygon;
import paintingcanvas.drawable.Square;
import paintingcanvas.drawable.Circle;

import java.awt.Color;

@SuppressWarnings("unused")
public class Test {
    public static void main(String[] args) {
        Canvas canvas = new Canvas();
        canvas.setTitle("Test");

        new Circle(0, 0, 10, Color.RED);
        new Circle(0, canvas.height(), 10, Color.RED);
        new Circle(canvas.width(), canvas.height(), 10, Color.RED);
        new Circle(canvas.width(), 0, 10, Color.RED);

        Line line = new Line(0, 0, 100, 100).setThickness(10)
                .setColor(0xff0000);

        Polygon gaming = new Polygon(new int[][]{{100, 100}, {200, 200}, {200, 100}})
                .setColor(0xffff00);
        Square square = new Square(0, 0, 100)
                .setColor(0x00ff00);

        Path curvey = new Path()
                .setThickness(10)
                .cursorTo(300, 300)
                .lineTo(200, 300)
                .quadTo(300, 300, 200, 200)
                .setColor(0x00ffff);
        curvey.moveTo(100, 100, 3)
                .rotateTo(90, 3);
        line.rotateTo(180, 3);
        square.rotateBy(180, 1);

        canvas.sleep();

        System.out.println("This runs after all the animations are done");

        curvey.fadeOut(10);
    }
}
