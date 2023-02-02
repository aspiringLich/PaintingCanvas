package examples;

import paintingcanvas.painter.App;
import paintingcanvas.painter.drawable.Line;
import paintingcanvas.painter.drawable.Path;
import paintingcanvas.painter.drawable.Polygon;

public class Test extends App {

    public static void main(String[] args) {
        new Test().run();
    }

    @Override
    protected void setup() {
        setTitle("Test");

        Line line = new Line(0, 0, 100, 100).setThickness(10);
        Polygon gaming = new Polygon(new int[][] {{100, 100}, {200, 200}, {200, 100}});

        Path curvey = new Path().setThickness(10).moveTo(100, 100).lineTo(200, 200).lineTo(200, 100);
    }
}
