package paintingcanvas.painter.animation;

import paintingcanvas.painter.drawable.Drawable;

import java.awt.*;

/**
 * Controls animation that is to do with position
 */
public class MovementAnimation extends Animation {
    public Point end;
    public Point start;

    public MovementAnimation(int start, int duration, Point end, Drawable drawable) {
        super(start, duration, drawable);
        this.end = end;
    }

    @Override
    void updateAnimation(Drawable drawable, int frame, int duration) {
        if (frame == 0 || start == null) this.start = new Point(drawable.x, drawable.y);
        var t = (float) easing.ease(frame / (double) duration);
        drawable.x = (int) (start.x + (end.x - start.x) * t);
        drawable.y = (int) (start.y + (end.y - start.y) * t);
    }
}
