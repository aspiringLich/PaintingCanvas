package paintingcanvas.animation;

import paintingcanvas.drawable.Drawable;
import paintingcanvas.drawable.Positionable;

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
        var out = new MovementAnimation(end);
        out.copy(this);
        out.relative = relative;
        return out;
    }

    @Override
    protected void updateAnimation(Drawable<?> drawable, double progress) {
        var t = easing.ease(progress);
        var p = (Positionable<?>) drawable;
        p.setPos(
                (int) (start.x + (end.x - start.x) * t),
                (int) (start.y + (end.y - start.y) * t)
        );
    }

    @Override
    protected void initAnimation(Drawable<?> drawable) {
        var p = (Positionable<?>) drawable;
        this.start = p.getPos();
        if (relative) {
            end.x += p.getX();
            end.y += p.getY();
        }
    }
}
