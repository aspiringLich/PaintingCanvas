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


    public MovementAnimation(Point end) {
        super();
        this.end = end;
    }

    public MovementAnimation relative() {
        relative = true;
        return this;
    }

    @Override
    public Animation copy() {
        return new MovementAnimation(end);
    }

    @Override
    protected void updateAnimation(Drawable drawable, double progress) {
        var t = easing.ease(progress);
        drawable.x = (int) (start.x + (end.x - start.x) * t);
        drawable.y = (int) (start.y + (end.y - start.y) * t);
    }

    @Override
    protected void initAnimation(Drawable drawable) {
        this.start = new Point(drawable.x, drawable.y);
        if (relative) {
            end.x += start.x;
            end.y += start.y;
        }
    }
}
