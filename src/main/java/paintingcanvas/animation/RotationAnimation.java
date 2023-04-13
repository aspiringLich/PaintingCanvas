package paintingcanvas.animation;

import paintingcanvas.drawable.Drawable;

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
        drawable.setRotation(start + (end - start) * t);
    }

    @Override
    protected void initAnimation(Drawable<? extends Drawable<?>> drawable) {
        start = drawable.getRotation();
        if (relative) {
            end += start;
        }
    }
}
