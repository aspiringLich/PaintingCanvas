package examples.tessellation;

import paintingcanvas.canvas.Canvas;
import paintingcanvas.animation.Animation;

import java.awt.*;

public class Main {
    public static Canvas canvas = new Canvas(960, 640, "");

    public static void main(String[] args) {
        canvas.setTitle("Tessellation project");
        canvas.sleep(0.05);

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        Tessellation tess = new Tessellation(width, height);
        tess.order();
        tess.animateAll(
                Animation.colorTo(Color.getHSBColor(0, 0.55f, 1.0f)),
                0.1,
                2,
                4,
                false
        );
    }
}
