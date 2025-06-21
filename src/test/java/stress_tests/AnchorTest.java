package stress_tests;

import paintingcanvas.canvas.Canvas;
import paintingcanvas.drawable.*;
import paintingcanvas.extensions.FrameCounter;
import paintingcanvas.extensions.InfoDisplay;
import paintingcanvas.misc.Anchor;

import java.awt.Color;
import java.util.List;

@SuppressWarnings("unused")
public class AnchorTest {
    static Color randomColor() {
        return new Color((float) Math.random(), (float) Math.random(), (float) Math.random());
    }

    public static void main(String[] args) {
        Canvas canvas = new Canvas();
        new FrameCounter().attach();
        new InfoDisplay().attach();

        List<DrawableBase<?>> shapes = List.of(new DrawableBase<?>[]{
                new Rectangle(0, 0, 50, 60, randomColor()).setAnchor(Anchor.BOTTOM_LEFT),
                new Text(0, 0, "top_center", randomColor()).setFontSize(20.0).setAnchor(Anchor.TOP_CENTER),
                new Text(0, 0, "center_left g", randomColor()).setFontSize(20.0).setAnchor(Anchor.CENTER_LEFT),
                new Text(0, 0, "bottom_right", randomColor()).setFontSize(20.0).setAnchor(Anchor.BOTTOM_RIGHT),
                new Triangle(0, 0, 100, 50, randomColor()).setAnchor(Anchor.TOP_CENTER),
                new Ellipse(0, 0, 100, 50, randomColor()).setAnchor(Anchor.TOP_CENTER),
                new Image(0, 0,  "src/test/java/examples/flop.jpg").setAnchor(Anchor.TOP_CENTER).setScale(0.2, 0.2),
        });
//        List<DrawableBase<?>> shapes = List.of(new DrawableBase<?>[]{s1, s5, s6});

        int i = 0;
        int cols = canvas.getWidth() / 100 - 2;
        for (DrawableBase<?> c : shapes) {
            int x = 100 * (i % cols) + 50, y = i / cols * 100 + 100;
            c.setPos(x, y);
            new Circle(x, y, 2, Color.BLACK);
            i++;
        }

        while (true) {
            canvas.sleep(0.5);
            for (DrawableBase<?> c : shapes) {
                c.rotate(15);
            }
        }
    }
}