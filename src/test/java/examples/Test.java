package examples;

import paintingcanvas.painter.App;
import paintingcanvas.painter.animation.Easing;

import java.awt.*;

public class Test extends App {
    public static void main(String[] args) {
        new Test().run();
    }

    @Override
    protected void setup() {
        for (int i = 1; i <= 5; i++) {
            var ball = new Circle(50, 55 * (i + 1), 50).setColor(Color.BLACK);
            ball.animate().with(moveTo(500, 55 * (i + 1)).easing(Easing.inOutNth(i)), 5);
        }
    }
}
