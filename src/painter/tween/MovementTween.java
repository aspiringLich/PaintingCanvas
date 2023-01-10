package painter.tween;

import painter.drawable.Drawable;

import java.awt.*;

public class MovementTween extends Tween {
    private final Point end;
    private Point start;

    public MovementTween(int start, int duration, Point end, Drawable drawable) {
        super(start, duration, drawable);
        this.end = end;
    }

    @Override
    void updateTween(Drawable drawable, int frame, int duration) {
        if (frame == 0) this.start = new Point(drawable.x, drawable.y);
        drawable.x = (int) (start.x + (end.x - start.x) * (frame / (float) duration));
        drawable.y = (int) (start.y + (end.y - start.y) * (frame / (float) duration));
    }
}
