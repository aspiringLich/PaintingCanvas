package examples;

import paintingcanvas.painter.App;

import paintingcanvas.painter.drawable.*;

public class DrawingInJava extends App {
    public static void main(String[] args) {
        new DrawingInJava().run();
    }

    @Override
    protected void setup() {
        setTitle("Drawing in Java");
        var width = 1000;
        var height = 600;

        // == Draw Landscape ==
        var sky = new Rectangle(0, 0, width, height / 2).setColor(0x87ceeb);
        var sun = new Circle(width / 2 - 50, 100, 50).setColor(0xffd700);
        var grass = new Rectangle(0, height / 2, width, height / 2).setColor(0x009a17);

        // ==  Draw House ==
        var house = new Rectangle(100, 250, 200, 150).setColor(0x1F48DA);
        var roof = new Triangle(100, 150, 200, 100).setColor(0x000000);
        var door = new Rectangle(150, 300, 50, 100).setColor(0xE8191A);

        // == Animate to Night ==
        sun.animate().with(moveTo(width / 2, height / 2 + 50), 7);
        sleep(3);

        var duration = 4;
        sky.animate().with(colorTo(0xF5976C), duration);
        grass.animate().with(colorTo(0x557C45), duration);
        house.animate().with(colorTo(0x010089), duration);
        roof.animate().with(colorTo(0x000000), duration);
        door.animate().with(colorTo(0x840001), duration);
    }
}
