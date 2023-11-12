package paintingcanvas.animation;

import paintingcanvas.drawable.Drawable;
import paintingcanvas.drawable.Positionable;
import paintingcanvas.misc.Misc;

/**
 * Controls animation that is to do with rotation
 */
public class RotationAnimation extends Animation {
    public double end;
    public Double start = null;
    public boolean relative = false;

    public RotationAnimation(double end) {
        super();
        this.end = end;
    }

    public RotationAnimation relative() {
        relative = true;
        return this;
    }

    @Override
    public Animation copy() {
        var out = new RotationAnimation(end);
        out.copy(this);
        out.relative = relative;
        return out;
    }

    @Override
    protected void updateAnimation(Drawable<? extends Drawable<?>> drawable, double progress) {
        var t = easing.ease(progress);
        var p = Misc.castDrawable(drawable, Positionable.class);
        p.setRotation(start + (end - start) * t);
    }

    @Override
    protected void initAnimation(Drawable<? extends Drawable<?>> drawable) {
        var p = Misc.castDrawable(drawable, Positionable.class);
        start = p.getRotation();
        if (relative) {
            end += start;
        }
    }
}
