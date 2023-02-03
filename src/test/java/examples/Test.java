package examples;

import paintingcanvas.App;
import paintingcanvas.drawable.Line;
import paintingcanvas.drawable.Path;
import paintingcanvas.drawable.Polygon;
import paintingcanvas.drawable.Square;

public class Test extends App {

    public static void main(String[] args) {
        new Test().run();
    }

    public void setup() throws InterruptedException {
        setTitle("Test");

        Line line = new Line(0, 0, 100, 100).setThickness(10)
                .setColor(0xff0000);
        Polygon gaming = new Polygon(new int[][]{{100, 100}, {200, 200}, {200, 100}})
                .setColor(0xffff00);
        Square square = new Square(300, 300, 100)
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
        square.rotate(180, 1);

        sleep();

        System.out.println("This runs after all the animations are done");
    }
}
