package paintingcanvas.animation;

import paintingcanvas.drawable.Drawable;

/**
 * Controls animation that is to do with rotation
 */
public class RotationAnimation extends Animation {
    public double end;
    public Double start = null;
    public boolean relative = false;

    public RotationAnimation(int start, int duration, double end, Drawable drawable) {
        super(start, duration, drawable);
        this.end = end;
    }

    public RotationAnimation relative() {
        relative = true;
        return this;
    }

    @Override
    void updateAnimation(Drawable drawable, double progress) {
        var t = (double) easing.ease(progress);
        drawable.rotation = start + (end - start) * t;
    }

    @Override
    void initAnimation(Drawable drawable) {
        start = drawable.rotation;
        if (relative) {
            end += start;
        }
    }
}
