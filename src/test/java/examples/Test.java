package examples;

import paintingcanvas.painter.App;
import paintingcanvas.painter.drawable.Circle;

import java.awt.*;
public class Test extends App {
    public static void main(String[] args) {
        new Test().run();
    }

    @Override
    protected void setup() {
        var circle = new Circle(100, 100, 40);
        var second = new Circle(200, 100, 40);
        var center = new Circle(-20, -20, 40);

        // the move should happen slightly after the color change
        circle.animate()
                .add(colorTo(Color.RED), 5)
                .wait(1)
                .with(moveTo(100, 200), 5);
        // should start moving at the same time as the first one
        second.animate()
                .with(moveTo(300, 100), 10);
        // this color change should happen after everything
        circle.animate()
                .add(colorTo(Color.BLUE), 5);
    }
}
