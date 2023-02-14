package examples;

import paintingcanvas.Canvas;
import paintingcanvas.animation.Animation;
import paintingcanvas.drawable.Circle;
import paintingcanvas.drawable.Rectangle;
import paintingcanvas.drawable.Triangle;

public class DrawingInJava {
    public static void main(String[] args) {
        Canvas canvas = new Canvas();
        canvas.setTitle("Drawing in Java");

        var width = canvas.width();
        var height = canvas.height();

        // == Draw Landscape ==
        var sky = new Rectangle(0, 0, width, height / 2).setColor(0x87ceeb);
        var sun = new Circle(width / 2 - 50, 100, 50).setColor(0xffd700);
        var grass = new Rectangle(0, height / 2, width, height / 2).setColor(0x009a17);

        // ==  Draw House ==
        var house = new Rectangle(100, 250, 200, 150).setColor(0x1F48DA);
        var roof = new Triangle(100, 150, 200, 100).setColor(0x000000);
        var door = new Rectangle(150, 300, 50, 100).setColor(0xE8191A);

        // == Animate to Night ==
        sun.animate().add(Animation.moveTo(width / 2, height / 2 + 50), 7).wait(3);

        var duration = 4;
        sky.animate().with(Animation.colorTo(0xF5976C), duration);
        grass.animate().with(Animation.colorTo(0x557C45), duration);
        house.animate().with(Animation.colorTo(0x010089), duration);
        roof.animate().with(Animation.colorTo(0x000000), duration);
        door.animate().with(Animation.colorTo(0x840001), duration);
    }
}
