package examples.tessellation;

import paintingcanvas.Canvas;
import paintingcanvas.drawable.Circle;
import paintingcanvas.drawable.Line;
import paintingcanvas.drawable.Path;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.function.Supplier;

class Const {
    static final int size = 200;
    static final int edgePadding = size / 4;
    static final int centerHeight = size / 8;
    static final int tabHeight = size / 4;
    static final int deviation = 10;
    static final int extMin = size / 12;
    static final int extMax = size / 10;
    static final int height = (int) (size * Math.sqrt(3) / 2);
}

public class Tessellation {
    public static void main(String[] args) {
        Canvas canvas = new Canvas();
        canvas.setTitle("Tessellation project");
        
        int width = canvas.width();
        int height = canvas.height();
        
        var t = new Tile();
        t.draw(width / 2, height / 2);
        new Circle(width / 2, height / 2, 10, Color.RED).move(0, - Const.height - Const.centerHeight);
    }
}