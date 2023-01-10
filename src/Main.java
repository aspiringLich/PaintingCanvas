import painter.App;

import java.awt.*;

public class Main extends App {
    public static void main(String[] args) {
        new Main().run();
    }

    @Override
    public void setup() {
        var text = new Text(100, 100, "Hello World").setColor(Color.RED);
        text.setText("Hello World!");
        text.moveTo(200, 200, 100);
        text.rotateTo(360, 50);
        text.colorTo(Color.BLUE, 50);
    }
}