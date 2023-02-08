package examples.tessellation;

import paintingcanvas.drawable.Line;
import paintingcanvas.drawable.Path;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.function.Supplier;

class Tile {
    Edge[] edges = new Edge[6];
    Path path = new Path();
    
    Tile() {
        for (int i = 0; i < 6; i++) {
            edges[i] = new Edge();
        }
    }
    
    void draw(int x, int y) {
        var size = Const.size;
        var height = Const.height;
        
        var p1 = new Point(-size / 2, -height);
        var p2 = new Point(size / 2, -height);
        Point c1 = new Point(p1), c2 = new Point(p2);
        c1.x += Const.edgePadding;
        c2.x -= Const.edgePadding;
        
        for (int i = 0; i < 2; i++) {
            var tf = AffineTransform.getRotateInstance(
                    Math.PI / 3 * i
            );
            Point tp1 = new Point(), tp2 = new Point();
            Point tc1 = new Point(), tc2 = new Point();
            tf.transform(p1, tp1);
            tf.transform(p2, tp2);
            tf.transform(c1, tc1);
            tf.transform(c2, tc2);
            
            var p = new Path().setPos(x, y);
            p.cursorTo(tp1.x, tp1.y)
                    .lineTo(tc1.x, tc1.y);
            
            var points = edges[i].get_path();
            for (int j = 1; j < points.size(); j += 2) {
                var end = points.get(j);
                var ctrl = points.get(j - 1);
                tf.transform(end, end);
                tf.transform(ctrl, ctrl);
                p.quadTo(ctrl.x, ctrl.y, end.x, end.y);
            }
            
            p.cursorTo(tc2.x, tc2.y)
                    .lineTo(tp2.x, tp2.y);
        }
    }
}

class Edge {
    boolean flip;
    
    Edge() {
        flip = Math.random() > 0.5 ? -1 : 1;
        
        Supplier<Integer> genDeviation = () ->
                (int) ((Math.random() - 0.5) * Const.deviation);
        
        var center = new Point(0, - Const.height - Const.centerHeight);
        p1 = new Point(center);
        p1.x += genDeviation.get() - Const.deviation;
        p1.y += genDeviation.get();
        p2 = new Point(center);
        p2.x += genDeviation.get() + Const.deviation;
        p2.y += genDeviation.get();
        
        var diff = Const.extMax - Const.extMin;
    }
    
    ArrayList<Point> get_path() {
        ArrayList<Point> out = new ArrayList<>();
        var size = Const.size;
        var height = Const.height;
        var padding = Const.edgePadding;
        
        var l1 = new Point(-size / 2 + padding, -height);
        var l2 = new Point(-size / 2 + padding, -height - Const.tabHeight);
        
        var center = new Point(0, -height - Const.centerHeight - Const.tabHeight * 2);
        
        var r2 = new Point(size / 2 - padding, -height - Const.tabHeight);
        var r1 = new Point(size / 2 - padding, -height);
        
        out.add(this.p1);
        out.add(l2);
//        out.add(l3);
        out.add(center);
//        out.add(r3);
        out.add(r2);
        out.add(this.p2);
        out.add(r1);
        return out;
    }
}

class Line_ {
    Point p1;
    Point p2;
    
    Line_(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }
    
    Line_(int x1, int y1, int x2, int y2) {
        this.p1 = new Point(x1, y1);
        this.p2 = new Point(x2, y2);
    }
    
    /**
     * Extends the line by the given amount
     *
     * @param ext amount to extend by
     * @return the extended line
     */
    Line_ extend(int ext) {
        int dx = p2.x - p1.x;
        int dy = p2.y - p1.y;
        double len = Math.sqrt(dx * dx + dy * dy);
        double scale = (len + ext) / len;
        int x = (int) (dx * scale);
        int y = (int) (dy * scale);
        return new Line_(p1.x, p1.y, p1.x + x, p1.y + y);
    }
    
    Point intersect(Line_ other) {
        double x1 = p1.x, y1 = p1.y;
        double x2 = p2.x, y2 = p2.y;
        double x3 = other.p1.x, y3 = other.p1.y;
        double x4 = other.p2.x, y4 = other.p2.y;
        
        double dx12 = x1 - x2, dx34 = x3 - x4;
        double dy12 = y1 - y2, dy34 = y3 - y4;
        double d1212 = x1 * y2 - y1 * x2;
        double d3434 = x3 * y4 - x4 * y3;
        
        double d = dx12 * dy34 - dy12 * dx34;
        double x = (d1212 * dx34 - dx12 * d3434) / d;
        double y = (d1212 * dy34 - dy12 * d3434) / d;
        
        return new Point((int) x, (int) y);
    }
}