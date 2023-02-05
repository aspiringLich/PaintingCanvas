package paintingcanvas.animation;

import paintingcanvas.drawable.Drawable;

import java.awt.*;

/**
 * Controls animation that is to do with position
 */
public class MovementAnimation extends Animation {
    public Point end;
    public Point start;
    public boolean relative = false;


    public MovementAnimation(int start, int duration, Point end, Drawable drawable) {
        super(start, duration, drawable);
        this.end = end;
    }

    public MovementAnimation relative() {
        relative = true;
        return this;
    }

    @Override
    void updateAnimation(Drawable drawable, double progress) {
        var t = (double) easing.ease(progress);
        drawable.x = (int) (start.x + (end.x - start.x) * t);
        drawable.y = (int) (start.y + (end.y - start.y) * t);
    }

    @Override
    void initAnimation(Drawable drawable) {
        this.start = new Point(drawable.x, drawable.y);
        if (relative) {
            end.x += start.x;
            end.y += start.y;
        }
    }
}
