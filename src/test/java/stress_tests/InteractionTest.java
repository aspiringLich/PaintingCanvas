package stress_tests;

import paintingcanvas.canvas.Canvas;
import paintingcanvas.drawable.*;
import paintingcanvas.drawable.Rectangle;
import paintingcanvas.extensions.FrameCounter;
import paintingcanvas.extensions.InfoDisplay;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

@SuppressWarnings("unused")
public class InteractionTest {
    static Color randomColor() {
        return new Color((float) Math.random(), (float) Math.random(), (float) Math.random());
    }

    static boolean c = false;

    public static void main(String[] args) {
        Canvas canvas = new Canvas();
        new InfoDisplay().attach();
        new FrameCounter().lines(() -> new String[]{
                String.format("Frame: %d", canvas.getFrame()),
                String.format("Used Memory: %dmb", (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1000 / 1000),
        }).attach();

        List<DrawableBase<?>> shapes = List.of(new DrawableBase<?>[]{
                new Rectangle(0, 0, 60, 100, randomColor()),
                new Ellipse(0, 0, 100, 200, randomColor()),
                new Circle(0, 0, 30, randomColor()),
                new Triangle(0, 0, 100, 150, randomColor()),
        });

//        List<DrawableBase<?>> shapes = List.of(new DrawableBase<?>[]{s1, s5, s6});

        int space = 100;
        int i = 0;
        int cols = canvas.getWidth() / 100 - 2;
        for (DrawableBase<?> c : shapes) {
            int x = space * (i % cols) + space, y = i / cols * space + space;
            c.setPos(x, y);
            i++;

            if (c instanceof Interactable interactable) {
                continue;
            }
            throw new RuntimeException("every shape must implement Interactable");
        }

        while (true) {
            if (canvas.clicked()) {
                c = !c;
                if (c) canvas.setBackgroundColor(new Color(240, 240, 240));
                else canvas.setBackgroundColor(new Color(255, 255, 255));
            }
            if (canvas.keyDown(KeyEvent.VK_UP)) {
                shapes.forEach(d -> d.moveVertical(-1));
                canvas.sleep();
            }
            if (canvas.keyDown(KeyEvent.VK_DOWN)) {
                shapes.forEach(d -> d.moveVertical(1));
                canvas.sleep();
            }
            if (canvas.keyDown(KeyEvent.VK_LEFT)) {
                shapes.forEach(d -> d.moveHorizontal(-1));
                canvas.sleep();
            }
            if (canvas.keyDown(KeyEvent.VK_RIGHT)) {
                shapes.forEach(d -> d.moveHorizontal(1));
                canvas.sleep();
            }
            for (DrawableBase<?> c : shapes) {
                if (c instanceof Interactable interactable) {
                    if (interactable.hovered()) {
                        c.setColor(randomColor());
                    }
                    if (interactable.clicked()) {
                        c.setPos((int) (Math.random() * (canvas.getWidth() - 100) + 50), (int) (Math.random() * (canvas.getHeight() - 100) + 50));
                        c.setRotation(Math.random() * 360);
                    }
                }
            }
        }
    }
}