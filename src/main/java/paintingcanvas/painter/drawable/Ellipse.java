package paintingcanvas.painter.drawable;

import java.awt.*;

public class Ellipse extends Drawable {
    int width, height;

    public Ellipse(int x, int y, int width, int height) {
        super(x, y, Color.BLACK);
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(Graphics g) {
        var gc = (Graphics2D) g;
        gc.setColor(color);
        if (this.filled) gc.fillOval(x, y, width, height);
        else gc.drawOval(x, y, width, height);
    }

    @Override
    public Point center(Graphics g) {
        return new Point(x + width / 2, y + height / 2);
    }
}
