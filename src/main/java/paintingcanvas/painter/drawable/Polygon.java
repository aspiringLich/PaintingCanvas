package paintingcanvas.painter.drawable;

import java.awt.*;

public class Polygon extends Drawable {
    private final java.awt.Polygon polygon;

    public Polygon(int x, int y, int[] xPoints, int[] yPoints) {
        super(x, y, Color.BLACK);
        this.polygon = new java.awt.Polygon(xPoints, yPoints, xPoints.length);
    }

    public Polygon(int x, int y, java.awt.Polygon polygon) {
        super(x, y, Color.BLACK);
        this.polygon = polygon;
    }

    @Override
    public void draw(Graphics g) {
        var gc = (Graphics2D) g;
        gc.setColor(color);
        polygon.translate(x, y);
        if (this.filled) gc.fillPolygon(polygon);
        else gc.drawPolygon(polygon);
        polygon.translate(-x, -y);
    }


    @Override
    public Point center(Graphics g) {
        return new Point(x + polygon.getBounds().width / 2, y + polygon.getBounds().height / 2);
    }
}
