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
        var text = new Text(100, 100, "Hello World").setColor(0xff0000);
        var poly = new Polygon(800, 400, polygon).setColor(Color.BLUE);
        var tri = new Triangle(500, 300, 20, -40).setColor(Color.PINK);


        text.animate()
                .add(moveTo(200, 200), 2)
                .wait(2)
                .with(rotateTo(360), 2)
                .with(colorTo(Color.BLUE), 2)
                .add(rotateTo(0), 2)
                .with(colorTo(Color.RED), 2)
                .wait(3);

        dot.animate()
                .with(moveTo(400, 200), 1)
                .add(moveTo(400, 400), 1)
                .add(moveTo(400, 200), 1);
    }
}