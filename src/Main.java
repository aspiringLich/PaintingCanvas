import painter.App;

import java.awt.*;

public class Main extends App {
    final java.awt.Polygon polygon = new java.awt.Polygon(new int[]{
            0,
            100,
            200
    }, new int[]{
            0,
            100,
            0
    }, 3);

    public static void main(String[] args) {
        new Main().run();
    }

    @Override
    public void setup() {
        var dot = new Circle(400, 400, 50);
        var egg = new Ellipse(200, 700, 50, 100);
        var text = new Text(100, 100, "Hello World").setColor(Color.RED);
        var poly = new Polygon(800, 400, polygon).setColor(Color.BLUE);
        var tri = new Triangle(500, 300, 20, -40).setColor(Color.PINK);
        text.setText("Hello World!");
        text.animate(moveTo(200, 200), 100).animate(rotateTo(360), 50).animateWith(colorTo(Color.BLUE), 50);
        dot.animateWith(moveTo(100, 100), 50);
        egg.animateWith(moveTo(300, 100), 50);
        poly.animateWith(moveTo(300, 300), 50).animateWith(rotateTo(360 * 4), 50);
        tri.animate(colorTo(Color.RED), 50);
    }
}