package examples;

import paintingcanvas.canvas.Canvas;
import paintingcanvas.drawable.Image;
import paintingcanvas.drawable.Line;
import paintingcanvas.drawable.Polygon;
import paintingcanvas.extensions.FrameCounter;
import paintingcanvas.extensions.InfoDisplay;

import java.awt.Color;


@SuppressWarnings("unused")
public class Test {
    static Color randomColor() {
        return new Color((float) Math.random(), (float) Math.random(), (float) Math.random());
    }

    public static void main(String[] args) {
        Canvas canvas = new Canvas();

        String dir = System.getProperty("user.dir");
        Image image = new Image(0, 0, "src/test/java/examples/flop.jpg")
                .rotate(90);

        while(true) {}
    }
}
