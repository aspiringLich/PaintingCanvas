package examples;

import paintingcanvas.canvas.Canvas;
import paintingcanvas.drawable.Image;
import paintingcanvas.drawable.Square;
import paintingcanvas.misc.Hue;

import java.awt.*;


@SuppressWarnings("unused")
public class Test {
    static Color randomColor() {
        return new Color((float) Math.random(), (float) Math.random(), (float) Math.random());
    }

    public static void main(String[] args) {
        Canvas canvas = new Canvas();

        String dir = System.getProperty("user.dir");

        Image image = new Image(200, 200, "src/test/java/examples/flop.jpg")
                .rotate(90);

        int i = -1;
        Square[] squares = new Square[Hue.values().length];
        for (Hue h : Hue.values())
            squares[++i] = new Square(50 + (i % 8) * 100, 50 + (i / 8) * 100, 100, new Color(h.hex));

        canvas.sleep(3);
        image.bringToFront();

        for (Square square : squares) {
            square.bringToFront();
            canvas.sleep(0.1);
        }

        for (Square square : squares) {
            square.sendToBack();
            canvas.sleep(0.1);
        }

        for (int j = 0; j < 500; j++) {
            canvas.atomic(() -> {
                for (Square square : squares)
                    square.moveHorizontal(1);
            });
            canvas.sleep(0.01);
        }
    }
}
