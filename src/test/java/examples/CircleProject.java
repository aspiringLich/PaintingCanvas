package examples;

import paintingcanvas.painter.App;
import paintingcanvas.painter.drawable.Circle;

import java.awt.*;
import java.util.ArrayList;

public class CircleProject extends App {
    public static void main(String[] args) {
        new CircleProject().run();
    }

    @Override
    protected void setup() {
        setTitle("Circle Projects");
        var width = 1000;
        var height = 600;

        // == Init Circles (2) ==
        var circles = new ArrayList<Circle>();
        for (int i = 0; i < 100; i++) {
            var circle = new Circle(width / 2 - 50, height / 2 - 50, 50).setColor(Color.BLACK);
            circles.add(circle);
        }

        // == Reset (1) ==
        sleep(3);
        for (var circle : circles)
            circle.setPos(0, 0);

        // == Place Random (1) ==
        sleep(3);
        setTitle("Circle Projects - Place Random");
        for (var circle : circles)
            circle.animate().with(moveTo((int) (Math.random() * width) - 25, (int) (Math.random() * height) - 25), 1);

        // == Circle Line (2) ==
        sleep(3);
        setTitle("Circle Projects - Circle Line");
        for (int i = 0; i < circles.size(); i++)
            circles.get(i).animate().with(moveTo((i - 2) * 10, height / 2 - 50), 1);

        // == Random Color (2) ==
        sleep(3);
        setTitle("Circle Projects - Random Color");
        for (var circle : circles)
            circle.animate().with(colorTo((int) (Math.random() * 0xFFFFFF)), 1);

        // Red Color Gradient (2) ==
        sleep(3);
        setTitle("Circle Projects - Red Color Gradient");
        for (int i = 0; i < circles.size(); i++)
            circles.get(i).animate().with(colorTo(0xFF0000 + i * 0x010000), 3);

        // == Red Gradient Over Time (2) ==
        sleep(6);
        setTitle("Circle Projects - Red Gradient Over Time");
        for (var circle : circles)
            circle.animate().with(colorTo(0xFF0000), 1);

        // == Fall Down (2) ==
        sleep(3);
        setTitle("Circle Projects - Fall Down");
        for (var circle : circles)
            circle.animate().with(moveTo(circle.getX(), height + 25), (float) (Math.random() * 3 + 1));

        // == Circle Rows (3) ==
        setTitle("Circle Projects - Circle Rows");
        sleep();
        for (int y = 0; y < 10; y++)
            for (int x = 0; x < 10; x++)
                circles.get(y * 10 + x).animate().with(moveTo(width / 2 - 250 + x * 50, height / 2 - 275 + y * 50), 1);

        // == Hue Shift ==
        sleep(3);
        setTitle("Circle Projects - Hue Shift");
        for (var circle : circles)
            circle.animate().with(colorTo(Color.getHSBColor((float) Math.random(), 1, 1)), 1);
    }
}