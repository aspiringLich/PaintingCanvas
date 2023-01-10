import painter.App;

import java.awt.*;

public class Main extends App {
    public static void main(String[] args) {
        new Main().run();
    }

    @Override
    public void setup() {
        var dot = new Circle(400, 400, 50);
        var text = new Text(100, 100, "Hello World").setColor(Color.RED);
        text.setText("Hello World!");
        text.animate(moveTo(200, 200), 100)
                .animate(rotateTo(360), 50)
                .animateWith(colorTo(Color.BLUE), 50);
        dot.animateWith(moveTo(100, 100), 50);
    }
}