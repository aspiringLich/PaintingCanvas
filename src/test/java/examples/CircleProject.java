package examples;

import paintingcanvas.animation.Animation;
import paintingcanvas.animation.Easing;
import paintingcanvas.canvas.Canvas;
import paintingcanvas.drawable.Circle;

import java.awt.*;
import java.util.ArrayList;

public class CircleProject {

    public static void main(String[] args) {
        Canvas canvas = new Canvas();
        canvas.setTitle("Circle Projects");
        var width = canvas.getWidth();
        var height = canvas.getHeight();

        // == Init Circles (2) ==
        var circles = new ArrayList<Circle>();
        for (int i = 0; i < 100; i++) {
            var circle = new Circle(width / 2, height / 2, 25).setColor(Color.BLACK);
            circles.add(circle);
        }

        // == Reset (1) ==
        canvas.sleep(3);
        for (var circle : circles)
            circle.setPos(0, 0);

        // == Place Random (1) ==
        canvas.sleep(3);
        canvas.setTitle("Circle Projects - Place Random");
        for (var circle : circles)
            circle.animate().with(Animation.moveTo((int) (Math.random() * width), (int) (Math.random() * height)).easing(Easing.inOutNth(2)), 1);

        // == Circle Line (2) ==
        canvas.sleep(3);
        canvas.setTitle("Circle Projects - Circle Line");
        for (int i = 0; i < circles.size(); i++)
            circles.get(i).animate().with(Animation.moveTo((i - 2) * 10, height / 2).easing(Easing.inOutNth(2)), 1);

        // == Random Color (2) ==
        canvas.sleep(3);
        canvas.setTitle("Circle Projects - Random Color");
        for (var circle : circles)
            circle.animate().with(Animation.colorTo((int) (Math.random() * 0xFFFFFF)), 1);

        // Red Color Gradient (2) ==
        canvas.sleep(3);
        canvas.setTitle("Circle Projects - Red Color Gradient");
        for (int i = 0; i < circles.size(); i++)
            circles.get(i).animate().with(Animation.colorTo(0xFF0000 + i * 0x010000), 3);

        // == Red Gradient Over Time (2) ==
        canvas.sleep(6);
        canvas.setTitle("Circle Projects - Red Gradient Over Time");
        for (var circle : circles)
            circle.animate().with(Animation.colorTo(0xFF0000), 1);

        // == Fall Down (2) ==
        canvas.sleep(3);
        canvas.setTitle("Circle Projects - Fall Down");
        for (var circle : circles)
            circle.animate().with(Animation.moveTo(circle.getX(), height).easing(Easing.inOutNth(2)), Math.random() * 3 + 1);

        // == Circle Rows (3) ==
        canvas.setTitle("Circle Projects - Circle Rows");
        canvas.sleep();
        for (int y = 0; y < 10; y++)
            for (int x = 0; x < 10; x++)
                circles.get(y * 10 + x).animate().with(Animation.moveTo(width / 2 - 250 + x * 50, height / 2 - 300 + y * 50).easing(Easing.inOutNth(2)), 1);

        // == Hue Shift ==
        canvas.sleep(3);
        canvas.setTitle("Circle Projects - Hue Shift");
        for (var circle : circles)
            circle.animate().with(Animation.colorTo(Color.getHSBColor((float) Math.random(), 1, 1)), 1);
    }
}