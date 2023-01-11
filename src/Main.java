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
                .add(moveTo(200, 200), 40)
                .wait(100)
                .with(rotateTo(360), 50)
                .with(rotateTo(720), 50)
                .with(rotateTo(0), 50)
                .add(colorTo(Color.BLUE), 50);
    }
}