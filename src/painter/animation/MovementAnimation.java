package painter.animation;

import painter.drawable.Drawable;

import java.awt.*;

/**
 * Controls animation that is to do with position
 */
public class MovementAnimation extends Animation {
    private final Point end;
    private Point start;

    public MovementAnimation(int start, int duration, Point end, Drawable drawable) {
        super(start, duration, drawable);
        this.end = end;
    }

    @Override
    void updateAnimation(Drawable drawable, int frame, int duration) {
        if (frame == 0 || start == null) this.start = new Point(drawable.x, drawable.y);
        drawable.x = (int) (start.x + (end.x - start.x) * (frame / (float) duration));
        drawable.y = (int) (start.y + (end.y - start.y) * (frame / (float) duration));
    }
}
