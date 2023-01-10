package painter.drawable;

import java.awt.*;

/**
 * An interface to connect to any objects that can be considered "painter.drawable.Drawable".
 * Provides various common methods to
 */
public interface Drawable {
    public void draw(Graphics g);

    public int getX();

    public int getY();

    public void setX();

    public void setY();
}
