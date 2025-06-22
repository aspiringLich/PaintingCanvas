package stress_tests;

import paintingcanvas.InternalCanvas;
import paintingcanvas.canvas.Canvas;
import paintingcanvas.drawable.*;
import paintingcanvas.drawable.Image;
import paintingcanvas.drawable.Rectangle;
import paintingcanvas.extensions.FrameCounter;
import paintingcanvas.extensions.InfoDisplay;
import paintingcanvas.misc.Anchor;

import java.awt.*;
import java.util.List;

@SuppressWarnings("unused")
public class InteractionTest {
    static Color randomColor() {
        return new Color((float) Math.random(), (float) Math.random(), (float) Math.random());
    }

    public static void main(String[] args) {
        Canvas canvas = new Canvas();
        new FrameCounter().lines(() -> new String[]{
                String.format("Frame: %d", canvas.getFrame()),
                String.format("Used Memory: %dmb", (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1000 / 1000),
        }).attach();
        new InfoDisplay().attach();

        List<DrawableBase<?>> shapes = List.of(new DrawableBase<?>[]{
                new Rectangle(0, 0, 150, 200, randomColor()),
//                new Triangle(0, 0, 200, 150, randomColor()),
//                new Ellipse(0, 0, 100, 200, randomColor()),
        });

//        List<DrawableBase<?>> shapes = List.of(new DrawableBase<?>[]{s1, s5, s6});

        int space = 200;
        int i = 0;
        int cols = canvas.getWidth() / 100 - 2;
        for (DrawableBase<?> c : shapes) {
            int x = space * (i % cols) + space, y = i / cols * space + space;
            c.setPos(x, y);
            i++;

            if (c instanceof Interactable interactable) {
            } else {
                throw new RuntimeException("every shape must implement Interactable");
            }
        }

        while (true) {
            for (DrawableBase<?> c : shapes) {
                if (c instanceof Interactable interactable) {
                    if (interactable.hovered()) {
                        c.setColor(randomColor());
                    }
                }
            }
        }
    }
}