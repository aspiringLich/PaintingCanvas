package examples;

import paintingcanvas.animation.Animation;
import paintingcanvas.canvas.Canvas;
import paintingcanvas.drawable.Circle;
import paintingcanvas.drawable.Rectangle;
import paintingcanvas.drawable.Triangle;

public class DrawingInJava {
    public static void main(String[] args) {
        Canvas canvas = new Canvas();
        canvas.setTitle("Drawing in Java");

        var width = canvas.getWidth();
        var height = canvas.getHeight();

        // == Draw Landscape ==
        var sky = new Rectangle(width / 2, height / 4, width, height / 2).setColor(0x87ceeb);
        var sun = new Circle(width / 2 - 50, 100, 50).setColor(0xffd700);
        var grass = new Rectangle(width / 2, height / 4 * 3, width, height / 2).setColor(0x009a17);

        // ==  Draw House ==
        var house = new Rectangle(300, 400, 200, 150).setColor(0x1F48DA);
        var roof = new Triangle(300, 275, 200, 100).setColor(0x000000);
        var door = new Rectangle(250, 425, 50, 100).setColor(0xE8191A);

        // == Animate to Night ==
        sun.animate().add(Animation.moveTo(width / 2, height / 2 + 50), 7).sleep(0.003);

        var duration = 4;
        sky.animate().with(Animation.colorTo(0xF5976C), duration);
        grass.animate().with(Animation.colorTo(0x557C45), duration);
        house.animate().with(Animation.colorTo(0x010089), duration);
        roof.animate().with(Animation.colorTo(0x000000), duration);
        door.animate().with(Animation.colorTo(0x840001), duration);
    }
}
