package paintingcanvas.painter.drawable;

import java.awt.*;

public class Rectangle extends Drawable {
    int width, height;

    public Rectangle(int x, int y, int width, int height) {
        super(x, y, Color.BLACK);
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(Graphics g) {
        var gc = (Graphics2D) g;
        gc.setColor(color);
        if (this.filled) gc.fillRect(x, y, width, height);
        else gc.drawRect(x, y, width, height);
    }


    @Override
    public Point center(Graphics g) {
        return new Point(x + width / 2, y + height / 2);
    }
}
